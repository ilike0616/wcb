/**
 * Created by like on 2015/8/27.
 */
Ext.define('user.store.SfaStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('sfa'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'sfa/list',
            remove: 'sfa/delete',
            update: "sfa/update",
            insert: "sfa/insert",
            save: 'sfa/save',
            enable:'sfa/enable',
            disable:'sfa/disable'
        }
    },
    autoLoad:true
});