/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.controller.EmployeeNotifyModelController', {
    extend: 'Ext.app.Controller',
    views: ['employeeNotifyModel.Main', 'module.List','employeeNotifyModel.List',
            'employeeNotifyModel.ForbidList'],
    stores: ['EmployeeNotifyModelStore','EmployeeNotifyModelForbidStore'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: 'moduleList',
            selector: 'employeeNotifyModelMain moduleList'
        },
        {
            ref: 'employeeNotifyModelList',
            selector: 'employeeNotifyModelMain employeeNotifyModelList'
        },
        {
            ref: 'employeeNotifyModelForbidList',
            selector: 'employeeNotifyModelMain employeeNotifyModelForbidList'
        }
    ],
    init: function () {
        this.control({
            'employeeNotifyModelMain moduleList': {                     //模板树形结构
                select: function (o, record, index, eOpts) {
                    var store = this.getEmployeeNotifyModelList().getStore();
                    Ext.apply(store.proxy.extraParams, {'moduleId': record.get('moduleId')});        //根据moduleId查询触发模型
                    store.load();
                    var forbidStore = this.getEmployeeNotifyModelForbidList().getStore();
                    Ext.apply(forbidStore.proxy.extraParams, {'moduleId': record.get('moduleId')});        //根据moduleId查询触发模型
                    forbidStore.load();
                },
                deselect:function (o, record, index, eOpts) {
                    Ext.Array.forEach(this.getEmployeeNotifyModelList().query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(true);
                    });
                }
            },
            'employeeNotifyModelMain employeeNotifyModelList button[operationId=employee_notify_model_default]':{
                click:function(btn){
                    var baseList = btn.up('baseList');
                    this.doDefault(baseList,baseList.alertName,btn);
                }
            },
            'baseWinForm[moduleId=employee_notify_model][optType=update]': {
                beforerender: function (form) {
                    var record = this.getEmployeeNotifyModelList().getSelectionModel().getSelection()[0];
                    var moduleId = record.get('notifyModel').module.moduleId;
                    Ext.apply(form.down("field[name='subjectTemplate']"),{moduleId: moduleId});
                    Ext.apply(form.down("field[name='contentTemplate']"),{moduleId: moduleId});
                }
            }
        });
    },
    doDefault:function(grid,name,btn){
        if(!grid){
            alert("参数传递不正确");
            return;
        }
        //得到数据集合
        var store = grid.getStore();
        var records = grid.getSelectionModel().getSelection();
        var data = [];
        var msg = "";
        Ext.Array.each(records,function(model){
            if(model.get('id')){
                data.push(Ext.JSON.encode(model.get('id')));
            }
            msg = msg +'\n'+model.get(name);
        });

        Ext.MessageBox.confirm('确定恢复默认设置？', msg, function(button){
            if(button=='yes'){
                if(data.length > 0){
                    Ext.Ajax.request({
                        url:store.getProxy().api['remove'],
                        params:{ids:"["+data.join(",")+"]"},
                        method:'POST',
                        timeout:4000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            store.load();
                            btn.setDisabled(true);
                            if(d.success){
                                Ext.example.msg('提示', '恢复默认设置成功');
                            }else{
                                Ext.example.msg('提示', '恢复默认设置失败！');
                            }
                        },failure:function(response, opts){
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '恢复默认设置失败！'+errorCode);
                        }
                    });
                }else{
                    Ext.example.msg('提示', '已是默认设置！');
                }
            }
        });
    }
});