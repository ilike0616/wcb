/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.SaleChanceFollowStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("sale_chance_follow"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'saleChanceFollow/list',
            remove: 'saleChanceFollow/delete',
            update: "saleChanceFollow/update",
            insert: "saleChanceFollow/insert",
            save: 'saleChanceFollow/save'
        }
    },
    autoSync:true,
    autoLoad:false
})