Ext.define("admin.controller.ModuleController",{
    extend:'Ext.app.Controller',
    views:['module.Main','module.List','module.Add','module.Edit','operation.List','portal.List','portal.Add','portal.Edit'
        ,'moduleStore.List','moduleStore.Add','moduleStore.Edit'] ,
    stores:['ModuleStore'],
    models:['ModuleModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref:'moduleList',
            selector:'moduleList'
        },{
            ref: 'moduleDeleteBtn',
            selector: 'moduleList button#deleteButton'
        },{
            ref: 'moduleUpdateBtn',
            selector: 'moduleList button#updateButton'
        },{
            ref:'userList',
            selector:'userList'
        },{
            ref:'operationList',
            selector:'moduleMain operationList'
        }
    ],
    init:function(){
        this.control({
            'moduleMain moduleList':{
                select:function( o, record, index, eOpts){
                    this.getModuleDeleteBtn().setDisabled(false);
                    this.getModuleUpdateBtn().setDisabled(false);
                    Ext.Array.forEach(eOpts.up('moduleMain').query('operationList'),function(o,index){
                    	var store = o.getStore();
                    	var clientType = o.clientType;
                    	Ext.apply(store.proxy.extraParams,{moduleId:record.get("id"),clientType:clientType});
                    	store.load();
                    });
                    var userStore = this.getUserList().getStore();
                    Ext.apply(userStore.proxy.extraParams,{moduleId:record.get("id")});
                    userStore.load();
                    var moduleMain = eOpts.up('moduleMain');
                    var portalList = moduleMain.down('portalList');
                    var portalStore = portalList.getStore();
                    Ext.apply(portalStore.proxy.extraParams,{moduleId:record.get("id")});
                    portalStore.load();
                    var moduleStoreList = moduleMain.down('moduleStoreList');
                    var moduleStoreStore = moduleStoreList.getStore();
                    Ext.apply(moduleStoreStore.proxy.extraParams,{moduleId:record.get("id")});
                    moduleStoreStore.load();
                },
                deselect:function(){
                    this.getModuleDeleteBtn().setDisabled(true);
                    this.getModuleUpdateBtn().setDisabled(true);
                }
            },
            'moduleList button#addButton':{
                click:function(btn){
                    var moduleRecord = this.getModuleList().getSelectionModel().getSelection()[0];
                    var parentModule = moduleRecord.get("id");
                    var view = Ext.widget('moduleAdd');
                    if(parentModule){
                        Ext.getCmp("moduleAddParentModule").setValue(parentModule);
                    }
                    view.show();
                }
            },
            'moduleList button#updateButton':{
                click:function(btn){
                     var record = this.getModuleList().getSelectionModel().getSelection()[0];
                     var view = Ext.widget('moduleEdit');
                     view.down('form').loadRecord(record);
                     view.show();
                }
            },
            'moduleList button#deleteButton':{
                click:function(btn){
                    var grid = btn.up("moduleList");
                    this.GridDoActionUtil.doDelete(grid,'moduleName',this.getModuleDeleteBtn());
                }
            },
            'moduleMain portalList button#addButton':{
                click:function(btn){
                    var moduleList = btn.up('moduleMain').down('moduleList');
                    var selectedModule = moduleList.getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(selectedModule) == 'undefined' || selectedModule == null){
                        Ext.Msg.alert('提示','请先选择模块！');
                        return;
                    }

                    var add = Ext.widget('portalAdd')
                    add.down('hiddenfield').setValue(selectedModule.get('id'));
                    add.show();
                }
            },
            'moduleMain moduleStoreList button#addButton':{
                click:function(btn){
                    var moduleList = btn.up('moduleMain').down('moduleList');
                    var selectedModule = moduleList.getSelectionModel().getSelection()[0];
                    if(!selectedModule){
                        Ext.Msg.alert('提示','请先选择模块！');
                        return;
                    }

                    var add = Ext.widget('moduleStoreAdd')
                    add.down('hiddenfield').setValue(selectedModule.get('id'));
                    add.show();
                }
            }
        });
    }
});