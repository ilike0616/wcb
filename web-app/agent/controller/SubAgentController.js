/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('agent.controller.SubAgentController',{
    extend : 'Ext.app.Controller',
    views:['subAgent.List','subAgent.Add','subAgent.Edit'] ,
    stores:['SubAgentStore'],
    models:['SubAgentModel'],
    GridDoActionUtil:Ext.create("agent.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'subAgentList button#initPasswordButton':{
                click:function(btn){
                    //得到数据集合
                    var grid = btn.up('subAgentList');
                    var store = grid.getStore();
                    var records = grid.getSelectionModel().getSelection();
                    var data = [];
                    var msg = "";
                    Ext.Array.each(records,function(model){
                        data.push(Ext.JSON.encode(model.get('id')));
                        msg = msg +'\n'+model.get('name');
                    });
                    Ext.MessageBox.confirm('确定初始化密码？', msg, function(button){
                        if(button=='yes'){
                            if(data.length > 0){
                                Ext.Ajax.request({
                                    url:'subAgent/initPassword',
                                    params:{ids:"["+data.join(",")+"]"},
                                    method:'POST',
                                    timeout:4000,
                                    success:function(response,opts){
                                        var d = Ext.JSON.decode(response.responseText);
                                        store.load();
                                        grid.getSelectionModel().deselectAll();
                                        btn.setDisabled(true);
                                        if(d.success){
                                            Ext.example.msg('提示', '初始化成功');
                                        }else{
                                            Ext.example.msg('提示', '初始化失败！');
                                        }
                                    },failure:function(response, opts){
                                        var errorCode = "";
                                        if(response.status){
                                            errorCode = 'error:'+response.status;
                                        }
                                        Ext.example.msg('提示', '初始化失败！'+errorCode);
                                    }
                                });
                            }
                        }
                    });
                }
            },
            'subAgentList button#addBalanceButton': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('addBalance',{
                        name:record.get('name'),
                        accountId:record.get('agentId'),
                        objectId:record.get('id'),
                        kind : 3, // 代理商为代理商充值
                        listDom:grid
                    });
                    view.show();
                }
            },
            'subAgentList button#reduceBalanceButton': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('reduceBalance',{
                        name:record.get('name'),
                        accountId:record.get('agentId'),
                        objectId:record.get('id'),
                        availableBalance:record.get('balance'),
                        kind : 3, // 代理商为代理商充值
                        listDom:grid
                    });
                    view.show();
                }
            }
        });
    }
})
