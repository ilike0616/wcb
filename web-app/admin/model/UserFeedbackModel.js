/**
 * Created by shqv on 2015-5-11.
 */
Ext.define("admin.model.UserFeedbackModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'kind', type: 'int'},
        {name: 'content', type: 'string'},
        {name: 'user', type: 'int'},
        {name: 'userName', type: 'string'},
        {name: 'employee', type: 'int'},
        {name: 'employeeName', type: 'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});