/**
 * Created by like on 2015/8/20.
 */
/**
 * Created by like on 2015/6/17.
 */
Ext.define('admin.view.userField.associatedRequired.Win', {
    extend: 'Ext.window.Window',
    alias: 'widget.associatedRequiredWin',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 950,
    height: 450,
    modal : true,
    layout: 'border',
    dbtype : '',    // 用于筛选记录
    userValue : '', // 上个页面选择的用户
    moduleValue : '', // 上个页面选择的模块
    initComponent: function() {
        var me = this;
        title = me.title,
            dbtype = me.dbtype,
            userValue = me.userValue,
            moduleValue = me.moduleValue,
            Ext.applyIf(me, {
                items: [
                    {
                        region:'north',
                        border : 0,
                        items: [{
                            xtype: 'fieldset',
                            title: '必选条件',
                            layout : 'column',
                            columns : 2,
                            margin : '0 5 0 5',
                            defaults :{
                                labelWidth : 70,
                                padding : '3 3 3 3'
                            },
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
                                store:Ext.create('admin.store.UserStore',{
                                    listeners:{
                                        load : function(){  // load数据时，把前台页面的数据对应的设置进去
                                            if(me.userValue != "" && me.userValue != null){
                                                me.down("combo[name=user]").setValue(me.userValue);
                                            }
                                        }
                                    }
                                }),
                                listeners : {
                                    change : function(){
                                        me.searchField(this,'user');
                                    }
                                }
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
                                store:Ext.create('admin.store.ModuleListStore',{
                                    listeners:{
                                        load : function(){  // load数据时，把前台页面的数据对应的设置进去
                                            if(me.moduleValue != "" && me.moduleValue != null){
                                                me.down("combo[name=module]").setValue(me.moduleValue);
                                            }
                                        }
                                    }
                                }),
                                listeners : {
                                    change : function(){
                                        me.searchField(this,'module');
                                    }
                                }
                            }]
                        }]
                    },
                    {
                        region:'center',
                        border : 0,
                        items : [{
                            xtype:'baseList',
                            autoScroll: true,
                            enableComplexQuery:false,
                            margin : '5 5 0 5',
                            height: 315,
                            overflowY: 'auto',
                            store : Ext.create('admin.store.AssociatedRequiredStore'),
                            tbar:[
                                {xtype:'button',text:'新增',itemId:'addButton',iconCls:'table_add'},
                                {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
                                {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,subject:'name'}
                            ],
                            columns:[
                                {
                                    xtype: 'rownumberer',
                                    width: 40,
                                    sortable: false
                                },
                                {
                                    text:'描述',
                                    width: 250,
                                    dataIndex:'name'
                                },
                                {
                                    text:'所属模块',
                                    width: 100,
                                    sortName:'module.id',
                                    dataIndex:'module.moduleName'
                                },{
                                    text: '类型',
                                    width: 100,
                                    dataIndex: 'kind',
                                    xtype: "rowselecter",
                                    arry: [
                                        ['1', '必填'],
                                        ['2', '唯一']
                                    ]
                                },
                                {
                                    text:'字段名称',
                                    dataIndex:'userFields.text',
                                    xtype: "arraycolumn",
                                    flex:1
                                }
                            ]
                        }]
                    }
                ],
                buttons: [
                    {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
                ]
            })
        me.callParent(arguments);
    },
    searchField : function(o,from){  // 查询可选字段
        var me = this;
        var win = o.up("associatedRequiredWin");
        var userValue = win.down("combo[name=user]").getValue();
        var moduleValue = win.down("combo[name=module]").getValue();
        // 只有手动选择的情况下，userValue为空，才给提示
        if((userValue == null || userValue == 'undefined') && (me.userValue == null || me.userValue == 'undefined')){
            Ext.example.msg('提示', '请选择所属用户！');
            return;
        }
        if((moduleValue == null || moduleValue == 'undefined') && (me.moduleValue == null || me.moduleValue == 'undefined')){
            Ext.example.msg('提示', '请选择所属模块！');
            return;
        }
        // 因为前面页面用户和模块都选了，所以给本页面的用户和模块默认赋值时，都执行了本方法。
        // 因此两者必须都不为空，否则直接返回，什么都不提示
        if((userValue == null && moduleValue != null) || (userValue != null && moduleValue == null)){
            return;
        }
        var grid = win.down('baseList'),
            store = grid.store;
        Ext.apply(store.proxy.extraParams,{user : userValue,module : moduleValue})
        store.load();
    },
    closeWin : function(o, e, eOpts){   // 关闭
        var me = this;
        me.close();
    }
});