Ext.define("user.model.LocusDetailModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id'},
        {name: 'employee'},
        {name: 'employee.name',type:'string'},
        {name: 'locus'},
        {name: 'location',type: 'string'},
        {name: 'longtitude', type: 'string'},
        {name: 'latitude',type: 'string'},
        {name: 'locusDate',type:'date'},
        {name: 'mac',type:'string'},
        {name:'model',type:'string'},
        {name:'dateCreated',type:'date'}
    ]
});