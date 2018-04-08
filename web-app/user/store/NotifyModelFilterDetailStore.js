/**
 * Created by like on 2015-04-15.
 */
Ext.define('user.store.NotifyModelFilterDetailStore', {
    extend: 'Ext.data.Store',
    model: 'user.model.NotifyModelFilterDetailModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy: {
        type: 'proxyTemplate',
        api: {
            read: 'notifyModelFilterDetail/list',
            remove: 'notifyModelFilterDetail/delete',
            update: "notifyModelFilterDetail/update",
            insert: "notifyModelFilterDetail/insert",
            save: 'notifyModelFilterDetail/save'
        }
    },
    autoLoad: false
});