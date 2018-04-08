Ext.define("admin.controller.ModelController",{
    extend:'Ext.app.Controller',
    views:['model.List','model.Add','model.Edit','model.Main'] ,
    stores:['ModelStore'],
    models:['ModelModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref:'modelMain',
            selector:'modelMain'
        },
        {
            ref:'modelList',
            selector:'modelList'
        },{
            ref: 'modelDeleteBtn',
            selector: 'modelList button#deleteButton'
        }
    ],
    init:function(){
        this.application.getController("FieldController");
        this.control({
            'modelList':{
                select:function( o, record, index, eOpts){ 
                    var fieldList = this.getModelMain().down('fieldList');
                    var fieldStore = fieldList.getStore();
                    var params = fieldStore.baseParams;
                    Ext.apply(fieldStore.proxy.extraParams, {model:record.get("id")});
                    fieldStore.load();
                },
                deselect:function(){
                }
            },
            'modelList button#deleteButton':{
                click:function(btn){
                    this.GridDoActionUtil.doDelete(this.getModelList(),'modelName',this.getModelDeleteBtn());
                }
            }
        });
    }
});