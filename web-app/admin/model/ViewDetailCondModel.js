/**
 * Created by guozhen on 2014/7/4.
 */
Ext.define("admin.model.ViewDetailCondModel",{
    extend:'Ext.data.Model',
    fields:[
        {name : 'fieldName',type :'string'},
        {name : 'fieldNameText',type :'string'},
        {name : 'operator',type : 'string'},
        {name : 'operatorText',type : 'string'},
        {name : 'userFieldValue',type : 'string'},
        {name : 'userFieldTextValue',type : 'string'},
        {name : 'dbType',type :'string'},
        {name : 'startDate',type:'date'},
        {name : 'endDate',type:'date'}
    ]
});