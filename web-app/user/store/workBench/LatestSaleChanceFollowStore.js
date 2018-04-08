/**
 * Created by like on 2015/6/16.
 */
Ext.define('user.store.workBench.LatestSaleChanceFollowStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('sale_chance_follow'),
    pageSize:10,
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
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
});