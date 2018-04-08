/**
 * Created by shqv on 2014-9-13.
 */
Ext.define('admin.view.dict.List', {
    extend: 'public.BaseList',
    alias: 'widget.dictList',
    autoScroll: true,
    store:Ext.create('admin.store.DataDictStore'),
    title: '数据字典',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'text',
    tbar:[
        {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true,autoShowEdit:true,target:'dictEdit'},
        {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,subject:'text'},
        {
            xtype: 'combo',
            fieldLabel: '所属用户',
            labelAlign: 'right',
            name: 'user',
            emptyText: '请选择...',
            autoSelect : true,
            forceSelection:true,
            displayField : 'name',
            valueField : 'id',
            queryMode: 'local',
            store:Ext.create('admin.store.UserNotUseSysTplStore',{pageSize:9999999})
        }
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'id',
            width: 50,
            dataIndex:'dataId'
        },
        {
            text:'名称',
            dataIndex:'text'
        },
        {
            text:'是否系统数据',
            dataIndex:'issys',
            renderer:function(val){
                if(val){
                    return '是';
                }else{
                    return "否";
                }
            }
        }/*,
        {
            text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        },
        {
            text:'修改时间',
            dataIndex:'lastUpdated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }*/
    ]
});