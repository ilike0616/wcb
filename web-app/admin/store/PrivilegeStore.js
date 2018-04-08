/**
 * Created by shqv on 2014-6-17.
 */
Ext.define('admin.store.PrivilegeStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.PrivilegeModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'privilege/listForAdmin',
            remove: 'privilege/delete',
            update: "privilege/update",
            insert: "privilege/insert",
            save: 'privilege/save'
        }
    },
    autoLoad:true
});