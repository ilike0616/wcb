/**
 * Created by like on 2015/9/18.
 */
Ext.define('user.store.SfaEventExecuteStore',{
    extend : 'Ext.data.Store',
    model:'user.model.SfaEventExecuteModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'sfaExecute/eventExecuteList'
        }
    },
    autoLoad:true
});