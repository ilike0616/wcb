/**
 * Created by guozhen on 2014/7/4.
 */
Ext.define("admin.model.RoleModel",{
    extend:'Ext.data.Model',
    fields:[
        {name : 'roleName',type :'string'},
        {name : 'user',type : 'int'},
        {name : 'name',type : 'string'},
        {name : 'depts'},
        {name : 'deptsNames'},
        {name : 'privileges'},
        {name : 'privilegesNames'},
        {name : 'employees'},
        {name : 'employeesNames'}
    ]
});