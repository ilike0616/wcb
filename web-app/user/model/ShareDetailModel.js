/**
 * Created by guozhen on 2015-09-16.
 */
Ext.define("user.model.ShareDetailModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'employee'},
        {name: 'employeeName',type:'string'},
        {name: 'shareTo',type: 'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});