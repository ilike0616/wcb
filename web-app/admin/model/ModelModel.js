Ext.define("admin.model.ModelModel",{
    extend:'Ext.data.Model',
    fields:[
    	{name: 'id',type: 'int'},
        {name: 'modelClass',type: 'string'},
        {name: 'modelName',type: 'string'},
        {name: 'remark',type:'string'},
        {name: 'fields'}
    ]
});