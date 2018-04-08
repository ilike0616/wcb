/**
 * Created by guozhen on 2014-07-04.
 */
Ext.define('user.store.RoleStore', {
    extend: 'Ext.data.TreeStore',
    model: modelFactory.getModelByModuleId('role'),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
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
    autoSync:true,
    autoLoad:false,
    root: {
        expanded: true,
        roleName: "用户角色"
    }
});
