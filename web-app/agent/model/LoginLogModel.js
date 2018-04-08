/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("agent.model.LoginLogModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'userId', type: 'string'},
        {name: 'userName', type: 'string'},
        {name: 'employeeName', type: 'string'},
        {name: 'loginIp', type: 'string'},
        {name: 'loginKind', type: 'int'},
        {name: 'loginType', type: 'int'},
        {name: 'clientType', type: 'int'},
        {name: 'loginTime', type: 'date'},
        {name: 'phoneModel', type: 'string'},
        {name: 'macAddress', type: 'string'},
        {name:'dateCreated',type:'date'}
    ]
});