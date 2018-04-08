/**
 * Created by shqv on 2014-8-29.
 */
Ext.define('admin.view.userField.main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.userFieldMain',
    layout: 'border',
    items: [
        {
            xtype:'toolbar',
            region:'north',
            items: [{
                xtype: 'fieldset',
                title: '过滤条件',
                items: [{
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
                },{
                    xtype: 'combo',
                    labelAlign: 'right',
                    fieldLabel: '所属模块',
                    name: 'module',
                    emptyText: '请选择...',
                    autoSelect : true,
                    forceSelection:true,
                    displayField : 'moduleName',
                    valueField : 'id',
                    queryMode: 'local',
                    store:Ext.create('admin.store.ModuleListStore')
                }
                ]
            }]
        },
        {
            region:'center',
            xtype: 'tabpanel',
            tabPosition: 'top',
            items: [{
                title: '全部',
                xtype:'userFieldList',
                store:Ext.create('admin.store.UserFieldStore')
            },{
                title: '字符',
                dbtype:'string',
                xtype:'userFieldList',
                columns:[
                    {
                        xtype: 'rownumberer',
                        width: 40,
                        sortable: false
                    },{
                        text:'所属公司',
                        sortName:'user.id',
                        dataIndex:'user.name'
                    },{
                        text:'所属模块',
                        sortName:'module.id',
                        dataIndex:'module.moduleName'
                    },{
                        text:'字段名称',
                        dataIndex:'fieldName'
                    },{
                        text:'标题',
                        dataIndex:'text'
                    },{
                        text:'数据类型',
                        sortName:'dbType',
                        dataIndex :'dbTypeName'
                    },{
                        text:'默认值',
                        dataIndex:'defValue'
                    },{
                        text:'数据字典',
                        dataIndex:'dict'
                    },{
                        text:'是否必填',
                        dataIndex:'bitian',
                        xtype:'booleancolumn',
                        trueText:'是',
                        falseText:'否'
                    },{
                        text:'是否唯一',
                        dataIndex:'weiyi',
                        xtype:'booleancolumn',
                        trueText:'是',
                        falseText:'否'
                    },{
                        text:'是否模糊查询',
                        dataIndex:'mohu',
                        xtype:'booleancolumn',
                        trueText:'是',
                        falseText:'否'
                    },{
                        text:'是否必须存在',
                        dataIndex:'must',
                        xtype:'booleancolumn',
                        trueText:'是',
                        falseText:'否'
                    },{
                        text:'数据字典',
                        sortName : 'dict.id',
                        dataIndex:'dict.text'
                    }
                ],
                store:Ext.create('admin.store.UserFieldStore')
            },{
                title: '数字',
                dbtype:'int',
                xtype:'userFieldList',
                store:Ext.create('admin.store.UserFieldStore')
            },{
                title: '浮点',
                dbtype:'float',
                xtype:'userFieldList',
                store:Ext.create('admin.store.UserFieldStore')
            },{
                title: '日期',
                dbtype:'date',
                xtype:'userFieldList',
                store:Ext.create('admin.store.UserFieldStore')
            },{
                title: '真假',
                dbtype:'boolean',
                xtype:'userFieldList',
                store:Ext.create('admin.store.UserFieldStore')
            },{
                title: '文件',
                dbtype:'doc',
                xtype:'userFieldList',
                store:Ext.create('admin.store.UserFieldStore')
            }]


        }
    ]
});