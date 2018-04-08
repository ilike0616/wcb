/**
 * Created by like on 2015-04-07.
 */
Ext.define('user.store.EmployeeLocusStore',{
    extend : 'Ext.data.Store',
    model: 'user.model.EmployeeLocusModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            map: 'employeeLocus/list'
        }
    },
    autoLoad:false
});