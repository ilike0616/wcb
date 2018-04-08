/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('agent.store.SubAgentStore',{
    extend : 'Ext.data.Store',
    model:'agent.model.SubAgentModel',
    proxy:{
        type:'ajax',
        api:{
            read: 'subAgent/list?fromWhere=agent',
            remove: 'subAgent/delete',
            update: "subAgent/update",
            insert: "subAgent/insert?fromWhere=agent",
            save: 'subAgent/save'
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
})