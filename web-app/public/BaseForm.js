/**
 * Created by shqv on 2014-9-16.
 */
Ext.define('public.BaseForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.baseForm',
    changeWinTitle:true,
    winTitle:'',
    maxHeight:600,
//    maxWidth:650,
//    autoScroll:true,
    overflowY:'auto',
    allowDomMove:true,
    layout: {
        type: 'table',
        columns: 2
    }, 
    fieldDefaults: {
        labelWidth: 90,
        labelAlign:'right',
        width : 330
    },
    bodyStyle: 'padding:5px 5px 0',
    defaults: {
        border: 0,
        msgTarget: 'side', //qtip title under side
        xtype: 'textfield'
    },
    buttonAlign:'center',
    initComponent: function() {
        var me = this,items=me.items,title,moduleId=me.moduleId,viewId=me.viewId;
        me.layout.columns = Ext.typeOf(me.paramColumns) == 'undefined' ? 2 : me.paramColumns;
        if(!items&&viewId){
            viewId = me.replaceFirstUper(viewId);
            if(me.unLocalStorage||!window.localStorage.getItem(viewId)) {
                var url = "view/form?viewId=" + viewId;
                if (me.url != "" && Ext.typeOf(me.url) != 'undefined') {
                    url = me.url;
                }
                Ext.Ajax.request({
                    url: url,
                    method: 'POST',
                    timeout: 4000,
                    async: false,
                    success: function (response, opts) {
                        var view = Ext.JSON.decode(response.responseText);
                        items = view.items;
                        me.winTitle = view.title;
                        moduleId = view.moduleId;
                        if (!me.paramColumns) {
                            me.layout.columns = view.paramColumns;
                        }
                        if (view.associatedRequired) {
                            me.associatedRequired = view.associatedRequired;
                        }
                        window.localStorage .setItem(viewId,response.responseText);
                    }
                });
            }else{
                var view = Ext.JSON.decode(window.localStorage.getItem(viewId));
                items = view.items;
                me.winTitle = view.title;
                moduleId = view.moduleId;
                if (!me.paramColumns) {
                    me.layout.columns = view.paramColumns;
                }
                if (view.associatedRequired) {
                    me.associatedRequired = view.associatedRequired;
                }
            }
        }
        if(me.layout.columns==1){
        	 var menu = Ext.Array.every(items,function(o){ //单纯的遍历数组    
                 delete o.width;
                 delete o.colspan;
                 return true;
             });
        }
        Ext.applyIf(me, {
            moduleId:moduleId,
            items : items
        });
        me.callParent(arguments);
    },
    replaceFirstUper:function(str){
        return str.replace(/\b(\w)|\s(\w)/g, function(m){
            return m.toUpperCase();
        });
    }
})
