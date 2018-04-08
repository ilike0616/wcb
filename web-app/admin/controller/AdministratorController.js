/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.controller.AdministratorController',{
    extend : 'Ext.app.Controller',
    views:['administrator.List','administrator.Add','administrator.Edit'] ,
    stores:['AdministratorStore'],
    models:['AdministratorModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'administratorList button#initPasswordButton':{
                click:function(btn){
                    //得到数据集合
                    var grid = btn.up('administratorList');
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
                                    url:'administrator/initPassword',
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
            }
        });
    }
})
