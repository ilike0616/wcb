/**
 * Created by guozhen on 2014/6/23.
 */

Ext.define('admin.controller.DeptController',{
    extend : 'Ext.app.Controller',
    views:['dept.Main','dept.List','dept.Add','dept.Edit','dept.DeptUserList','employee.List','privilege.List'] ,
    stores:['DeptStore','DeptStoreForEdit','PrivilegeStore','UserStore'],
    models:['DeptModel','UserModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.application.getController("EmployeeController");
        this.application.getController("PrivilegeController");
        this.control({
            'deptMain deptUserList': {
                beforerender:function(grid,eOpts){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams,{limit:9999999});
                    store.load();
                },
                select : function(o, record, index, eOpts){
                    var deptMain = eOpts.up('deptMain');

                    var deptStore = deptMain.down('deptList').getStore();
                    var id = record.get('id');
                    Ext.apply(deptStore.proxy.extraParams,{userId:id});
                    deptStore.load();

                    var employeeStore = deptMain.down('employeeList').getStore();
                    Ext.apply(employeeStore.proxy.extraParams,{userId:record.get('id')});
                    employeeStore.load();
                    Ext.apply(deptMain.down('employeeList'),{selectedUser:record.get('id')});

                    var privilegeStore = deptMain.down('privilegeList').getStore();
                    Ext.apply(privilegeStore.proxy.extraParams,{userId:record.get('id')});
                    privilegeStore.load();
                }
            },
            'deptMain deptList': {
                select : function(o, record, index, eOpts){
                    var deptMain = eOpts.up('deptMain');
                    var employeeStore = deptMain.down('employeeList').getStore();
                    Ext.apply(employeeStore.proxy.extraParams,{userId:record.get('user'),deptId:record.get('id')});
                    employeeStore.load();
                }
            },
            'deptList > treeview': {
                drop: function (node, data, overModel, dropPosition, eOpts) {
                    var store = data.view.store.treeStore;
                    var url = store.proxy.api['save'];
                    var records = data.records[0];
                    var data ={};
                    data["id"] = records.get("id");
                    if(dropPosition == 'append'){
                        data["parentDept"] = overModel.get("id");
                    }else{
                        data["parentDept"] = overModel.get("parentDept");
                    }
                    data["user"] = overModel.get("user");
                    Ext.Ajax.request({
                        url:url,
                        params:{data:Ext.JSON.encode(data)},
                        method:'POST',
                        timeout:4000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            store.load();
                            if(d.success){
                                Ext.example.msg('提示', '保存成功');
                            }else{
                                Ext.example.msg('提示', '保存失败！');
                            }
                        },
                        failure:function(response,opts){
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '删除失败！'+errorCode);
                        }
                    });
                },
                nodedragover : function(targetNode, position, dragData, e, eOpts ){
                    if (targetNode.get('leaf')){
                        targetNode.set('leaf','false');
                    }
                    return true;
                }
            },
            'deptList button#addButton': {
                click: function (btn) {
                    var deptList = btn.up('baseTreeGrid');
                    var userId = "";
                    var parentDept = "";
                    var deptRecord = deptList.getSelectionModel().getSelection()[0];
                    if(!deptRecord){
                        var userRecord = btn.up('deptMain').down('deptUserList').getSelectionModel().getSelection()[0];
                        if(!userRecord){
                            Ext.Msg.alert("提示","请先选择用户！");
                            return;
                        }else{
                            userId = userRecord.get("id");
                        }
                    }else{
                        userId = deptRecord.get("user");
                        parentDept = deptRecord.get("id");
                    }

                    var view = Ext.widget('deptAdd');
                    view.down('hiddenfield[name=user]').setValue(userId);
                    if(parentDept){
                        view.down("hiddenfield[name=parentDept]").setValue(parentDept);
                    }
                    view.show();
                }
            },
            'deptList button#updateButton': {
                click: function (btn) {
                    var deptList = btn.up('baseTreeGrid');
                    var record = deptList.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('deptEdit');
                    view.down('form').loadRecord(record);
                    view.show();
                }
            }
        });
    }
})
