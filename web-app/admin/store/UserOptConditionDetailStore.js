/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("admin.store.UserOptConditionDetailStore",{
    extend : 'Ext.data.Store',
    model:'admin.model.UserOptConditionDetailModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userOptConditionDetail/list',
            insert: 'userOptConditionDetail/insert',
            update: 'userOptConditionDetail/update',
            remove: 'userOptConditionDetail/delete'
        }
    },
    autoLoad:true
})
