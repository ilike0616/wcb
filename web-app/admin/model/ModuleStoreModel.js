/**
 * Created by shqv on 2014-6-11.
 */
Ext.define("admin.model.ModuleStoreModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'moduleId',type: 'int'},
        {name: 'moduleName',type: 'string'},
        {name: 'name',type:'string'},
        {name:'store',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});