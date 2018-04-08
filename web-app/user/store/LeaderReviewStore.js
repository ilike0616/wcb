/**
 * Created by like on 2015/8/13.
 */
Ext.define('user.store.LeaderReviewStore',{
    extend : 'Ext.data.Store',
    model : modelFactory.getModelByModuleId("leader_review"),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'leaderReview/list',
            reply: 'leaderReview/reply',
            toRead: 'leaderReview/toRead'
        }
    },
    remoteSort: true,
    autoSync:true,
    autoLoad:false
});