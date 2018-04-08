Ext.define("user.model.NoteModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id'},
        {name: 'employee'},
        {name: 'tag',type: 'int'},
        {name: 'customer'},
        {name: 'content',type: 'string'},
        {name: 'location',type:'string'},
        {name: 'longtitude',type:'string'},
        {name:'latitude',type:'string'},
        {name:'dateCreated',type:'date'},
        {name: "ats"},
        {name: "zs"},
        {name: 'zsNum', type: 'int'},
        {name: "files"},
        {name: "voices"},
        {name: "comments"},
        {name: 'commentsNum', type: 'int'}
    ]
});