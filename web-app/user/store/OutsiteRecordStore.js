/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.OutsiteRecordStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("outsite_record"),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'outsiteRecord/list',
            remove: 'outsiteRecord/delete',
            update: "outsiteRecord/update",
            insert: "outsiteRecord/insert",
            save: 'outsiteRecord/save'
        }
    },
    autoLoad:false
});