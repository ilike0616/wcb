/**
 * Created by like on 2015/8/31.
 */
Ext.define("user.model.UserModuleModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type:'int'},
        {name: 'moduleId',type: 'string'},
        {name: 'moduleName',type: 'string'},
        {name:'parentModule',type:'string'},
        {name:'parentModuleName',type:'string'},
        {name:'model.id',type:'int'},
        {name:'model.modelName',type:'string'}
    ]
});