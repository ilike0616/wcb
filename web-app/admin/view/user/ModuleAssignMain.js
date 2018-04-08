/**
 * Created by guozhen on 2015/4/1.
 */
Ext.define("admin.view.user.ModuleAssignMain",{
    extend: 'Ext.window.Window',
    alias: 'widget.userModuleAssignMain',
    modal: true,
    width: 900,
    height: 700,
    layout: 'border',
    title: '模块&操作',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [{
                region:'center',
                xtype:'panel',
                layout:'border',
                items:[{
                    region:'center',
                    width: 450,
                    split: true,
                    layout: 'fit',
                    title: '模块列表',
                    enableToolbar: false,
                    xtype: 'moduleAssignmentList',
                    listeners:{
                        scope:me,
                        checkchange : me.checkChange,
                        selectionchange: me.selectionChange
                    }
                },{
                    region:'east',
                    width: 400,
                    collapsible: true,
                    split: true,
                    layout: 'fit',
                    title: '操作列表',
                    store: Ext.create('admin.store.UserOperationStore'),
                    xtype: 'enableOperationList'
                }]
            },{
                xtype:'hiddenfield',
                name:'id'
            }]
        });
        me.callParent(arguments);
    },
    checkChange : function(record, checked, eOpts ){
        if(record.get('leaf')) {
            if(!checked) {
                record.set('checked', false);
            }
        }else{
            record.cascadeBy(function(record) {
                record.set('checked', checked);
            });
        }
    },
    selectionChange : function(o, record, eOpts){
        var me = this;
        var userId = me.down('hiddenfield[name=id]').getValue();
        var store = me.down('enableOperationList').getStore();
        Ext.apply(store.proxy.extraParams,{});
        if(Ext.typeOf(record[0]) != 'undefined'){
            Ext.apply(store.proxy.extraParams,{moduleId:record[0].get('id'),userId:userId});
            store.load();
        }
    }
})