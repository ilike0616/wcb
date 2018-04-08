Ext.define('admin.store.ViewStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ViewModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'view/list',
            remove: 'view/delete',
            update: "view/update",
            insert: "view/insert",
            save: 'view/save'
        }
    },
    autoLoad:false
});