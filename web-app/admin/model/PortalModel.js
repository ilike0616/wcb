/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("admin.model.PortalModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'type', type: 'int'},
        {name: 'title',type: 'string'},
        {name: 'height', type: 'int'},
        {name: 'xtype', type: 'string'},
        {name: 'module'},
        {name: 'moduleName', type: 'string'},
        {name: 'viewId', type: 'string'},
        {name: 'viewStore', type: 'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});