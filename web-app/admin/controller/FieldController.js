Ext.define("admin.controller.FieldController",{
    extend:'Ext.app.Controller',
    views:['field.List','field.Main'] ,
    stores:['FieldStore'],
    models:['FieldModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [
        {
            ref: 'fieldDeleteBtn',
            selector: 'fieldList button#deleteButton'
        }
    ],
    init:function(){
        this.application.getController("ModuleController");
        this.control({
            'fieldList button#deleteButton':{
                click:function(btn){
                    var fieldList = btn.up('fieldList');
                    this.GridDoActionUtil.doDelete(fieldList,'fieldName',this.getFieldDeleteBtn());
                }
            }
        });
    }
});