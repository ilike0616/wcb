/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("admin.model.UserModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'userId', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'balance', type: 'float'},
        {name: 'sumAddBalance', type: 'float'},
        {name: 'sumAddRealBalance', type: 'float'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'},
        {name:'modules'},
        {name:'moduleNames'},
        {name:'edition',type: 'int'},
        {name:'editionName',type: 'string'},
        {name: 'isTest', type: 'string'},
        {name: 'testDueDate', type: 'date'},
        {name: 'allowedNum', type: 'int'},
        {name: 'monthlyFee', type: 'float'},
        {name: 'dueDate', type: 'date'},
        {name: 'enabled', type: 'string'},
        {name:'agent'},
        {name:'isTemplate'},
        {name:'useSysTpl'},
        {name:'versionCopyState',type:'int'},
        {name:'agentName',type:'string'}
    ]
});