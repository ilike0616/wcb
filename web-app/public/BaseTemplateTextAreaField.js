/**
 * Created by like on 2015-05-14.
 * 使用时必须配置storeId
 * 如storeId:'user.store.UserFieldStore'
 */
Ext.define('public.BaseTemplateTextAreaField', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.baseTemplateTextAreaField',
    enableKeyEvents: true,
    matchFieldWidth: true,
    hideTrigger: true,
    minChars: 1,
    width: 500,
    height: 80,
    grow:true,
    colspan:2,
    queryParam: 'searchTemplate',
    moduleId: 'customer',
    listConfig: {
        loadingText: '正在查找...',
        emptyText: '没有找到匹配的数据',
        getInnerTpl: function () {
            return '<span>{text}</span>';
        }
    },
    initComponent: function () {
        var me = this, storeId = me.storeId;
        me.store = Ext.create(storeId);
        me.mon(me.getPicker().getSelectionModel(), {
            beforeselect: me.onBeforeSelect,
            beforedeselect: me.onBeforeDeselect,
            selectionchange: me.onListSelectionChange,
            scope: me
        });
        me.callParent();
    },
    fieldSubTpl: [
        '<div class="{hiddenDataCls}" role="presentation"></div>',
        '<textArea id="{id}" type="{type}" {inputAttrTpl} class="{fieldCls} {typeCls} {editableCls}" width="{width}" height="{height}" autocomplete="off"',
        '<tpl if="value"> value="{[Ext.util.Format.htmlEncode(values.value)]}"</tpl>',
        '<tpl if="name"> name="{name}"</tpl>',
        '<tpl if="placeholder"> placeholder="{placeholder}"</tpl>',
        '<tpl if="maxLength !== undefined"> maxlength="{maxLength}"</tpl>',
        '<tpl if="readOnly"> readonly="readonly"</tpl>',
        '<tpl if="disabled"> disabled="disabled"</tpl>',
        '<tpl if="tabIdx"> tabIndex="{tabIdx}"</tpl>',
        '></textArea>',
        {
            compiled: true,
            disableFormats: true
        }
    ],
    /**
     * 选择一个后处理结构到文本域
     * @param list
     * @param selectedRecords
     */
    onListSelectionChange: function (list, selectedRecords) {
        var me = this,
            value = me.getValue(),
            hasRecords = selectedRecords.length > 0;
        if (!me.ignoreSelection && me.isExpanded) {
            if (hasRecords) {
                var text = selectedRecords[0].get('text'),      //选中项的现实名称
                    fieldName = selectedRecords[0].get('fieldName');    //选中项的字段名
                if (value != null) {
                    var pos = me.inputEl.dom.selectionStart,    //光标插入位置
                        top = value.substr(0, pos),                 //光标位置钱的字符串，下称“前串”
                        under = value.substr(pos),                  //光标位置后的字符串，下称“后串”
                        startPos1 = top.lastIndexOf('{'),            //前串中最后一个左大括号的位置
                        endPos1 = top.lastIndexOf('}'),
                        startPos2 = under.indexOf('{'),
                        endPos2 = under.indexOf('}');                //后产中第一个右大括号的位置
                    if (pos != -1) {    //能获取光标位置
                        if (startPos1 != -1 && endPos2 != -1 && ((startPos2 != -1 && endPos2 < startPos2) || startPos2 == -1)) {   //已经输入“{}”
                            value = top.substr(0, startPos1 + 1) + text + '#' + fieldName + under.substr(endPos2);
                        } else if (startPos1 != -1) {
                            value = top.substr(0, startPos1 + 1) + text + '#' + fieldName + '}' + under
                        }
                        me.setValue(value, false);
                    } else {
                        me.setValue(value);
                    }
                } else {
                    me.setValue('{' + text + '#' + fieldName + '}');
                }
                me.focus();
                //me.fireEvent('select', me, selectedRecords);
            }
        }
        me.getPicker().hide();
    },
    onListRefresh: function () {
        // Picker will be aligned during the expand call
        if (!this.expanding) {
            this.alignPicker();
        }
        this.syncSelection();
    },
    /**
     * 截取查询字符串
     * @param queryPlan
     * @returns {{query: String, forceAll: Boolean, cancel: Boolean, rawQuery: Boolean}}
     */
    beforeQuery: function (queryPlan) {
        var me = this;
        // Allow beforequery event to veto by returning false
        if (me.fireEvent('beforequery', queryPlan) === false) {
            queryPlan.cancel = true;
        }
        // Allow beforequery event to veto by returning setting the cancel flag
        else if (!queryPlan.cancel) {
            var query = queryPlan.query;    //输入的内容
            var endPos = me.inputEl.dom.selectionStart;   //光标位置
            var top = query.substr(0, endPos);
            var startPos = top.lastIndexOf('{');
            if (startPos == -1 || startPos < top.lastIndexOf('}')) {
                query = '';
            } else {
                query = top.substr(startPos + 1)
            }
            queryPlan.query = query;
            if (queryPlan.query.substr(-1) == '}') {
                queryPlan.cancel = true;
            }
            // If the minChars threshold has not been met, and we're not forcing an "all" query, cancel the query
            if (queryPlan.query.length < me.minChars && !queryPlan.forceAll) {
                queryPlan.cancel = true;
            }
            if (top.substr('-1') == '{') {
                queryPlan.cancel = false;
            }
            if (queryPlan.cancel == false) {
                me.getPicker().show();
            }
        }
        return queryPlan;
    },
    /**
     * doQuery -> doRemoteQuery -> getParams
     * @param queryString
     * @returns {{}}
     */
    getParams: function (queryString) {
        var params = {},
            param = this.queryParam;

        if (param) {
            params[param] = queryString;
        }
        //增加参数：模块
        if (this.moduleId) {
            params['moduleId'] = this.moduleId;
        }
        return params;
    },
    //取消选择时删除整块
    onCollapse: function () {
        var me = this,
            keyNav = me.listKeyNav;
        if (keyNav) {
            keyNav.disable();
            me.ignoreMonitorTab = false;
        }
        me.backspace();
    },
    // store the last key and doQuery if relevant
    onKeyUp: function (e, t) {
        var me = this,
            key = e.getKey();
        if (!me.readOnly && !me.disabled && me.editable) {
            me.lastKey = key;
            // we put this in a task so that we can cancel it if a user is
            // in and out before the queryDelay elapses

            // perform query w/ any normal key or backspace or delete
            if (!e.isSpecialKey() || key == e.BACKSPACE || key == e.DELETE) {
                me.doQueryTask.delay(me.queryDelay);
            }
        }
        //新增处理,下拉选择没有打开时按删除键
        if (key == e.BACKSPACE && !me.isExpanded) {
            me.backspace();
        }
        if (me.enableKeyEvents) {
            me.callParent(arguments);
        }
    },
    /**
     * 取消选择或删除时，删除整块
     */
    backspace: function () {
        var me = this, newValue,
            pos = me.inputEl.dom.selectionStart,    //光标插入位置
            value = me.getValue();
        if (value != null) {
            var top = value.substr(0, pos),
                bottom = value.substr(pos),
                topLeft = top.lastIndexOf('{'),
                topRight = top.lastIndexOf('}'),
                bottomLeft = bottom.indexOf('{'),
                bottomRight = bottom.indexOf('}');
            if (topLeft != -1) {
                if ((topRight != -1 && topLeft > topRight) || topRight == -1) {
                    if (bottomRight != -1) {
                        if ((bottomLeft != -1 && bottomLeft > bottomRight) || bottomLeft == -1) {   //***{}***{pos}***{}***; ***{}***{pos}***
                            newValue = top.substr(0, topLeft) + bottom.substr(bottomRight + 1);
                        } else if (bottomLeft != -1 && bottomLeft < bottomRight) { //***{}***{pos***{}***
                            newValue = top.substr(0, topLeft) + bottom;
                        }
                    } else {
                        newValue = top.substr(0, topLeft) + bottom; //***{}***{pos***
                    }
                } else if (topRight != -1 && topLeft < topRight) {
                    newValue = value
                }
            } else {
                newValue = value
            }
            me.setValue(newValue);
        }
    }
});