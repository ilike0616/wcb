Ext.define('user.controller.AuditController', {
    extend: 'Ext.app.Controller',
    views: ['audit.Main', 'audit.List', 'audit.Edit', 'auditOpinion.Edit', 'auditOpinion.List', 'auditOpinion.WinList',
        'auditOpinion.ViewApply','auditOpinion.ViewApplyWin'],
    stores: ['AuditStore'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: 'auditList',
            selector: 'auditList'
        }
    ],
    init: function () {
        this.application.getController("FareClaimsController");
        this.control({
            'auditMain auditList': {
                render: function (grid) {
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams, {auditType: grid.auditType}); //附加待审、已审、发出的参数
                    store.load();
                }
            },
            //“查看申请单”
            'auditList button[operationId=work_audit_view_apply]': {
                click: function (btn) {
                    var record = btn.up('auditList').getSelectionModel().getSelection()[0];
                    var qz = record.get('qz'); //审核类型的标记
                    var ctrl = this.switchQz(qz);
                    var view = Ext.widget('auditOpinionViewApplyWin', {viewId: ctrl + 'View'});
                    view.show();
                    //申请单信息
                    var apply = view.down('auditOpinionViewApply');
                    if (qz) {
                        Ext.Ajax.request({
                            url: 'audit/viewQz',
                            params: {id: record.get('id')},
                            method: 'POST',
                            timeout: 20000,
                            async: false,
                            success: function (response, opts) {
                                var d = Ext.JSON.decode(response.responseText);
                                if (d.success) {
                                    apply.form.setValues(d.data[0]);
                                }
                            }
                        });
                    }
                    //加载明细
                    if(view.down('grid')){
                        var store = view.down('grid').getStore();
                        var id = "0";
                        if(view.down('form').down("field[name=id]")){
                            id = view.down('form').down("field[name=id]").value;
                        }
                        Ext.apply(store.proxy.extraParams,{object_id:id});
                        store.load();
                    }
                }
            },
            'auditMain auditList button[operationId=work_audit_audit]': {        //点击审核按钮
                click: function (btn) {
                    var record = btn.up('auditList').getSelectionModel().getSelection()[0];
                    var qz = record.get('qz'); //审核类型的标记
                    var ctrl = this.switchQz(qz);

                    var view = Ext.widget('auditEdit', {viewId: ctrl + 'View'})
                    view.show();
                    //审核意见
                    view.getComponent('auditOpinionEdit').down('form').form.setValues(record.get('nowAuditOpinion'));
                    //已审核意见列表
                    var opinionStore = view.getComponent('auditOpinionList').getStore();
                    Ext.apply(opinionStore.proxy.extraParams, {audit: record.get('id')})
                    opinionStore.load();
                    //申请单信息
                    var apply = view.down('auditOpinionViewApply');
                    if (qz) {
                        Ext.Ajax.request({
                            url: 'audit/viewQz',
                            params: {id: record.get('id')},
                            method: 'POST',
                            timeout: 20000,
                            async: false,
                            success: function (response, opts) {
                                var d = Ext.JSON.decode(response.responseText);
                                if (d.success) {
                                    apply.form.setValues(d.data[0]);
                                }
                            }
                        });
                    }
                }
            },
            'auditOpinionWinList auditOpinionList': {        //加载审核意见列表
                render: function (grid) {
                    var audit = grid.up("auditOpinionWinList").listDom.getSelectionModel().getSelection()[0];
                    var store = grid.getStore();
                    if (grid.up("auditOpinionWinList").formType) {
                        Ext.apply(store.proxy.extraParams, {audit: audit.get('audit').id});
                    } else {
                        Ext.apply(store.proxy.extraParams, {audit: audit.get('id')});
                    }

                    store.load();
                }
            },
            'auditList button[operationId=work_audit_view]': {   //点击“审核意见”查看
                click: function (btn) {
                    var v = Ext.widget('auditOpinionWinList', {listDom: btn.up('baseList')});
                    v.show();
                }
            },
            'auditEdit auditOpinionEdit combo[name=state]': {
                change: function (me, newValue, oldValue, eOpts) {
                    var auditor = me.up('auditOpinionEdit').down('baseSpecialTextfield[name=nextAuditor.name]');
                    if (newValue == 3) {
                        auditor.setDisabled(false);
                    } else {
                        auditor.setDisabled(true);
                    }
                }
            },
            'auditEdit button[itemId=save]': {
                click: function (btn) {
                    var grid = Ext.ComponentQuery.query(btn.target)[0];
                    Ext.Array.each(Ext.ComponentQuery.query(btn.target),function(com, index, countriesItSelf){
                        if(com.auditType == 1){
                            grid = com;
                        }
                    });
                    this.GridDoActionUtil.doUpdate(grid, btn.up('window').down('auditOpinionEdit').down('form'), btn.up('window'));
                }
            }
        });
    },
    'switchQz':function(qz){
        var ctrl = "";
        switch (qz) {         //区分审核类型
            case 'goout':
                ctrl = 'GoOutApply';
                break;
            case 'trip':
                ctrl = 'BusinessTripApply';
                break;
            case 'leave':
                ctrl = 'LeaveApply';
                break;
            case 'overtime':
                ctrl = 'OvertimeApply';
                break;
            case 'fareClaims':
                ctrl = 'FareClaims';
                break;
            case 'income':
                ctrl = 'FinanceIncome';
                break;
            case 'expense':
                ctrl = 'FinanceExpense';
                break;
        }
        return ctrl;
    }
})