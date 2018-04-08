/**
 * Created by shqv on 2014-6-17.
 */

Ext.define("admin.model.PrivilegeModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'name',type: 'string'},
        {name: 'user'},
        {name:'userName',type:'string'}
    ]
});