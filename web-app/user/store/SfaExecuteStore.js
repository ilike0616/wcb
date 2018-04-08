/**
 * Created by like on 2015/8/27.
 */
Ext.define('user.store.SfaExecuteStore',{
    extend : 'Ext.data.Store',
    model:'user.model.SfaExecuteModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'sfaExecute/list'
        }
    },
    autoLoad:true
});