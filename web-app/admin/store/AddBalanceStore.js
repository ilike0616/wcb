/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.AddBalanceStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.AddBalanceModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'addBalance/list'
        }
    },
    autoLoad:true
})