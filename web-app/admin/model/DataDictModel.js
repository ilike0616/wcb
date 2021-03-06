/**
 * Created by shqv on 2014-9-13.
 */
Ext.define("admin.model.DataDictModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type: 'int'},
//        id:it.id,dataId:it.dataId,text:it.text,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated
        {name:'dataId',type:'int',useNull:true},
        {name:'text',type:'string'},
        {name:'issys',type:'boolean'},
        {name:'user'},
        {name:'fields'},
        {name:'items'},
        {name:'dateCreated',type:'data'},
        {name:'lastUpdated',type:'data'}
    ]
});