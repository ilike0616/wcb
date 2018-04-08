Ext.define('public.BaseDetailTextField',{
    extend : 'Ext.form.field.ComboBox',
    alias:'widget.baseDetailTextField',
    enableKeyEvents:true,
    matchFieldWidth : false,
    minChars: 1,
    queryParam : 'searchValue',
    listConfig: {
        loadingText: '正在查找...',
        emptyText: '没有找到匹配的数据'
    },
    columns : null,
    title:'产品列表',
    initComponent: function() {
        var me = this,
            fieldLabel = me.label;
        if(Ext.typeOf(me.title) != "undefined"){
            this.title = me.title;
        }
        this.storeName = me.store;
        me.store = Ext.create('user.store.'+me.store);
        me.store.load();
        if(me.viewId){
            var url = 'view?viewId='+me.viewId;
            if(me.url != "" && Ext.typeOf(me.url) != 'undefined') {
                url = me.url;
            }
            Ext.Ajax.request({
                url:url,
                method:'POST',
                timeout:4000,
                async: false,
                success:function(response,opts){
                    var view = Ext.JSON.decode(response.responseText);
                    this.columns = view.columns;
                } ,
                failure:function(e,op){
                    Ext.Msg.alert("发生错误");
                }
            });
        }

        Ext.applyIf(me, {
            listeners : {
                scope : me,
                focus :function(o, The, eOpts){
                    me.reloadStore();
                },
                keyup:function(o, e, eOpts ){
                    var grid = o.up('baseEditList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    record.set(me.hiddenName+'.id',null);
                },
                blur : function(o, The, eOpts ){
                    var grid = o.up('baseEditList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    if(record.get(me.hiddenName+'.id') == null){
                        record.set(me.hiddenName+'.id',null);
                        o.setValue(null);
                    }else{
                        o.setValue(record.get(me.name));
                    }
                }
            }
        });
        me.callParent(arguments);
    },
    createPicker : function(){
        var me = this;
        var picker = Ext.create('Ext.grid.Panel', {
                title : me.title,
                store: me.store,
                frame: true,
                autoScroll:true,
                forceFit : true,
                resizable : true,
                columns : columns,
                selModel: {
                    mode:'SINGLE'
                },
                floating: true,
                hidden: true,
                focusOnToFront: false,
                forceSelection: true,
                width:600
        });
        me.mon(picker.getSelectionModel(), {
            beforeselect: me.onBeforeSelect,
            beforedeselect: me.onBeforeDeselect,
            selectionchange: me.onListSelectionChange,
            scope: me
        });
        this.picker = picker;
        return picker;
    },
    onListSelectionChange: function(list, selectedRecords) {
        var me = this,
            hasRecords = selectedRecords.length > 0;
        if (!me.ignoreSelection && me.isExpanded) {
            if (hasRecords) {
                me.setValue(selectedRecords, false);
                me.fireEvent('select', me, selectedRecords);
            }

            var grid = me.up('baseEditList');
            var record = grid.getSelectionModel().getSelection()[0];
            me.customUpdateRecord(selectedRecords,record);
            me.inputEl.focus();
            Ext.defer(me.collapse, 1, me);
        }
    },
    doAutoSelect: function() {
        var me = this,
            picker = me.picker,
            lastSelected, itemNode;
        if (picker && me.autoSelect && me.store.getCount() > 0) {
            // Highlight the last selected item and scroll it into view
            lastSelected = picker.getSelectionModel().lastSelected;
            itemNode = picker.view.getNode(lastSelected || 0);
            if (itemNode) {
                picker.view.highlightItem(itemNode);
                picker.view.el.scrollChildIntoView(itemNode, false);
            }
        }
    },
    customUpdateRecord : function(selData,record){
        var me = this;
        var models = record.store.model.getFields();
        if(record != null){
            Ext.Array.each(models, function(model, index) {
                // 处理关联字段
                var preName = "";
                var fieldName = model.name;
                var index = fieldName.indexOf(".");
                if(index > 0){
                    preName = fieldName.substr(0,index);
                    fieldName = fieldName.substr(index+1);
                }
                if(preName == me.hiddenName){
                    record.set(model.name,selData[0].data[fieldName]);
                    if(me.name == model.name){
                        me.setValue(selData[0].data[fieldName])
                    }
                }
                // 处理带initName字段
                var initPreName = "";
                var initFieldName = model.initName;
                if(initFieldName != null && Ext.typeOf(initFieldName) != 'undefined'){
                    var initIndex = initFieldName.indexOf(".");
                    if(initIndex > 0){
                        initPreName = initFieldName.substr(0,initIndex);
                        initFieldName = initFieldName.substr(initIndex+1);
                    }
                    if(initPreName == me.hiddenName){
                        record.set(model.name,selData[0].data[initFieldName]);
                    }
                }
            });
        }
    },
    reloadStore : function(){
        var me = this;
        Ext.apply(me.store.proxy.extraParams, {specialParam:""});
        me.store.clearFilter();
        var parentFormObj = me.ownerCt.ownerCt;
        if(Ext.typeOf(me.paramName) != 'undefined' && me.paramName != ''){
            var paramValue = parentFormObj.down("hiddenfield[name="+me.paramName+".id]").getValue();
            Ext.apply(me.store.proxy.extraParams, {specialParam:paramValue});
            me.store.load();
        }
    }
});