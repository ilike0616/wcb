/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('admin.store.ModuleListStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ModuleModel',
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'module/moduleList'
        }
    },
    autoLoad:true
});
