/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.CustomerController',{
    extend : 'Ext.app.Controller',
    views:['customer.Main','customer.List'],
    stores:['CustomerStore'],
//    models:['CustomerModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
		{
		    ref : 'mainList',
		    selector : 'customerMain customerList'
		}
    ],
    init:function() {
    	this.application.getController("ContactController");
    	this.application.getController("CustomerFollowController");
    	this.application.getController("ContractOrderController");
    	//this.application.getController("ServiceTaskController");
    	this.application.getController("NoteController");
        this.control({
        	'customerMain customerList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.getXType()=='gridview') {
                        grid = grid.up('grid');
                    }
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('customerMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid.getView());
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = selectModel.view.up('customerMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];

                    if(tabpanel.hidden==false){
                        var follow = tabpanel.down('customerFollowList');
                        if(follow.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            follow = follow.ownerCt;
                        }
                        if(follow){
                            var store = follow.getStore();
                            Ext.apply(store.proxy.extraParams,{customer:record.get('id')});
                            follow.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];
                            store.load(function(records){
                                follow.setTitle(follow.title1+"("+records.length+")");
                            });
                        }
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
                        var contractOrder = tabpanel.down('contractOrderList');
                        if(contractOrder.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            contractOrder = contractOrder.ownerCt;
                        }
                        if(contractOrder){
                            var store = contractOrder.getStore();
                            Ext.apply(store.proxy.extraParams,{customer:record.get('id')});
                            contractOrder.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];
                            store.load(function(records,operation, success){
                                contractOrder.setTitle(contractOrder.title1+"("+this.getTotalCount()+")");
                            });
                        }
                        //var serviceTask = tabpanel.down('serviceTaskList');
                        //if(serviceTask.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        //    serviceTask = serviceTask.ownerCt;
                        //}
                        //if(serviceTask){
                        //    var store = serviceTask.getStore();
                        //    Ext.apply(store.proxy.extraParams,{customer:record.get('id')});
                        //    serviceTask.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];
                        //    store.load(function(records,operation, success){
                        //        serviceTask.setTitle(serviceTask.title1+"("+this.getTotalCount()+")");
                        //    });
                        //}
                        var sfa = tabpanel.down('sfaExecuteMain');
                        if(sfa.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            sfa = sfa.ownerCt;
                        }
                        if(sfa){
                            var store = sfa.down('sfaExecuteSfaList').getStore();
                            Ext.apply(store.proxy.extraParams,{moduleId:'customer',linkId:record.get('id')});
                            sfa.initValues = [{id:'moduleId',value:'customer'},{id:'linkId',value:record.get('id')},{id:'linkName',value:record.get('name')}];
                            store.load(function(records,operation, success){
                                sfa.setTitle(sfa.title1+"("+this.getTotalCount()+")");
                            });
                            sfa.down('sfaExecuteEventList').getStore().removeAll();
                            sfa.down('sfaExecuteEventExecuteList').getStore().removeAll();
                        }
                        var note = tabpanel.down('noteList');
                        if(note){
                            var store = note.down('dataview').getStore();
                            Ext.apply(store.proxy.extraParams,{customer:record.get('id')});
                            note.initValues = [{id:'customer',value:record.get('id')},{id:'customer.name',value:record.get('name')}];
                            store.load(function(records,operation, success){
                                note.setTitle(note.title1+"("+this.getTotalCount()+")");
                            });
                        }
                    }
                }
            },
            'customerMain customerList dataview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    if(e.getKey()==e.Q||e.getKey()==e.TAB){
                        var tab = view.up('customerMain').down('tabpanel');
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
                        var grid = view.up('baseList');
                        if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            grid = grid.ownerCt;
                        }
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,view);
                    }else if(e.getKey()==e.ESC){
                        view.up('customerMain').down('tabpanel').hide();
                    }
                }
            }
        });
    }
})
