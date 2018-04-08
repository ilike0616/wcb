/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("user.model.EmployeePortalModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'title', type: 'string'},
        {name: 'idx', type: 'int'},
        {name: 'user'},
        {name: 'userName',type:'string'},
        {name: 'employee'},
        {name: 'employeeName',type:'string'},
        {name: 'userPortal'},
        {name: 'userPortalName',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});