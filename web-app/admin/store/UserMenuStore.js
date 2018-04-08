/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("admin.store.UserMenuStore",{
    extend: 'Ext.data.TreeStore',
    model : 'admin.model.UserMenuModel',
    root: {
        expanded: true,
        text: "菜单管理"
    },
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userMenu/list',
            remove: 'userMenu/delete',
            update: "userMenu/update",
            insert: "userMenu/insert",
            save: 'userMenu/save'
        },
        reader:'json'
    }
});