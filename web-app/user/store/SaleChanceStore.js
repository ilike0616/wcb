/**
 * Created by guozhen on 2015/04/13.
 */
Ext.define('user.store.SaleChanceStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("sale_chance"),
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
            save: 'saleChance/save',
            map: 'saleChance/locusMap'
        }
    },
    autoSync:true,
    autoLoad:false
});