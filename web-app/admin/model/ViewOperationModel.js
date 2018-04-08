/**
 * Created by shqv on 2014-8-28.
 */
Ext.define("admin.model.ViewOperationModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'orderIndex', type: 'int'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'},
        {name:'userOperation.id'},
        {name:'userOperation.text'},
        {name:'view.id'},
        {name:'view.title'}
    ]
});