/**
 * Created by shqv on 2014-6-11.
 */

Ext.define('admin.store.UserModuleStore', {
    extend: 'Ext.data.TreeStore',
    model:'admin.model.ModuleModel',
    alias: 'widget.UserModuleStore',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
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
        }
    },
    autoLoad:false,
    folderSort : true
});
