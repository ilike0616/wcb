/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ProductStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('product'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'product/list',
            remove: 'product/delete',
            update: "product/update",
            insert: "product/insert",
            save: 'product/save'
        }
    },
    autoLoad:false
});