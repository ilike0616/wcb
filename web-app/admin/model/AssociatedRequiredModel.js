/**
 * Created by like on 2015/8/20.
 */
Ext.define("admin.model.AssociatedRequiredModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type: 'int'},
        {name:'user.id',type:'int'},
        {name:'user.name',type:'String'},
        {name: 'module.moduleName',type:'string'},
        {name: 'module.modelId',type:'string'},
        {name: 'module.id',type:'int'},
        {name:'kind',type:'int'},
        {name:'name',type:'String'},
        {name:'userFields'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});