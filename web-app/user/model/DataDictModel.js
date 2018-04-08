/**
 * Created by shqv on 2014-9-13.
 */
Ext.define("user.model.DataDictModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type: 'int'},
//        id:it.id,dataId:it.dataId,text:it.text,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated
        {name:'dataId',type:'int'},
        {name:'text',type:'string'},
        {name:'dateCreated',type:'data'},
        {name:'lastUpdated',type:'data'}
    ]
});