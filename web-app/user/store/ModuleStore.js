/**
 * Created by shqv on 2014-6-11.
 */

Ext.define('user.store.ModuleStore', {
    extend: 'Ext.data.TreeStore',
    model:'admin.model.UserMenuModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userMenu/list'
        },
        reader:{
            type:'json'
        }
    },
    root: {
        expanded: true,
        text: "模块管理"
    }
});
