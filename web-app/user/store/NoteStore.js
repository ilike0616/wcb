/**
 * Created by like on 2015/3/19.
 */
Ext.define('user.store.NoteStore',{
    extend : 'Ext.data.Store',
    model : 'user.model.NoteModel',
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'note/list',
            remove: 'note/delete',
            update: "note/update",
            insert: "note/insert",
            save: 'note/save',
            comment: "note/comment",
            zs: 'note/zs'
        }
    },
    autoLoad:false
});