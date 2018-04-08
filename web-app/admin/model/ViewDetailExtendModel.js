Ext.define("admin.model.ViewDetailExtendModel",{
    extend:'Ext.data.Model',
    fields:[
        {name:'id', type: 'int'},
        {name:'paramName', type: 'string'},
        {name:'paramValue'},
        {name:'paramType',type: 'string'},
        {name:'isBelongToEditor',type:'boolean'},
        {name:'viewDetail',type:'int'}
    ]
});