Ext.define("user.view.workBench.WorkBenchSet",{
    extend: 'Ext.window.Window',
    alias: 'widget.workBenchSet',
    requires:[
        'Ext.ux.form.ItemSelector'
    ],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    modal: true,
    width: 500,
    layout: 'anchor',
    title: '工作台设置',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    items:[{
                        xtype: 'itemselector',
                        name: 'employeePortals',
                        buttons:['add', 'remove'],
                        anchor: '100%',
                        height:400,
                        style:'margin:3px 5px 5px 5px',
                        imagePath: 'ext-4.2.1/examples/ux/css/images',
                        store: Ext.create('user.store.EmployeePortalStore'),
                        displayField: 'title',
                        valueField: 'id',
                        fromTitle: '可选Portal',
                        toTitle: '已选Portal'
                    }
                    ],
                    listeners:{
                        afterrender:function(view, eOpts){
                            Ext.Ajax.request({
                                url:'userPortal/getSelectedUserPortal',
                                method:'POST',
                                timeout:4000,
                                async: false,
                                success:function(response,opts){
                                    var obj = Ext.JSON.decode(response.responseText);
                                    view.down('itemselector[name=employeePortals]').setValue(obj.selectedValue);
                                } ,
                                failure:function(e,op){
                                    Ext.Msg.alert("发生错误");
                                }
                            });
                        }
                    },
                    buttons: [
                        {text:'确定',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                        {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
                    ]
                }
            ]
        });
        me.callParent(arguments);
    },
    confirm : function(o, e, eOpts){
        var formObj = o.up('form');
        var form = formObj.getForm();
        var employeePortals = formObj.down('itemselector[name=employeePortals]').getValue();
        var params = {employeePortals:employeePortals};
        this.GridDoActionUtil.doAjax('userPortal/enableUserPortals',params,null,false);
        // 刷新工作台
        var workBench = Ext.ComponentQuery.query('mainviewport')[0].down('workBench');
        workBench.refreshWorkBench(workBench);
        this.close();
    },
    closeWin : function(o, e, eOpts){
        this.close();
    }
})