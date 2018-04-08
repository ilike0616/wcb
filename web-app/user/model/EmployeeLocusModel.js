/**
 * Created by like on 2015-04-07.
 */
Ext.define("user.model.EmployeeLocusModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id'},
        {name: 'name',type: 'string'},
        {name: 'location',type:'string'},
        {name: 'longtitude',type:'string'},
        {name:'latitude',type:'string'},
        {name:'locusDate',type:'date'}
    ]
});