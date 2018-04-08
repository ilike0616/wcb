/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.store.UserPortalPureStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.UserPortalModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'userPortal/pureList'
        }
    },
    autoLoad:true,
    sorters:[{
        property : 'idx',
        direction: 'ASC'
    }]
});