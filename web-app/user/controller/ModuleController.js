Ext.define("user.controller.ModuleController",{
    extend:'Ext.app.Controller',
    views:['module.List'] ,
    stores:['ModuleStore'],
    models:['ModuleModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref:'moduleList',
            selector:'moduleList'
        }
    ],
    init:function(){
        this.control({
            'moduleList':{
                select:function( o, record, index, eOpts){
                    this.getModuleDeleteBtn().setDisabled(false);
                    this.getModuleUpdateBtn().setDisabled(false);

                    /*var operationStore = this.getOperationList().getStore();
                    Ext.apply(operationStore.proxy.extraParams,{moduleId:record.get("id")});
                    operationStore.load();

                    var userStore = this.getUserList().getStore();
                    Ext.apply(userStore.proxy.extraParams,{moduleId:record.get("id")});
                    userStore.load();*/
                },
                render:function(component,options){
                },
                deselect:function(){
                    this.getModuleDeleteBtn().setDisabled(true);
                    this.getModuleUpdateBtn().setDisabled(true);

                }
            }
        });
    }
});