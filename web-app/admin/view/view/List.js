/**
 * Created by shqv on 2014-8-29.
 */
Ext.define('admin.view.view.List', {
    extend: 'public.BaseList',
    alias: 'widget.viewList',
    autoScroll: true,
    store:Ext.create('admin.store.ViewStore'),
    title: '字段管理',
    split:true,
    tbar:[
        {xtype:'button',text:'添加',itemId:'add',iconCls:'table_add'},
        {xtype:'button',text:'修改',itemId:'update',iconCls:'table_save',disabled:true,autoShowEdit:true,target:'viewEdit',autodisabled:true},
        {xtype:'button',text:'删除',itemId:'del',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,subject:'title'},
        {xtype:'button',text:'视图明细管理',itemId:'detail',iconCls:'table_save',disabled:true},
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
            text:'视图Id',
            width: 200,
            dataIndex:'viewId'
        },
        {
            text:'所属用户',
            width: 200,
            dataIndex:'user.name'
        },
        {
            text:'所属模块',
            width: 100,
            dataIndex:'module.moduleName'
        },
        {
            text:'客户端类型',
            dataIndex:'clientType',
            width: 100,
            xtype:'rowselecter',
            arry:[
                ['pc','pc端'],
                ['mobile','手机端']
            ]
        },
        {
            text:'页面类型',
            dataIndex:'viewType',
            width: 100,
            xtype:'rowselecter',
            arry:[
                ['form','表单'],
                ['list','列表']
            ]
        },
        {
            text:'标题',
            width: 200,
            dataIndex:'title'
        },
        {
            text:'查询视图',
            width: 100,
            xtype:'booleancolumn',
            dataIndex:'isSearchView',
            falseText :'否',
            trueText : '是'
        },
        {
            text:'导入视图',
            width: 100,
            xtype:'booleancolumn',
            dataIndex:'isImportOrExportView',
            falseText :'否',
            trueText : '是'
        }/*,
        {
            text:'创建时间',
            xtype: 'datecolumn',
            format:'Y-m-d H:i:s',
            dataIndex:'dateCreated'
        }*/
    ]
});