/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.DeptNotTreeStore',{
    extend : 'Ext.data.Store',
    model : 'user.model.DeptModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'dept/commonList'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
})