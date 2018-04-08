/**
 * Created by shqv on 2014-8-28.
 */
Ext.define("admin.model.ViewModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name:'viewId', type: 'string'},
        {name:'clientType',type:'string'},
        {name:'viewType',type:'string'},
        {name:'title',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'},
        {name:'forceFit',type:'boolean'},
        {name:'editable',type:'boolean'},
        {name:'user.id'},
        {name:'user.name'},
        {name:'module.id'},
        {name:'module.moduleName'},
        {name:'model.id'},
        {name:'model.modelName'},
        {name:'isSearchView'},
        {name:'isImportOrExportView'},
        {name:'columns'},
        {name:'remark'}
    ]
});