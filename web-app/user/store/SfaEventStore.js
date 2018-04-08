/**
 * Created by like on 2015/8/31.
 */
Ext.define('user.store.SfaEventStore',{
    extend : 'Ext.data.Store',
    model:'user.model.SfaEventModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'sfaEvent/list',
            remove: 'sfaEvent/delete',
            update: "sfaEvent/update",
            insert: "sfaEvent/insert",
            save: 'sfaEvent/save'
        }
    },
    autoLoad:true
});