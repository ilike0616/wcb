/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('agent.store.UserStore', {
    extend: 'Ext.data.Store',
    model:'agent.model.UserModel',
    /*    autoSync: true,//需要同步    */
    pageSize:15,  //每页显示的记录行数
    remoteSort: true,
    proxy:{
        type:'ajax',
        api:{
            read: 'user/list?fromWhere=agent',
            remove: 'user/delete',
            update: "user/update",
            insert: "user/insert?fromWhere=agent",
            save: 'user/save'
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
    autoLoad:true,
    listeners: {
        exception: function(proxy, type, action, options, res){
            Ext.Msg.show({
                title: 'ERROR',
                msg: res.message,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.Msg.OK
            });
        }
    }
});
