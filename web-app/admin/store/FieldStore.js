Ext.define('admin.store.FieldStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.FieldModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'field/list',
            remove: 'field/delete',
            update: "field/update",
            insert: "field/insert",
            save: 'field/save'
        }
    },
    autoLoad:false
});