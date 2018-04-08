/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.UserFeedbackStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.UserFeedbackModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userFeedback/list',
            insert: "userFeedback/insert"
        }
    },
    autoLoad:true
})