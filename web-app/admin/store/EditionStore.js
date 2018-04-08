/**
 * Created by like on 2015-04-29.
 */
Ext.define('admin.store.EditionStore',{
    extend : 'Ext.data.Store',
    model:'admin.model.EditionModel',
    requires:[
        'admin.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'edition/list',
            remove: 'edition/delete',
            update: "edition/update",
            insert: "edition/insert",
            save: 'edition/save'
        }
    },
    remoteSort: true,
    autoLoad:true
});