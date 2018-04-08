Ext.define('admin.store.ViewOperationStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ViewOperationModel',
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'viewOperation/list',
            remove: 'viewOperation/delete',
            update: "viewOperation/update",
            insert: "viewOperation/insert",
            save: 'viewOperation/save'
        }
    },
    autoLoad:true,
    sorters:[{
        property : 'orderIndex',
        direction: 'ASC'
    }]
});