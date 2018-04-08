/**
 * Created by guozhen on 2014-07-04.
 */
Ext.define('admin.store.ViewDetailCondStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.ViewDetailCondModel',
    pageSize:15,  //每页显示的记录行数
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
        }
    },
    autoLoad:false
});
