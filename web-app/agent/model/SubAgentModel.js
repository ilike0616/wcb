/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("agent.model.SubAgentModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'agentId', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'password', type: 'string'},
        {name: 'isAllowLowerAgent', type: 'boolean'},
        {name: 'email', type: 'string'},
        {name: 'phone', type: 'string'},
        {name: 'mobile', type: 'string'},
        {name: 'fax', type: 'string'},
        {name: 'balance', type: 'float'},
        {name: 'sumAddBalance', type: 'float'},
        {name: 'sumAddRealBalance', type: 'float'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});