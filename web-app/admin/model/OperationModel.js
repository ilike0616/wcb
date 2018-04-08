/**
 * Created by guozhen on 2014-12-04.
 */

Ext.define("admin.model.OperationModel",{
    extend:'Ext.data.Model',
    fields:[
        {name:'id', type: 'int'},
        {name:'userOperationId', type: 'int'},
        {name:'operationId',type:'string'},
        {name:'type',type:'string'},
        {name:'showWin',type:'boolean'},
        {name:'autodisabled',type:'boolean'},
        {name:'text',type: 'string'},
        {name:'clientType',type:'string'},
        {name:'auto',type:'boolean'},
        {name:'isCustom',type:'boolean'},
        {name:'itemId',type: 'string'},
        {name:'iconCls',type: 'string'},
        {name:'url',type:'string'},
        {name:'ctrl',type: 'string'},
        {name:'vw',type:'string'},
        {name:'targetEl',type:'string'},
        {name:'optRecords',type:'string'},
        {name:'module',type:'int'},
        {name:'moduleName',type:'string'}
    ]
});