/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.ContactStore',{
    extend : 'Ext.data.Store',
//    model:'user.model.ContactModel',
    model : modelFactory.getModelByModuleId("contact"),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'contact/list',
            remove: 'contact/delete',
            update: "contact/update",
            insert: "contact/insert",
            save: 'contact/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
})