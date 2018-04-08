/**
 * Created by like on 2015-04-29.
 */
Ext.define("admin.model.EditionModel",{
    extend : 'Ext.data.Model',
    fields : [
        {name: 'id', type: 'int'},
        {name: 'name', type: 'string'},
        {name:'kind',type:'string'},
        {name:'ver',type:'string'},
        {name:'monthlyFee',type:'string'},
        {name:'remark',type:'string'},
        {name:'templateUser'},
        {name:'templateUserName',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
})