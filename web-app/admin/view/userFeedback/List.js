/**
 * Created by guozhen on 2015/04/23
 */
Ext.define('admin.view.userFeedback.List', {
    extend: 'public.BaseList',
    alias: 'widget.userFeedbackList',
    autoScroll: true,
    store: Ext.create('admin.store.UserFeedbackStore'),
    title: '企业反馈',
    split:true,
    tbar:[
        /*{xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add',autoShowAdd:true,target:'agentAdd'}*/
        {
            xtype: 'combo',
            fieldLabel: '所属企业',
            labelAlign: 'right',
            name: 'user',
            emptyText: '请选择...',
            autoSelect : true,
            forceSelection:true,
            displayField : 'name',
            valueField : 'id',
            queryMode: 'local',
            store:Ext.create('admin.store.UserStore',{pageSize:9999999})
        }
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'企业名称',
            width: 250,
            dataIndex:'userName'
        },
        {
            text:'反馈者',
            width: 150,
            dataIndex:'employeeName'
        },
        {
            text:'种类',
            width: 100,
            dataIndex:'kind',
            renderer:function(value){
                if (value === 1) {
                    return '一般反馈';
                }else if(value == 2){
                    return '重要反馈';
                }
                return '企业建议';
            }
        },
        {
            text:'反馈内容',
            dataIndex:'content',
            width: 400
        },
        {   text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            width: 250,
            format:'Y-m-d H:i:s'
        }
    ]
});