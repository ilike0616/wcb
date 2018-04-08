/**
 * Created by like on 2015-04-15.
 */
Ext.define('user.store.NotifyModelFilterForEditStore', {
    extend: 'Ext.data.TreeStore',
    fields : ['name','id','leaf'],
    root: {
        expanded: true,
        text: "条件组"
    },
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'notifyModelFilter/treeForEdit'
        },
        reader:'json'
    }
})
