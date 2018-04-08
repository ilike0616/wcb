/**
 * Created by like on 2015/6/16.
 */
Ext.define('user.store.workBench.LatestSaleChanceStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('sale_chance'),
    pageSize:10,
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'saleChance/list',
            remove: 'saleChance/delete',
            update: "saleChance/update",
            insert: "saleChance/insert",
            save: 'saleChance/save'
        }
    },
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
});