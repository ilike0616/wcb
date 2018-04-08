/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.AddBalanceStore',{
    extend : 'Ext.data.Store',
    model:'user.model.AddBalanceModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'addBalance/list?fromWhere=user'
        }
    },
    autoLoad:true
})