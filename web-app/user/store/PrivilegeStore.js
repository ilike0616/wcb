/**
 * Created by shqv on 2014-6-17.
 */
Ext.define('user.store.PrivilegeStore', {
    extend: 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('privilege'),
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'privilege/list',
            remove: 'privilege/delete',
            update: "privilege/update",
            insert: "privilege/insert",
            save: 'privilege/save'
        }
    },
    autoLoad:true
});