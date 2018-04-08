/**
 * Created by like on 2015/8/20.
 */
Ext.define('admin.store.AssociatedRequiredStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.AssociatedRequiredModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'associatedRequired/list',
            remove: 'associatedRequired/delete',
            update: "associatedRequired/update",
            insert: "associatedRequired/insert",
            save: 'associatedRequired/save'
        }
    },
    autoSync:true,
    autoLoad:false
});