/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('agent.store.AddBalanceStore',{
    extend : 'Ext.data.Store',
    model:'agent.model.AddBalanceModel',
    proxy:{
        type:'ajax',
        api:{
            read: 'addBalance/list?fromWhere=agent'
        },
        reader:{
            type:'json',
            root:'addBalances',
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