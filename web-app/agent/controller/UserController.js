Ext.define("agent.controller.UserController",{
    extend:'Ext.app.Controller',
    views:['user.List','user.Add','user.Edit'] ,
    stores:['UserStore'],
    models:['UserModel'],
    GridDoActionUtil:Ext.create("agent.util.GridDoActionUtil"),
    refs: [
    ],
    init:function(){
        this.control({
            'userList button#addBalanceButton': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('addBalance',{
                        name:record.get('name'),
                        accountId:record.get('userId'),
                        objectId:record.get('id'),
                        kind : 4, // 管理员为代理商充值
                        listDom:grid
                    });
                    view.show();
                }
            },
            'userList button#reduceBalanceButton': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('reduceBalance',{
                        name:record.get('name'),
                        accountId:record.get('userId'),
                        objectId:record.get('id'),
                        availableBalance:record.get('balance'),
                        kind : 4, // 管理员为代理商充值
                        listDom:grid
                    });
                    view.show();
                }
            }
        });
    }
});