/**
 * Created by guozhen on 2015/09/16.
 */
Ext.define('user.store.ShareDetailStore',{
    extend : 'Ext.data.Store',
    model:'user.model.ShareDetailModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'share/detailList'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
})