/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.controller.SaleChanceController',{
    extend : 'Ext.app.Controller',
    views:['saleChance.Main','saleChance.List','saleChance.Edit','saleChance.View','saleChance.Close',
            'saleChance.LocusMap'] ,
    stores:['SaleChanceStore'],
    models:['SaleChanceModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [{
        ref : 'saleChanceLocusMap',
        selector : 'saleChanceLocusMap'
    }],
    init:function(){
        this.application.getController("SaleChanceFollowController");
        this.control({
            'saleChanceMain saleChanceList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.getXType()=='gridview') {
                        grid = grid.up('grid');
                    }
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('saleChanceMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid);
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = selectModel.view.up('saleChanceMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{id:'saleChance.id',value:record.get('id')},{id:'saleChance.subject',value:record.get('subject')}];

                    if(tabpanel.hidden==false){
                        var follow = tabpanel.down('saleChanceFollowList');
                        if(follow){
                            var store = follow.getStore();
                            Ext.apply(store.proxy.extraParams,{saleChance:record.get('id')});
                            follow.initValues = [
                                {id:'saleChance.id',value:record.get('id')},{id:'saleChance.subject',value:record.get('subject')},
                                {id:'customer.id',value:record.get('customer.id')},{id:'customer.name',value:record.get('customer.name')},
                                {id:'contact.id',value:record.get('contact.id')},{id:'contact.name',value:record.get('contact.name')}
                            ];
                            store.load(function(records){
                                follow.setTitle(follow.title1+"("+records.length+")");
                            });
                            var sfa = tabpanel.down('sfaExecuteMain');
                            if(sfa.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                                sfa = sfa.ownerCt;
                            }
                            if(sfa){
                                var store = sfa.down('sfaExecuteSfaList').getStore();
                                Ext.apply(store.proxy.extraParams,{moduleId:'sale_chance',linkId:record.get('id')});
                                sfa.initValues = [{id:'moduleId',value:'sale_chance'},{id:'linkId',value:record.get('id')},{id:'linkName',value:record.get('subject')}];
                                store.load(function(records,operation, success){
                                    sfa.setTitle(sfa.title1+"("+this.getTotalCount()+")");
                                });
                                sfa.down('sfaExecuteEventList').getStore().removeAll();
                                sfa.down('sfaExecuteEventExecuteList').getStore().removeAll();
                            }
                        }
                    }
                }
            },
            'saleChanceMain saleChanceList dataview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    if(e.getKey()==e.Q||e.getKey()==e.TAB){
                        var tab = view.up('saleChanceMain').down('tabpanel');
                        if(tab.hidden==false){
                            tab.setActiveTab(1);
                            var activeIndex = tab.activeIndex+1;
                            if(tab.items.getCount()==activeIndex){
                                activeIndex = 0;
                            }
                            tab.activeIndex = activeIndex;
                            tab.setActiveTab(activeIndex);
                        }else{
                            tab.activeIndex = 0;
                            tab.setActiveTab(0);
                            tab.show();
                        }
                    }else if(e.getKey()==e.ESC){
                        view.up('saleChanceMain').down('tabpanel').hide();
                    }
                }
            },
            'saleChanceList button[operationId=sale_chance_update]' :{
                click:function(btn){
                    var record = btn.up('saleChanceList').getSelectionModel().getSelection()[0];
                    var competitorsId = [],competitorsName = [];
                    Ext.each(record.get('competitors'),function(item){
                        competitorsId.push(item.id);
                        competitorsName.push(item.name);
                    })
                    if(competitorsId.length > 0){
                        competitorsId.join(",");
                        competitorsName.join(",");
                    }
                    var view = Ext.widget('saleChanceEdit').show();
                    view.down('form').loadRecord(record);
                    view.down('hiddenfield[name=competitors]').setValue(competitorsId);
                    view.down('baseMultiSelectTextareaField textareafield[name=competitors.name]').setValue(competitorsName);
                }
            },
            'saleChanceList button[operationId=sale_chance_view]' :{
                click:function(btn){
                    var record = btn.up('saleChanceList').getSelectionModel().getSelection()[0];
                    var competitorsId = [],competitorsName = [];
                    Ext.each(record.get('competitors'),function(item){
                        competitorsId.push(item.id);
                        competitorsName.push(item.name);
                    })
                    if(competitorsId.length > 0){
                        competitorsId.join(",");
                        competitorsName.join(",");
                    }
                    var view = Ext.widget('saleChanceView').show();
                    view.down('form').loadRecord(record);
                    view.down('textareafield[name=competitors.name]').setValue(competitorsName);
                }
            },
            'saleChanceList button[operationId=sale_chance_close]' : {
                click: function (btn) {
                    var grid = btn.up('saleChanceList')
                    var record = grid.getSelectionModel().getSelection()[0];
                    if(record.get('isClosed') == false){ // 未关闭
                        var view = Ext.widget('saleChanceClose',{
                            listDom:grid
                        }).show();
                        view.down('form').loadRecord(record);
                    }else{
                        Ext.Msg.show({
                            title:'提示',
                            msg: '该商机已关闭！',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.ERROR
                        });
                    }
                }
            },
            'saleChanceClose button#save' : {
                click: function (btn) {
                    var win = btn.up('window');
                    var grid = win.listDom;
                    var form = win.down('form');
                    var store = grid.getStore();
                    if (!form.getForm().isValid()) return;
                    form.submit({
                        waitMsg: '正在提交数据',
                        waitTitle: '提示',
                        url:'saleChance/close',
                        method: 'POST',
                        params: {moduleId:form.moduleId},
                        submitEmptyText : false,
                        success: function(response, opts) {
                            store.load();
                            grid.getSelectionModel().deselectAll();
                            Ext.example.msg('提示', '关闭成功');
                            if(win){
                                win.close();
                            }
                        },
                        failure:function(form,action){
                            var result = Util.Util.decodeJSON(action.response.responseText);
                            Ext.example.msg('提示', result.msg);
                        }
                    });
                }
            },
            'saleChanceList button[operationId=sale_chance_follow]' :{
                click:function(btn){
                    var record = btn.up('saleChanceList').getSelectionModel().getSelection()[0];
                    var view = Ext.widget('saleChanceFollowAdd').show();
                    view.down('baseSpecialTextfield[name=customer.name]').down('combo').setValue(record.get('customer.name'));
                    view.down('baseSpecialTextfield[name=customer.name]').down('hiddenfield').setValue(record.get('customer.id'));
                    view.down('baseSpecialTextfield[name=contact.name]').down('combo').setValue(record.get('contact.name'));
                    view.down('baseSpecialTextfield[name=contact.name]').down('hiddenfield').setValue(record.get('contact.id'));
                    view.down('baseSpecialTextfield[name=saleChance.subject]').down('combo').setValue(record.get('subject'));
                    view.down('baseSpecialTextfield[name=saleChance.subject]').down('hiddenfield').setValue(record.get('id'));
                }
            },
            'baseWinForm[moduleId=sale_chance][optType=add] spinnerfield[name=foreseeMoney]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.calculateDiscountRate(o,1);
                }
            },
            'baseWinForm[moduleId=sale_chance][optType=add] spinnerfield[name=discountMoney]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.calculateDiscountRate(o,2);
                }
            },
            'baseWinForm[moduleId=sale_chance][optType=update] spinnerfield[name=foreseeMoney]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.calculateDiscountRate(o,1);
                }
            },
            'baseWinForm[moduleId=sale_chance][optType=update] spinnerfield[name=discountMoney]':{
                change:function(o, newValue, oldValue, eOpts){
                    this.calculateDiscountRate(o,2);
                }
            },
            'saleChanceLocusMap':{
                afterrender : function(component, eOpts) {
                    var store = component.listDom.getStore();
                    var dataObj = [];
                    Ext.Ajax.request({
                        url : store.getProxy().api['map'],
                        params : {},
                        method : 'POST',
                        timeout : 4000,
                        async : false,
                        success : function(response, opts) {
                            var rt = Ext.JSON.decode(response.responseText);
                            dataObj = rt.data;
                        },
                        failure : function(response, opts) {
                            var errorCode = "";
                            if (response.status) {
                                errorCode = 'error:' + response.status;
                            }
                            Ext.example.msg('提示', '操作失败！' + errorCode);
                            success = false;
                        }
                    });
                    this.getSaleChanceLocusMap().initPathMap(component.el.dom, dataObj);
                }
            }
        });
    },
    /**
     * 计算折扣率
     * @param o
     * @param flag
     */
    calculateDiscountRate : function(o,flag){
        var win = o.up('window');
        var foreseeMoneyObj = win.down('spinnerfield[name=foreseeMoney]');
        var discountMoneyObj = win.down('spinnerfield[name=discountMoney]');
        var discountRateObj = win.down('textfield[name=discountRate]');
        if(Ext.typeOf(foreseeMoneyObj) != 'undefined' && Ext.typeOf(discountMoneyObj) != 'undefined' && Ext.typeOf(discountRateObj) != 'undefined'){
            // 折扣率 = 实际金额/预计金额
            discountRateObj.setValue(discountMoneyObj.getValue()/foreseeMoneyObj.getValue());
        }
    }
})
