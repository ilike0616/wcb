Ext.define("admin.model.ViewDetailModel",{
    extend:'Ext.data.Model',
    fields:[
        {name:'id', type: 'int'},
        {name:'orderIndex', type: 'int'},
        {name:'label', type: 'string'},
        {name:'defaultValue',type:'string'},
        {name:'extraCondition',type:'string'},
        {name:'userField.id',type:'int'},
        {name:'userField.fieldName',type:'string'},
        {name:'userField.text',type:'string'},
        {name:'userField.bitian',type:'boolean'},
        {name:'userField.min',type:'int'},
        {name:'userField.max',type:'int'},
        {name:'userField.scale',type:'int'},
        {name:'userField.dict',type:'int'},
        {name:'userField.maxSize',type:'int'},
        {name:'userField.dateFormat',type:'string'},
        {name:'userField.trueText',type:'string'},
        {name:'userField.falseText',type:'string'},
        {name:'userField.isMoney',type:'boolean'},
        {name:'pageType',type:'string'},
        {name:'defValue',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'},
        {name:'width',type:'int'},
        {name:'inputType',type:'string'},
        {name:'cols',type:'int'},
        {name:'rows',type:'int'},
        {name:'inputFormat',type:'string'},
        {name:'initName',type:'string'},
        {name:'isSubmitValue',type:'boolean'},
        {name:'disabled',type:'boolean'},
        {name:'readOnly',type:'boolean'},
        {name:'paramViewId',type:'string'},
        {name:'paramStore',type:'string'},
        {name:'targetIdName',type:'string'},
        {name:'isHyperLink',type:'boolean'},
        {name:'locked',type:'boolean'},
        {name:'sortable',type:'boolean'},
        {name:'sortName',type:'string'},
        {name:'isCurrentDate',type:'boolean'},
        {name:'defExpanded',type:'boolean'},
        {name:'isCollapsible',type:'boolean'},
        {name:'user.id'},
        {name:'user.name'},
        {name:'view.id'},
        {name:'view.viewId'},
        {name:'view.title'},
        {name:'view.editable',type:'boolean'},
        {name:'listView.id'},
        {name:'listView.viewId'},
        {name:'listView.title'},
        {name:'remark'},
        {name:'dbTypeName'},
        {name:'win'}
    ]
});