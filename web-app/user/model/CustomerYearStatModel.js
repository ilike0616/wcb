/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("user.model.CustomerYearStatModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'employee',type:'int'},
        {name: 'employeeName', type: 'string'},
        {name: 'dept',type:'int'},
        {name: 'deptName',type: 'string'},
        {name: 'JANUARY', type: 'int'},
        {name: 'FEBRUARY', type: 'int'},
        {name: 'MARCH', type: 'int'},
        {name: 'APRIL', type: 'int'},
        {name: 'MAY', type: 'int'},
        {name: 'JUNE', type: 'int'},
        {name: 'JULY', type: 'int'},
        {name: 'AUGUST', type: 'int'},
        {name: 'SEPTEMBER', type: 'int'},
        {name: 'OCTOBER', type: 'int'},
        {name: 'NOVEMBER', type: 'int'},
        {name: 'DECEMBER', type: 'int'}
    ]
});