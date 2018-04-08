/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.controller.EmployeeController',{
    extend : 'Ext.app.Controller',
    views:['employee.List','employee.Main','employee.EmployeeDeptList'
        ,'employee.BindEmpPrivilege','employee.EmployeeStructure','employee.EmployeeModifyPwd'] ,
    stores:[],
    models:[],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.application.getController("DeptController");
        this.control({
            'baseWinForm[moduleId=employee][optType=add]':{
              afterrender:function(view){
                  var deptRecord = view.listDom.up('employeeMain').down('employeeDeptList').getSelectionModel().getSelection()[0];
                  if(deptRecord != null){
                      view.down('textfield[name=dept.name]').setValue(deptRecord.get('name'));
                      view.down('hiddenfield[name=dept]').setValue(deptRecord.get('id'));
                  }
              }
            },
            'employeeList button[operationId=employee_password_init]':{
                click:function(btn){
                    var grid = btn.up('baseList')
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var store = grid.getStore();
                    var records = grid.getSelectionModel().getSelection();
                    var data = [];
                    var msg = "";
                    Ext.Array.each(records,function(model){
                        data.push(Ext.JSON.encode(model.get('id')));
                        msg = msg +'\n'+model.get('name');
                    });
                    Ext.MessageBox.confirm('确定初始化密码吗？', msg, function(button){
                        if(button=='yes'){
                            if(data.length > 0){
                                Ext.Ajax.request({
                                    url:store.getProxy().api['initPassword'],
                                    params:{ids:"["+data.join(",")+"]"},
                                    method:'POST',
                                    timeout:4000,
                                    success:function(response,opts){
                                        var d = Ext.JSON.decode(response.responseText);
                                        btn.setDisabled(true);
                                        if(d.success){
                                            Ext.example.msg('提示', '密码初始化成功');
                                            grid.getSelectionModel().deselectAll();
                                        }else{
                                            Ext.example.msg('提示', '密码初始化失败！');
                                        }
                                    },failure:function(response, opts){
                                        var errorCode = "";
                                        if(response.status){
                                            errorCode = 'error:'+response.status;
                                        }
                                        Ext.example.msg('提示', '密码初始化！'+errorCode);
                                    }
                                });
                            }
                        }
                    });
                }
            },
            'employeeModifyPwd button#modify_password_button':{
                click:function(btn){
                    var gridDoActionUtil = this.GridDoActionUtil;
                    var view = btn.up('employeeModifyPwd');
                    if (!view.down('form').getForm().isValid()) return;
                    var oldPwd = view.down('textfield[name=oldPassword]').getValue();
                    var newPwd = view.down('textfield[name=newPassword]').getValue();
                    Ext.MessageBox.confirm('确定修改密码？',oldPwd, function(button) {
                        if (button == 'yes') {
                            var succ = gridDoActionUtil.doAjax('employee/modifyPassword',{oldPassword:oldPwd,newPassword:newPwd},null,false);
                            if(succ == true){
                                Ext.Ajax.request({
                                    url: 'logout',
                                    success: function(conn, response, options, eOpts){
                                        var result = Util.Util.decodeJSON(conn.responseText);
                                        if (result.success) {
                                            Ext.ComponentQuery.query('mainviewport')[0].destroy();
                                            window.location.reload();
                                        } else {
                                            Util.Util.showErrorMsg(result.msg);
                                        }
                                    },
                                    failure: function(conn, response, options, eOpts) {
                                        Util.Util.showErrorMsg(conn.responseText);
                                    }
                                });
                            }
                        }
                    })
                }
            },
            'employeeDeptList':{
                itemclick : function(view, record, item, index, e, eOpts ){
                    var employeeStore = view.up('employeeMain').down('employeeList').getStore();
                    Ext.apply(employeeStore.proxy.extraParams,{deptId:record.get("id")});
                    employeeStore.load();
                }
            }
        });
    }
})
