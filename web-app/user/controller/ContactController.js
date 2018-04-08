/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.ContactController',{
    extend : 'Ext.app.Controller',
    views:['contact.Main','contact.List','contact.Detail'] ,
    stores:['ContactStore','CustomerStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'contactList',
            selector : 'contactList'
        },
        {
            ref: 'contactDeleteBtn',
            selector: 'contactList button#deleteButton'
        }
    ],
    init:function() {
        this.control({
            'contactMain contactList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.getXType()=='gridview') {
                        grid = grid.up('grid');
                    }
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('contactMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,grid.getView());
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = selectModel.view.up('contactMain').down('tabpanel');
                    var baseList = selectModel.view.up('baseList');
                    if(baseList.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        baseList = baseList.ownerCt;
                    }
                    baseList.initValues = [{id:'contact',value:record.get('id')},{id:'contact.name',value:record.get('name')}];

                    if(tabpanel.hidden==false){
                        var sfa = tabpanel.down('sfaExecuteMain');
                        if(sfa.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            sfa = sfa.ownerCt;
                        }
                        if(sfa){
                            var store = sfa.down('sfaExecuteSfaList').getStore();
                            Ext.apply(store.proxy.extraParams,{moduleId:'contact',linkId:record.get('id')});
                            sfa.initValues = [{id:'moduleId',value:'contact'},{id:'linkId',value:record.get('id')},{id:'linkName',value:record.get('name')}];
                            store.load(function(records,operation, success){
                                sfa.setTitle(sfa.title1+"("+this.getTotalCount()+")");
                            });
                            sfa.down('sfaExecuteEventList').getStore().removeAll();
                            sfa.down('sfaExecuteEventExecuteList').getStore().removeAll();
                        }
                    }
                }
            },
        	'contactAdd button#save':{
        		click:function(btn){
        			this.GridDoActionUtil.doInsert(this.getContactList(),btn.up('form'),btn.up('window'));
        		}
        	}
        });
    }
})
