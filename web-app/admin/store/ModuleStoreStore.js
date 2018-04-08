/**
 * Created by shqv on 2014-6-11.
 */
Ext.define('admin.store.ModuleStoreStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ModuleStoreModel',
    /*    autoSync: true,//需要同步    */
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'moduleStore/list',
            remove: 'moduleStore/delete',
            update: "moduleStore/update",
            insert: "moduleStore/insert"
        }
    },
    autoLoad:true
});
