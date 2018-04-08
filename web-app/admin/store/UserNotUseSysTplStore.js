/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.store.UserNotUseSysTplStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.UserModel',
    /*    autoSync: true,//需要同步    */
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'user/list?useSysTpl=false',
            remove: 'user/delete',
            update: "user/update",
            insert: "user/insert",
            save: 'user/save'
        }
    },
    autoLoad:true
});
