/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define("admin.model.EmployeeModel",{
    extend : 'Ext.data.Model',
    fields : [
        {name: 'id', type: 'int'},
        {name: 'name', type: 'string'},
        {name: 'account', type: 'string'},
        {name:'password',type:'string'},
        {name:'email',type:'string'},
        {name:'checkEmail',type:'boolean'},
        {name:'phone',type:'string'},
        {name:'mobile',type:'string'},
        {name:'checkMobile',type:'boolean'},
        {name:'fax',type:'string'},
        {name:'enabled',type:'boolean'},
        {name:'isLocus',type:'boolean'},
        {name:'accountLocked',type:'boolean'},
        {name:'parentEmployee'},
        {name:'parentEmployee.name',type : 'string'},
        {name:'parentEmployeeName',type : 'string'},
        {name:'user',type : 'int'},
        {name:'userName',type:'string'},
        {name : 'dept'},
        {name : 'deptName',type:'string'},
        {name : 'dept.name',type : 'string'}
    ]

})
