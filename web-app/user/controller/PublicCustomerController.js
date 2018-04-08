/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.PublicCustomerController',{
    extend : 'Ext.app.Controller',
    views:['publicCustomer.Main','publicCustomer.List'
        ,'publicCustomer.Allocation','publicCustomer.ApplyAudit'],
    stores:['PublicCustomerStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
    	this.application.getController("ContactController");
        this.control({
        	'publicCustomerMain publicCustomerList':{
        		itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.getXType()=='gridview') {
                        grid = grid.up('grid');
                    }
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
        			var tabpanel = grid.up('publicCustomerMain').down('tabpanel');
        			if(tabpanel.hidden){
        				tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid.getView());
        			}else{
        				tabpanel.hide();
        			}
        		},
        		select:function(selectModel, record, index, eOpts){
        			var tabpanel = selectModel.view.up('publicCustomerMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];

        			if(tabpanel.hidden==false){
	        			var contact = tabpanel.down('contactList');
                        if(contact.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            contact = contact.ownerCt;
                        }
	        			if(contact){ 
		        			var store = contact.getStore();
		                    Ext.apply(store.proxy.extraParams,{customer:record.get('id')});
                            contact.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];
		                    store.load(function(records,operation, success){
		                    	contact.setTitle(contact.title1+"("+this.getTotalCount()+")");
		                    });
	        			}
                        var sfa = tabpanel.down('sfaExecuteMain');
                        if(sfa.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            sfa = sfa.ownerCt;
                        }
                        if(sfa){
                            var store = sfa.down('sfaExecuteSfaList').getStore();
                            Ext.apply(store.proxy.extraParams,{moduleId:'public_customer',linkId:record.get('id')});
                            sfa.initValues = [{id:'moduleId',value:'public_customer'},{id:'linkId',value:record.get('id')},{id:'linkName',value:record.get('name')}];
                            store.load(function(records,operation, success){
                                sfa.setTitle(sfa.title1+"("+this.getTotalCount()+")");
                            });
                            sfa.down('sfaExecuteEventList').getStore().removeAll();
                            sfa.down('sfaExecuteEventExecuteList').getStore().removeAll();
                        }
        			}
        		}
        	},
        	'publicCustomerMain publicCustomerList dataview':{
        		itemkeydown:function( view, record, item, index, e, eOpts){
        			if(e.getKey()==e.Q||e.getKey()==e.TAB){
        				var tab = view.up('publicCustomerMain').down('tabpanel');
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
        				view.up('publicCustomerMain').down('tabpanel').hide();
        			}
        		}
        	},
            'publicCustomerMain publicCustomerList button[operationId=public_customer_recover]':{   // 客户回收
                click:function(btn) {
                    var me = this;
                    var grid = btn.up('baseList');
                    var records = grid.getSelectionModel().getSelection();
                    var customers = [];
                    Ext.Array.each(records, function(record) {
                        if(record.get('customerState') == 3){
                            customers.push(record.get('id'));
                        }
                    });
                    var recoverLength = customers.length;
                    if(recoverLength <= 0){
                        Ext.Msg.alert('提示', '您选择的记录中不包含已分配的记录！');
                        return;
                    }

                    Ext.MessageBox.confirm('提示', "您确定回收选中的客户吗？", function(button){
                        if(button=='yes'){
                            me.GridDoActionUtil.doAjax('publicCustomer/recover',{customers:"["+customers.join(",")+"]"},grid.getStore(),false);
                            grid.getSelectionModel().deselectAll();
                            Ext.example.msg('提示', '您共选择'+records.length+"条数据<br>成功回收"+recoverLength+"条数据");
                        }
                    })
                }
            },
            'publicCustomerMain publicCustomerList button[operationId=public_customer_apply]':{   // 客户申请
                click:function(btn) {
                    var me = this;
                    var grid = btn.up('baseList');
                    var records = grid.getSelectionModel().getSelection();
                    var customers = [];
                    Ext.Array.each(records, function(record) {
                        if(record.get('customerState') == 1){
                            customers.push(record.get('id'));
                        }
                    });
                    var applyLength = customers.length;
                    if(applyLength <= 0){
                        Ext.Msg.alert('提示', '您选择的记录中不包含空闲中的记录！');
                        return;
                    }

                    Ext.MessageBox.confirm('提示', "您确定申请选中的客户吗？", function(button){
                        if(button=='yes'){
                            me.GridDoActionUtil.doAjax('publicCustomer/apply',{customers:"["+customers.join(",")+"]"},grid.getStore(),false);
                            grid.getSelectionModel().deselectAll();
                            Ext.example.msg('提示', '您共选择'+records.length+"条数据<br>成功递交申请"+applyLength+"条数据");
                        }
                    })
                }
            }
        });
    }
});