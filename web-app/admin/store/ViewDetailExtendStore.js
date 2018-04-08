Ext.define('admin.store.ViewDetailExtendStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ViewDetailExtendModel',
    pageSize:-1,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'viewDetail/searchViewDetailExtend',
            insert: "viewDetail/insertProperty",
            update: 'viewDetail/updateProperty',
            remove: 'viewDetail/delProperty'
        }
    },
    autoLoad:true
});