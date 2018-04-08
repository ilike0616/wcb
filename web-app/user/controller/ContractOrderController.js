Ext.define('user.controller.ContractOrderController', {
    extend: 'Ext.app.Controller',
    views: ['contractOrder.Main','contractOrder.List',
        'contractOrder.DetailList', 'contractOrder.DetailEditList',
        'product.comProductMain', 'product.ProductProductKindList', 'product.List'],
    stores: ['ContractOrderStore', 'ContractOrderDetailStore', 'ProductStore', 'ProductKindStore'],
    GridDoActionUtil: Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: ''
        }
    ],
    init: function () {
        this.application.getController("ProductController");
        this.application.getController('FinanceIncomeController')
        this.control({
            'contractOrderMain contractOrderList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.getXType()=='gridview') {
                        grid = grid.up('grid');
                    }
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('contractOrderMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid.getView());
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = selectModel.view.up('contractOrderMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{id:'contractOrder',value:record.get('id')},{id:'contractOrder.subject',value:record.get('subject')}];

                    if(tabpanel.hidden==false){
                        var detailList = tabpanel.down('contractOrderDetailList');
                        if(detailList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            detailList = detailList.ownerCt;
                        }
                        if(detailList){
                            var store = detailList.getStore();
                            Ext.apply(store.proxy.extraParams,{object_id:record.get('id')});
                            detailList.initValues = [{id:'contractOrder',value:record.get('id')},{id:'contractOrder.subject',value:record.get('subject')}];
                            store.load(function(records){
                                detailList.setTitle(detailList.title1+"("+records.length+")");
                            });
                        }
                        var income = tabpanel.down('financeIncomeList');
                        if(income.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            income = income.ownerCt;
                        }
                        if(income){
                            var store = income.getStore();
                            Ext.apply(store.proxy.extraParams,{contractOrder:record.get('id')});
                            income.initValues = [{id:'contractOrder',value:record.get('id')},{id:'contractOrder.subject',value:record.get('subject')}];
                            store.load(function(records){
                                income.setTitle(income.title1+"("+records.length+")");
                            });
                        }
                        var sfa = tabpanel.down('sfaExecuteMain');
                        if(sfa.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            sfa = sfa.ownerCt;
                        }
                        if(sfa){
                            var store = sfa.down('sfaExecuteSfaList').getStore();
                            Ext.apply(store.proxy.extraParams,{moduleId:'contract_order',linkId:record.get('id')});
                            sfa.initValues = [{id:'moduleId',value:'contract_order'},{id:'linkId',value:record.get('id')},{id:'linkName',value:record.get('subject')}];
                            store.load(function(records,operation, success){
                                sfa.setTitle(sfa.title1+"("+this.getTotalCount()+")");
                            });
                            sfa.down('sfaExecuteEventList').getStore().removeAll();
                            sfa.down('sfaExecuteEventExecuteList').getStore().removeAll();
                        }
                    }
                }
            },
            'baseForm[moduleId=contract_order] field[name=sumMoney]':{
                keyup:function(field, e, eOpts ){
                    var form = field.up('form');
                    var sumMoney = field.getValue();
                    var discount = form.down('field[name=discountRate]').getValue();
                    form.down('field[name=discountMoney]').setValue(Number(discount)*sumMoney/100)
                }
            },
            'baseForm[moduleId=contract_order] field[name=discountRate]':{
                keyup:function(field, e, eOpts ){
                    var form = field.up('form');
                    var sumMoney = form.down('field[name=sumMoney]').getValue();
                    var discountRate = field.getValue()
                    form.down('field[name=discountMoney]').setValue(Number(sumMoney)*Number(discountRate)/100);
                }
            },
            'baseForm[moduleId=contract_order] field[name=discountMoney]':{
                keyup:function(field, e, eOpts ){
                    var form = field.up('form');
                    var sumMoney = form.down('field[name=sumMoney]').getValue();
                    var discountMoney = field.getValue();
                    form.down('field[name=discountRate]').setValue(Number(discountMoney)/Number(sumMoney)*100)
                }
            },
            'baseForm[moduleId=contract_order] baseEditList': {
                afterrender: function (grid, eOpts) {
                    var me = this;
                    grid.mon(grid.store, 'update', function (store, record, operation, eOpts) {
                        me.setFormMoney(store, record, operation, eOpts, grid);
                    });
                }
            },
            'baseForm[moduleId=contract_order] baseEditList button[itemId=add]': {
                click: function (btn) {
                    var v = Ext.widget('comProductMain', {listDom: btn.up('baseEditList')});
                    v.show();
                }
            },
            'baseForm[moduleId=contract_order] baseEditList button[itemId=del]': {
                click: function (btn) {
                    var store = btn.up('baseEditList').getStore();
                    var record = btn.up('baseEditList').getSelectionModel().getSelection()[0];
                    store.remove(record);
                }
            },
            //入账
            'contractOrderList button[operationId=contract_order_finance_income]':{
                click: function(btn){
                    var record = btn.up('baseList').getSelectionModel().getSelection()[0];
                    var targetDom = Ext.ComponentQuery.query('financeIncomeList')[0]
                    var v = Ext.widget(btn.vw,{listDom:btn.up('baseList'),targetDom:targetDom}),
                        form = v.down('form');
                    if(form.down('field[name="subject"]')){
                        form.down('field[name="subject"]').setValue(record.get('subject'));
                    }
                    if(form.down('field[name="contractOrder"]')){
                        form.down('field[name="contractOrder"]').setValue(record.get('id'));
                    }else{
                        form.add({
                            name:'contractOrder',
                            hidden:true,
                            value:record.get('id')
                        });
                    }
                    if(form.down('field[name="contractOrder.subject"]')){
                        form.down('field[name="contractOrder.subject"]').setValue(record.get('subject'));
                    }
                    var money = record.get('discountMoney');
                    var store = Ext.create('user.store.FinanceIncomeStore');
                    Ext.apply(store.proxy.extraParams,{contractOrder:record.get('id')});
                    store.load(function(records){
                        Ext.Array.each(records,function(old){
                            if(old.get('money')){
                                money -= old.get('money');
                            }
                        });
                        if(money <=0){
                            Ext.MessageBox.confirm('提醒', '该订单金额已全部产生入账单，是否继续入账？', function(btn, text){
                                if (btn == 'yes'){
                                    v.show();
                                }
                            });
                        }else{
                            v.show();
                        }
                        if(form.down('field[name="money"]')){
                            form.down('field[name="money"]').setValue(money);
                        }
                    });
                }
            }
        });
    },
    setFormMoney: function (store, record, operation, eOpts, grid) {
        if (eOpts == 'price' || eOpts == 'num') {
            record.set('totalPrice', record.get('price') * record.get('num'));
        }
        if(eOpts == 'totalPrice'){
            var form = grid.up('form');
            form.down('field[name=sumMoney]').setValue(store.getSum(store.getRange(),'totalPrice'))
        }
    }
})