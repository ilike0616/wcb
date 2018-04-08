/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("agent.model.AddBalanceModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'kind', type: 'int'},
        {name: 'operator'},
        {name: 'operatorName',type: 'string'},
        {name: 'prepaidUser'},
        {name: 'prepaidUserName',type: 'string'},
        {name: 'createAdmin'},
        {name: 'createAdminName',type: 'string'},
        {name: 'createAgent'},
        {name: 'createAgentName', type: 'string'},
        {name: 'agent'},
        {name: 'agentName', type: 'string'},
        {name: 'user'},
        {name: 'userName', type: 'string'},
        {name: 'preBalance', type: 'float'},
        {name: 'postBalance', type: 'float'},
        {name: 'balance', type: 'float'},
        {name: 'realAddBalance', type: 'float'},
        {name: 'remark', type: 'string'},
        {name: 'type', type: 'int'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});