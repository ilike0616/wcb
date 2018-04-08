/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.store.EmployeePortalStore',{
    extend : 'Ext.data.Store',
    model:'user.model.EmployeePortalModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userPortal/searchAvailableUserPortal'
        }
    },
    autoLoad:true,
    sorters:[{
        property : 'idx',
        direction: 'ASC'
    }]
});