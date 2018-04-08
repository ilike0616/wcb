Ext.define('admin.store.UserFieldStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.UserFieldModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userField/list',
            remove: 'userField/delete',
            update: "userField/update",
            insert: "userField/insert",
            save: 'userField/save'
        }
    },
    autoSync:true,
    autoLoad:false
});