/**
 * Created by like on 2015/9/18.
 */
Ext.define("user.model.SfaExecuteModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type:'int'},
        {name: 'user.id',type: 'int'},
        {name: 'user.name',type: 'string'},
        {name: 'employee.id',type: 'int'},
        {name: 'employee.name',type: 'string'},
        {name: 'sfa.id',type: 'int'},
        {name: 'sfa.name',type: 'string'},
        {name: 'module.id',type: 'int'},
        {name: 'module.moduleId',type: 'string'},
        {name:'linkId',type:'int'},
        {name:'state',type:'int'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});