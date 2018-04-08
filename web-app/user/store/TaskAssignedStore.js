/**
 * Created by like on 2015-01-22.
 */
Ext.define('user.store.TaskAssignedStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('task_assigned'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'taskAssigned/list',
            remove: 'taskAssigned/delete',
            update: "taskAssigned/update",
            insert: "taskAssigned/insert",
            save: 'taskAssigned/save',
            comment: 'taskAssigned/comment'
        }
    },
    autoLoad:false
})