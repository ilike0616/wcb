/**
 * Created by shqv on 2014-9-13.
 */

Ext.define('user.store.DataDictItemStore', {
    extend: 'Ext.data.Store',
    model:'admin.model.DataDictItemModel',
    /*    autoSync: true,//需要同步    */
    pageSize:-1,  //每页显示的记录行数
    sortInfo: { field: "seq", direction: "ASC" },
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy:{
        type:'proxyTemplate',
        url: 'dataDictItem/list',
        api:{
            read: 'dataDictItem/list',
            remove: 'dataDictItem/delete',
            update: "dataDictItem/update",
            insert: "dataDictItem/insert",
            save: 'dataDictItem/save'
        }
    },
    autoLoad:false,
    folderSort : true,
    sorters:[{
        property : 'seq',
        direction: 'ASC'
    }]
});