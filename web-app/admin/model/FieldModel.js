Ext.define("admin.model.FieldModel",{
    extend:'Ext.data.Model',
    fields:[
    	{name: 'id',type: 'int'},
        {name: 'fieldName',type: 'string'},
        {name: 'dbType',type:'string'},
        {name: 'relation',type:'string'},
        {name: 'remark',type:'string'},
        {name: 'model.modelName',type:'string'},
        {name: 'model.modelClass',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});