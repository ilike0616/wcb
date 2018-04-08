/**
 * Created by guozhen on 2014/6/19.
 * 查询所有的员工并且分页，如：选择审核员工的时候
 */
Ext.define('user.store.EmployeePagingStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('employee'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'employee/listPaging'
        }
    },
    autoLoad:true
});