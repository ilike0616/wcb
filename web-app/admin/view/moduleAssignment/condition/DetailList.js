Ext.define('admin.view.moduleAssignment.condition.DetailList', {
    extend: 'public.BaseList',
    alias: 'widget.conditionDetailList',
    autoScroll: true,
    store: Ext.create('admin.store.UserOptConditionDetailStore'),
    split:true,
    forceFit:true,
    alertName:'fieldName',
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',autodisabled:true,disabled:true},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',autodisabled:true,disabled:true,autoDelete:true}
    ],
    columns:[{
            text:'字段名称',
            dataIndex:'fieldNameText',
            width: 70
        },{
            text:'操作符',
            dataIndex:'operator',
            width: 40,
            renderer:function(value){
                if (value === '==') {
                    return '等于';
                }else if(value === '!='){
                    return '不等于';
                }else if(value === '>'){
                    return '大于';
                }else if(value === '>='){
                    return '大于等于';
                }else if(value === '<'){
                    return '小于';
                }else if(value === '<='){
                    return '小于等于';
                }else if(value === 'in'){
                    return '包含';
                }
                return '不包含';
            }
        },{
            text:'值',
            dataIndex:'valueText'
        },{
            text:'值标识',
            dataIndex:'valueFlag',
            width:60,
            renderer:function(value){
                if (value === 'field') {
                    return '字段值';
                }
                return '字面值';
            }
        }
    ]
});