/**
 * Created by shqv on 2014-9-14.
 */
Ext.define("admin.model.DataDictItemModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type: 'int'},
        {name:'itemId',type:'int'},
        {name:'text',type:'string'},
        {name:'seq',type:'int'},
        {name:'dict'},
        {name:'user'},
        {name:'dateCreated',type:'data'},
        {name:'lastUpdated',type:'data'}
    ]
});