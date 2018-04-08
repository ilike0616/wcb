/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define('admin.store.UserOperationStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.UserOperationModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userOperation/list',
            remove: 'userOperation/delete',
            update: "userOperation/update",
            insert: "userOperation/insert",
            save: 'userOperation/save'
        }
    },
    autoLoad:false
});