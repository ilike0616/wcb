/**
 * Created by like on 2015-04-24.
 */
Ext.define("admin.model.AppVersionModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'platform', type: 'string'},
        {name: 'edition', type: 'string'},
        {name: 'appVersion', type: 'string'},
        {name: 'remark', type: 'string'},
        {name: 'administrator.id'},
        {name: 'administrator.name'},
        {name: 'appPackage'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});