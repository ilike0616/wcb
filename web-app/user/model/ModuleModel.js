/**
 * Created by shqv on 2014-6-11.
 */
Ext.define("user.model.ModuleModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'moduleId',type: 'string'},
        {name: 'moduleName',type: 'string'},
        {name: 'companyTotal',type:'int'},
        {name: 'privilegeTotal',type:'int'},
        {name:'parentModule',type:'string'},
        {name:'parentModuleName',type:'string'},
        {name:'model.id',type:'int'},
        {name:'model.modelName',type:'string'}
    ]
});