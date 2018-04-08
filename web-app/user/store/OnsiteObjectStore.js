/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.OnsiteObjectStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("onsite_object"),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'onsiteObject/list',
            remove: 'onsiteObject/delete',
            update: "onsiteObject/update",
            insert: "onsiteObject/insert",
            save: 'onsiteObject/save'
        }
    },
    autoLoad:true
});