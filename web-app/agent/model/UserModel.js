/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("agent.model.UserModel",{
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
        {name:'moduleNames'}
    ]
});