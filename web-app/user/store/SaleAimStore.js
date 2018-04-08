/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.SaleAimStore',{
    extend : 'Ext.data.TreeStore',
    model:'user.model.SaleAimModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'saleAim/list'
        },
        reader : {
            type : 'json',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    autoLoad:true,
    folderSort : true,
    root: {
        expanded: true,
        name: "销售目标"
    }
})