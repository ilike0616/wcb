/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.LoginLogStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("login_log"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'loginLog/list'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
});