Ext.define("admin.controller.UserController", {
    extend: 'Ext.app.Controller',
    views: ['user.Main', 'user.List', 'user.Add', 'user.Edit', 'user.ModuleAssignMain', 'moduleAssignment.List', 'moduleAssignment.EnableOperationList'
        , 'employee.List', 'employee.Add', 'employee.Edit','user.SortUserPortalList','user.SwitchVersion'],
    stores: ['UserStore', 'EmployeeStore', 'DeptStoreForEdit','UserNotUseSysTplStore'],
    models: ['UserModel', 'EmployeeModel'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
    ],
    init: function () {
        this.application.getController('EmployeeController')
        this.control({
            'userMain userList': {
                itemdblclick: function (grid, record, item, index, e, eOpts) {
                    var tabpanel = grid.up('userMain').down('tabpanel');
                    record = grid.getSelectionModel().getSelection()[0];
                    if (tabpanel.hidden) {
                        tabpanel.show();
                        grid.fireEvent('select', grid.getSelectionModel(), record, index, grid);
                        Ext.apply(tabpanel.down('employeeList'),{selectedUser:record.get('id')});
                    } else {
                        tabpanel.hide();
                    }
                },
                select: function (selectModel, record, index, eOpts) {
                    var tabpanel = eOpts.up('userMain').down('tabpanel');
                    eOpts.up('baseList').initValues = [
                        {id: 'user.id', value: record.get('id')},
                        {id: 'user.name', value: record.get('name')}
                    ];
                    if (tabpanel.hidden == false) {
                        var employee = tabpanel.down('employeeList');
                        if (employee) {
                            var store = employee.getStore();
                            Ext.apply(store.proxy.extraParams, {userId: record.get('id')});
                            store.load(function (records, operation, success) {
                                employee.setTitle(employee.title1 + "(" + this.getTotalCount() + ")");
                            });
                            Ext.apply(employee,{selectedUser:record.get('id')});
                        }
                    }
                }
            },
            'userMain userList dataview': {
                itemkeydown: function (view, record, item, index, e, eOpts) {
                    if (e.getKey() == e.ESC) {
                        view.up('userMain').down('tabpanel').hide();
                    }
                }
            },
            'userMain userList button[itemId=moduleAssignButton]': {
                click: function (o, e, eOpts) {
                    var record = o.up('baseList').getSelectionModel().getSelection()[0];
                    var baseList = o.up('baseList');
                    var moduleAssignWin = Ext.widget('userModuleAssignMain', {listDom: baseList});
                    moduleAssignWin.down('hiddenfield[name=id]').setValue(record.get('id'));
                    moduleAssignWin.setTitle(record.get('userId') + "_" + record.get('name'));

                    var moduleList = moduleAssignWin.down('moduleAssignmentList');
                    var moduleStore = moduleList.getStore();
                    Ext.apply(moduleStore.proxy.extraParams, {userId: record.get("id")});
                    moduleStore.load();
                    moduleAssignWin.show();
                }
            },
            'userModuleAssignMain moduleAssignmentList button#saveButton': {
                click: function (btn) {
                    var tree = btn.up("userModuleAssignMain").down("moduleAssignmentList");
                    var records = tree.getView().getChecked();
                    var moduleIds = [];
                    Ext.Array.each(records, function (rec) {
                        moduleIds.push(rec.get('id'));
                    });
                    var userId = btn.up("userModuleAssignMain").down('hiddenfield[name=id]').getValue();
                    var store = tree.getStore();
                    var success = this.GridDoActionUtil.doAjax('user/update', {id: userId, moduleIds: moduleIds}, store, false);
                    if (success) {
                        var listDom = btn.up("userModuleAssignMain").listDom;
                        if (Ext.typeOf(listDom) != 'undefined') {
                            listDom.getStore().load();
                        }
                    }
                }
            },
            'userModuleAssignMain enableOperationList button#enableButton': {
                click: function (btn) {
                    var win = btn.up('userModuleAssignMain');
                    var moduleList = win.down('moduleAssignmentList');
                    var moduleRecord = moduleList.getSelectionModel().getSelection()[0];
                    if (moduleRecord == null || Ext.typeOf(moduleRecord) == 'undefined') {
                        alert("请选择模块！");
                        return;
                    }
                    var operationList = win.down('enableOperationList');
                    var userId = win.down('hiddenfield[name=id]').getValue();
                    this.GridDoActionUtil.doSave(operationList, Object, {userId: userId, moduleId: moduleRecord.get('id')});
                }
            },
            'userList button#addBalanceButton': {
                click: function (btn) {
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    var view = Ext.widget('addBalance', {
                        name: record.get('name'),
                        accountId: record.get('userId'),
                        objectId: record.get('id'),
                        kind: 2, // 管理员为用户充值
                        listDom: grid
                    });
                    view.show();
                }
            },
            'userAdd field[name=edition]': {
                change: function (me, newValue, oldValue, eOpts) {
                    this.calculateMonthlyFee(me.up('form'));
                }
            },
            'userAdd field[name=allowedNum]': {
                change: function (me, newValue, oldValue, eOpts) {
                    this.calculateMonthlyFee(me.up('form'));
                }
            },
            'userEdit field[name=edition]': {
                change: function (me, newValue, oldValue, eOpts) {
                    this.calculateMonthlyFee(me.up('form'));
                }
            },
            'userEdit field[name=allowedNum]': {
                change: function (me, newValue, oldValue, eOpts) {
                    this.calculateMonthlyFee(me.up('form'));
                }
            },
            'userAdd field[name=isTest]': {
                change: function (me, newValue, oldValue, eOpts) {
                    var testDueDate = me.up('form').down('field[name=testDueDate]');
                    var dueDate = me.up('form').down('field[name=dueDate]');
                    if(newValue){   //试用账号
                        testDueDate.show();
                        dueDate.hide()
                    }else{          //正式账号
                        testDueDate.hide()
                        dueDate.show();
                    }
                }
            },
            'userEdit field[name=isTest]': {
                change: function (me, newValue, oldValue, eOpts) {
                    var testDueDate = me.up('form').down('field[name=testDueDate]');
                    var dueDate = me.up('form').down('field[name=dueDate]');
                    if(newValue){   //试用账号
                        testDueDate.show();
                        dueDate.hide()
                    }else{          //正式账号
                        testDueDate.hide()
                        dueDate.show();
                    }
                }
            },
            'userEdit':{
                render:function(me,eOpts){
                    var listDom = me.listDom,form=me.down('form');
                    var record = listDom.getSelectionModel().getSelection()[0]
                    var edition = form.down('field[name=edition]');
                    edition.store.load();
                    edition.setValue(record.get('editionId'));
                    var agent = form.down('field[name=agent]');
                    agent.store.load();
                    agent.setValue(record.get('agent'));
                }
            },
            'userList button#sortUserPortalButton' : {
                click:function(btn){
                    var grid = btn.up('baseList');
                    var record = grid.getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(record) == 'undefined'){
                        Ext.Msg.alert("提示","请选择企业！");
                        return;
                    }
                    Ext.create('Ext.window.Window',{
                        width: 450,
                        height:500,
                        layout : 'fit',
                        autoScroll:true,
                        modal:true,
                        title:'用户Portal排序',
                        items: [{
                            xtype: 'sortUserPortalList',
                            userId: record.get('id')
                        }]
                    }).show();
                }
            },
            'userList button#setDefaultModulesButton' : {
                click:function(btn){
                    var me = this;
                    Ext.MessageBox.confirm('提醒', '此操作将覆盖系统现有的自定义配置文件，请谨慎操作！是否继续？', function(button) {
                        if (button == 'yes') {
                            var grid = btn.up('baseList');
                            var record = grid.getSelectionModel().getSelection()[0];
                            me.GridDoActionUtil.doAjax('user/setDefaultModules',{id:record.get('id')},null,false);
                        }
                    });
                }
            },
            'userList button#enableCustom' : {
                click:function(btn){
                    var me = this;
                    Ext.MessageBox.confirm('提醒', '此操作将会为该用户外创建一份自定义配置，请谨慎操作！是否继续？', function(button) {
                        if (button == 'yes') {
                            var grid = btn.up('baseList');
                            var record = grid.getSelectionModel().getSelection()[0];
                            me.GridDoActionUtil.doAjax('user/enableCustom',{id:record.get('id')},null,false);
                        }
                    });
                }
            },
            'sortUserPortalList grid':{
                select:function(selectModel, record, index, eOpts){
                    if(index==0){
                        eOpts.up('sortUserPortalList').down("button#up").setDisabled(true);
                        eOpts.up('sortUserPortalList').down("button#down").setDisabled(false);
                        return;
                    }
                    if(eOpts.up('grid').getStore().getCount()-1==index){
                        eOpts.up('sortUserPortalList').down("button#up").setDisabled(false);
                        eOpts.up('sortUserPortalList').down("button#down").setDisabled(true);
                        return;
                    }
                    eOpts.up('sortUserPortalList').down("button#up").setDisabled(false);
                    eOpts.up('sortUserPortalList').down("button#down").setDisabled(false);
                }
            },
            'sortUserPortalList button#up':{
                click:function(btn){
                    var grid = btn.up('grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index - 1, rec);
                    store.each(function(record,index){
                        record.set('idx',index+1);
                    });
                    grid.getSelectionModel().select(rec);

                    var mr = store.getModifiedRecords();
                    var sbtn = grid.down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'sortUserPortalList button#down':{
                click:function(btn){
                    var grid = btn.up('grid');
                    var store = grid.getStore();
                    var rec = grid.getSelectionModel().getSelection()[0];
                    var index = store.indexOf(rec);
                    store.removeAt(index);
                    store.insert(index + 1, rec);
                    store.each(function(record,index){
                        record.set('idx',index+1);
                    });
                    grid.getSelectionModel().select(rec);
                    var mr = store.getModifiedRecords();
                    var sbtn = grid.down('button#save');
                    if (mr.length == 0) {//确认修改记录数量
                        sbtn.setDisabled(true);
                        return false;
                    }
                    sbtn.setDisabled(false);
                }
            },
            'sortUserPortalList button#save':{
                click:function(btn){
                    this.GridDoActionUtil.doSave(btn.up('grid'),btn,null,'userPortal/sortIdx');
                }
            }
        });
    },
    'calculateMonthlyFee':function(form){
        var allowedNum = form.down('field[name=allowedNum]').getValue();
        var edition = form.down('field[name=edition]');
        var fee = 0;
        if(edition) {
            if (edition.findRecordByValue(edition.getValue()))
                fee = edition.findRecordByValue(edition.getValue()).get('monthlyFee');  //每人每月多少钱
            if (allowedNum && fee) {
                form.down('field[name=monthlyFee]').setValue(fee * allowedNum);   //设置企业月费用
            } else {
                form.down('field[name=monthlyFee]').setValue(0);
            }
        }
    }
});