/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.AdministratorStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.AdministratorModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'administrator/listForAdmin',
            remove: 'administrator/delete',
            update: "administrator/update",
            insert: "administrator/insert",
            save: 'administrator/save'
        }
    },
    autoLoad:true
});