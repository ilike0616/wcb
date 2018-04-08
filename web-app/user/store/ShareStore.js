/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ShareStore',{
    extend : 'Ext.data.Store',
//    model:'user.model.ContactModel',
    model : modelFactory.getModelByModuleId("share"),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'share/list',
            remove: 'share/delete',
            update: "share/update",
            insert: "share/insert"
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
})