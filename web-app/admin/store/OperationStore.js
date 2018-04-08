/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define('admin.store.OperationStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.OperationModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'operation/list',
            remove: 'operation/delete',
            update: "operation/update",
            insert: "operation/insert",
            save: 'operation/save'
        }
    },
    autoLoad:true
});