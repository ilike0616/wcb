/**
 * Created by guozhen on 2014-07-04.
 */
Ext.define('admin.store.RoleStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.RoleModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'role/list',
            remove: 'role/delete',
            update: "role/update",
            insert: "role/insert",
            save: 'role/save'
        }
    },
    autoLoad:true
});
