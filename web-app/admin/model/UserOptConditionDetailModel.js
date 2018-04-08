/**
 * Created by GuoZhen on 2014-6-16.
 */
Ext.define("admin.model.UserOptConditionDetailModel",{
    extend:'Ext.data.Model',
    fields:[
        {name : 'id',type : 'int'},
        {name : 'fieldName',type : 'string'},
        {name : 'fieldNameText',type : 'string'},
        {name: 'operator', type: 'string'},
        {name : 'value',type:'string'},
        {name : 'valueText',type:'string'},
        {name : 'valueFlag',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});