Ext.define('user.store.UserFieldStore', {
    extend: 'Ext.data.Store',
    model:'user.model.UserFieldModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    proxy:{
        type:'ajax',
        api:{
            read: 'userField/listForTemplate'
        },
        reader:{
            type:'json',
            root:'data',
            successProperty:'success',
            totalProperty:'total'
        },
        writer:{
            type:'json'
        },
        simpleSortMode: true
    },
    autoSync:true,
    autoLoad:false
});