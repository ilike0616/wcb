/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.store.BulletinStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("bulletin"),
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'bulletin/list',
            remove: 'bulletin/delete',
            update: "bulletin/update",
            insert: "bulletin/insert",
            save: 'bulletin/save'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
})