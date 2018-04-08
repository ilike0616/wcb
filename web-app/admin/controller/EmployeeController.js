/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('admin.controller.EmployeeController',{
    extend : 'Ext.app.Controller',
    views:['employee.Main','employee.List','employee.Add','employee.Edit','privilege.List','privilege.Add','employee.BindEmpPrivilege'
        ,'employee.EmployeeModifyPwd'] ,
    stores:['EmployeeStore','EmployeeTreeStore','PrivilegeStore'],
    models:['EmployeeModel','PrivilegeModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'employeeMain employeeList' : {
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    var tabpanel = grid.up('employeeMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid);
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = eOpts.up('employeeMain').down('tabpanel');
                    eOpts.up('baseList').initValues = [{id:'user',value:record.get('user')}];
                    if(tabpanel.hidden==false){
                        var privilege = tabpanel.down('privilegeList');
                        if(privilege){
                            var store = privilege.getStore();
                            Ext.apply(store.proxy.extraParams,{userId:record.get('user'),employeeId:record.get('id')});
                            store.load(function(records,operation, success){
                                privilege.setTitle(privilege.title1+"("+this.getTotalCount()+")");
                            });
                        }
                    }
                }
            },
            // 按ESC取消视图
            'employeeMain employeeList dataview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    if(e.getKey()==e.ESC){
                        view.up('employeeMain').down('tabpanel').hide();
                    }
                }
            },
            'employeeList button#addButton':{
                click:function(btn){
                    var userMain = btn.up('userMain');
                    var view = Ext.widget('employeeAdd',{listDom:btn.up('grid')});
                    view.show();
                    if(userMain != null && userMain != 'undefined'){
                        var grid = userMain.down('userList');
                        var record = grid.getSelectionModel().getSelection()[0];
                        view.down('form combobox[name=user]').setValue(record.get('id'));
                    }

                }
            },
            // 修改
            'employeeList button[itemId=updateButton]':{
                click:function(btn){
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('employeeEdit');
                    view.down('form').loadRecord(record);
                    view.show();
                    view.down('baseSpecialTextfieldTree[name=parentEmployee.name]').down('textfield').setValue(record.get('parentEmployeeName'));
                    view.down('baseSpecialTextfieldTree[name=parentEmployee.name]').down('hiddenfield').setValue(record.get('parentEmployee'));
                    view.down('baseSpecialTextfieldTree[name=dept.name]').down('textfield').setValue(record.get('deptName'));
                    view.down('baseSpecialTextfieldTree[name=dept.name]').down('hiddenfield').setValue(record.get('dept'));
                }
            },
            // 新增
            'employeeMain privilegeList button#addButton':{
                click:function(btn){
                    var main = btn.up('employeeMain');
                    var employeeList = main.down('employeeList');
                    var record = employeeList.getSelectionModel().getSelection()[0];
                    if(!record){
                        Ext.Msg.alert("提示","请先选择员工！");
                        return;
                    }else{
                        var userId = record.get("user");
                        var view = Ext.widget('privilegeAdd');
                        view.down('hiddenfield[name=user]').setValue(userId);
                        view.show();
                    }
                }
            },
            // 绑定用户按钮
            'employeeList button#bindPrivilegeButton':{
                click:function(btn){
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('bindEmpPrivilege',{listDom:grid,rec:record}).show();
                    view.down('form').loadRecord(record);
                }
            },
            'employeeModifyPwd button#modify_password_button':{
                click:function(btn){
                    var gridDoActionUtil = this.GridDoActionUtil;
                    var view = btn.up('employeeModifyPwd');
                    if (!view.down('form').getForm().isValid()) return;
                    var store = view.listDom.getStore();
                    var oldPwd = view.down('textfield[name=oldPassword]').getValue();
                    var newPwd = view.down('textfield[name=newPassword]').getValue();
                    var employeeId = view.down('hiddenfield[name=id]').getValue();
                    Ext.MessageBox.confirm('确定修改密码？',oldPwd, function(button) {
                        if (button == 'yes') {
                            var succ = gridDoActionUtil.doAjax('employee/modifyPassword',{employeeId:employeeId,oldPassword:oldPwd,newPassword:newPwd},store,false);
                            if(succ == true){
                                view.close();
                            }
                        }
                    })
                }
            }
        });
    }
})
