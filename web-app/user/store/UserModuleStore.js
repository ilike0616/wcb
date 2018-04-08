/**
 * Created by like on 2015/8/31.
 */
Ext.define('user.store.UserModuleStore', {
    extend: 'Ext.data.TreeStore',
    model:'user.model.UserModuleModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'module/list'
        },
        reader:{
            type : 'json',
            root:'data',
            successProperty:'success'
        }
    },
    autoLoad:true,
    folderSort : true,
    root: {
        expanded: true,
        moduleName: "模块管理"
    }
});