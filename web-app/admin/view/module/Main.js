Ext.define('admin.view.module.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.moduleMain',
    layout: 'border',
    items: [
        {
            region:'west',
            title:'模块',
            xtype: 'moduleList',
            width: 300,
            collapsible: true,
            layout: 'fit'
        },
        {
            region:'center',
            xtype: 'panel',
            layout: 'border',
            items: [
                {
                    region:'center',
                    xtype:'tabpanel',
                    layout: 'fit',
                    items:[
                       {
                    	   xtype: 'operationList',
                           store : Ext.create('admin.store.OperationStore'),
                           title: '全部操作',
                           clientType:'all',
                           split: true
                       },
                       {
                    	   xtype: 'operationList',
                    	   clientType:'pc',
                           store : Ext.create('admin.store.OperationStore'),
                           title: 'PC端操作',
                           split: true
                       },
                       {
                    	   xtype: 'operationList',
                    	   clientType:'mobile',
                           store : Ext.create('admin.store.OperationStore'),
                           title: '手机操作',
                           split: true
                       },{
                            xtype:'portalList',
                            store:Ext.create('admin.store.PortalStore'),
                            title:'Portal',
                            split:true
                        },{
                            xtype:'moduleStoreList',
                            store:Ext.create('admin.store.ModuleStoreStore'),
                            title:'Store',
                            split:true
                        }
                    ]
                },
                {
                    region: 'south',
                    collapsed:true,
                    xtype: 'userList',
                    title: '公司',
                    height: 300,
                    split: true,
                    collapsible: true,
                    store:Ext.create('admin.store.UserStore')
                }
            ]
        }
    ]
});