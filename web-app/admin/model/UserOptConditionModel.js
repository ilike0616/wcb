/**
 * Created by shqv on 2014-6-16.
 */
Ext.define("admin.model.UserOptConditionModel",{
    extend:'Ext.data.Model',
    fields:[
        {name : 'id',type : 'int'},
        {name : 'name',type : 'string'},
        {name: 'remark', type: 'string'},
        {name : 'user'},
        {name : 'userName',type : 'string'},
        {name : 'userOperation'},
        {name : 'userOperationName',type : 'string'},
        {name : 'userOperationFilterDetail'},
        {name : 'userOperationFilterDetailNames',type : 'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});