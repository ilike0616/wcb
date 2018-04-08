/**
 * Created by shqv on 2014-9-13.
 */

Ext.define('user.store.DataDictStore', {
    extend: 'Ext.data.Store',
    model:'user.model.DataDictModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        api:{
            read: 'dataDict/list',
            remove: 'dataDict/delete',
            update: "dataDict/update",
            insert: "dataDict/insert",
            save: 'dataDict/save'
        }
    },
    autoSync:true,
    autoLoad:true,
    folderSort : true
});