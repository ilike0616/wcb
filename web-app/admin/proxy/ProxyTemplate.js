/**
 * Created by qiaoxu on 15/5/14.
 */
Ext.define('admin.proxy.ProxyTemplate',{
    extend : 'Ext.data.proxy.Ajax',
    alias : 'proxy.proxyTemplate',
    type:'ajax',
    reader:{
        type:'json',
        root:'data',
        successProperty:'success',
        totalProperty:'total'
    },
    writer:{
        type:'json'
    },
    simpleSortMode: true,
    listeners: {
        exception: function(proxy,  response, operation, eOpts){
            if(response.status==403) {
                Ext.Msg.show({
                    title: 'ERROR',
                    msg: "无操作权限",
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.Msg.OK
                });
            }
        }
    }
});