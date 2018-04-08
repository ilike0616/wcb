/**
 * Created by like on 2015-04-15.
 */
Ext.define('user.store.NotifyModelFilterStore', {
    extend : 'Ext.data.TreeStore',
    model: 'user.model.NotifyModelFilterModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy: {
        type: 'proxyTemplate',
        api: {
            read: 'notifyModelFilter/list',
            remove: 'notifyModelFilter/delete',
            update: "notifyModelFilter/update",
            insert: "notifyModelFilter/insert",
            save: 'notifyModelFilter/save'
        },
        reader: {
            type: 'json',
            successProperty: 'success'
        }
    },
    autoLoad: false,
    folderSort : true,
    root: {
        expanded: true,
        name: "条件组"
    }
});