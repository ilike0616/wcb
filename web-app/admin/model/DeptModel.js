/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define('admin.model.DeptModel',{
    extend : 'Ext.data.TreeModel',
    fields : [
        {name : 'name',type : 'string'},
        {name : 'dateCreated',type : 'date'},
        {name : 'lastUpdated',type : 'date'},
        {name : 'parentDept'},
        {name : 'parentName',type : 'string'},
        {name : 'user'},
        {name : 'name',type : 'string'},
        {name : 'employees'},
        {name:'leaf',type:'boolean'},
        {name:'expanded',type:'boolean'},
        {name : 'roles'},
        {name : 'rolesNames'}
    ]
})