/**
 * Created by shqv on 2014-6-11.
 */
Ext.define('admin.store.ModuleStore', {
    extend: 'Ext.data.TreeStore',
    model:'admin.model.ModuleModel',
    /*    autoSync: true,//需要同步    */
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'module/list',
            remove: 'module/delete',
            update: "module/update",
            insert: "module/insert",
            save: 'module/save'
        },
        reader : {
            type : 'json',
            root:'data',
            successProperty:'success'
        }
    },
    autoLoad:false,
    folderSort : true,
    root: {
        expanded: true,
        moduleName: "模块管理"
    }
});
