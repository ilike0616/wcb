/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("admin.store.UserOptConditionStore",{
    extend : 'Ext.data.Store',
    model:'admin.model.UserOptConditionModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userOptCondition/list',
            insert: 'userOptCondition/insert',
            update: 'userOptCondition/update',
            remove: 'userOptCondition/delete'
        }
    },
    autoLoad:true
})
