/**
 * Created by shqv on 2014-6-17.
 */
Ext.define("admin.controller.PrivilegeController",{
    extend:'Ext.app.Controller',
    views:['privilege.Main','privilege.List','privilege.Add','privilege.Edit','dept.DeptUserList'] ,
    stores:['PrivilegeStore','UserStore'],
    models:['PrivilegeModel','UserModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
    ],
    init:function(){
        this.control({
            'privilegeMain deptUserList':{
                beforerender:function(grid,eOpts){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams,{limit:9999999});
                    store.load();
                },
                select : function(o, record, index, eOpts){
                    var privilegeMain = eOpts.up('privilegeMain');
                    var privilegeStore = privilegeMain.down('privilegeList').getStore();
                    Ext.apply(privilegeStore.proxy.extraParams,{userId:record.get('id')});
                    privilegeStore.load();
                }
            },
            'privilegeList button#addButton':{
                click:function(btn){
                    var grid = btn.up('privilegeList');
                    var main;
                    if(btn.up('privilegeMain')){
                        main = btn.up('privilegeMain');
                    }else{
                        main = grid.up('deptMain');
                    }
                    var userRecord = main.down('deptUserList').getSelectionModel().getSelection()[0];
                    if(!userRecord){
                        Ext.Msg.alert("提示","请先选择用户！");
                        return;
                    }else{
                        var userId = userRecord.get("id");
                        var view = Ext.widget('privilegeAdd');
                        view.down('hiddenfield[name=user]').setValue(userId);
                        view.show();
                    }
                }
            }
        });
    }
});