/**
 * Created by shqv on 2014-6-17.
 */
Ext.define('admin.store.ModelStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ModelModel',
    /*    autoSync: true,//需要同步    */
    pageSize:-1,  //不设置分页
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'model/list',
            remove: 'model/delete',
            update: "model/update",
            insert: "model/insert",
            save: 'model/save'
        }
    },
    autoLoad:true
});