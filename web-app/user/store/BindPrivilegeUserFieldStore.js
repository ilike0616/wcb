Ext.define('user.store.BindPrivilegeUserFieldStore', {
    extend: 'Ext.data.Store',
    fields : ['text','id','unShow','unEdit'],
    pageSize:-1,  //每页显示的记录行数
    remoteSort: true,
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'privilege/bindPrivilegeViewDetailList',
            save: "privilege/bindViewDetail"
        }
    },
    autoLoad:false,
    sorters:[{
        property : 'orderIndex',
        direction: 'ASC'
    }]
});