/**
 * Created by like on 2015/8/18.
 */
Ext.define('user.store.MyTaskStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('my_task'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'myTask/list',
            comment: 'myTask/comment'
        }
    },
    autoLoad:false
})