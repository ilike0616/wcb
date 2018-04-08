Ext.define('admin.store.ViewDetailStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ViewDetailModel',
    /*    autoSync: true,//需要同步    */
    pageSize:-1,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'viewDetail/list',
            remove: 'viewDetail/delete',
            update: "viewDetail/update",
            insert: "viewDetail/insert",
            save: 'viewDetail/save'
        }
    },
    autoLoad:false
});