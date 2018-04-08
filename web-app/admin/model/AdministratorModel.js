/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("admin.model.AdministratorModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'adminId', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'password', type: 'string'},
        {name: 'email', type: 'string'},
        {name: 'phone', type: 'string'},
        {name: 'mobile', type: 'string'},
        {name: 'fax', type: 'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});