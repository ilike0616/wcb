/**
 * Created by like on 2015/6/16.
 */
Ext.define('user.store.workBench.LatestBulletinStore',{
    extend : 'Ext.data.Store',
    model:modelFactory.getModelByModuleId('bulletin'),
    pageSize:10,
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
    autoLoad:false,
    sorters:[{
        property : 'dateCreated',
        direction: 'DESC'
    }]
});