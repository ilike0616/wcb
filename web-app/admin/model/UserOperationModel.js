/**
 * Created by guozhen on 2014-12-04.
 */

Ext.define("admin.model.UserOperationModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'userOperationId', type: 'int'},
        {name: 'text',type: 'string'},
        {name: 'iconCls',type: 'string'},
        {name: 'user',type: 'int'},
        {name: 'name',type: 'string'},
        {name: 'operation',type: 'int'},
        {name: 'operationName',type: 'string'},
        {name: 'module',type:'int'},
        {name: 'moduleName',type:'string'},
        {name: 'isEnable',type:'boolean'}
    ]
});