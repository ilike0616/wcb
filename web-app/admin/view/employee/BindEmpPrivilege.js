Ext.define("admin.view.employee.BindEmpPrivilege",{
    extend: 'Ext.window.Window',
    alias: 'widget.bindEmpPrivilege',
    requires:[
        'Ext.ux.form.ItemSelector'
    ],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    modal: true,
    width: 500,
    layout: 'anchor',
    title: '绑定用户权限',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    items:[{
                            xtype: 'itemselector',
                            name: 'employeePrivileges',
                            buttons:['add', 'remove'],
                            anchor: '100%',
                            height:300,
                            style:'margin:3px 5px 5px 5px',
                            imagePath: 'ext-4.2.1/examples/ux/css/images',
                            store: Ext.create('admin.store.PrivilegeStore'),
                            displayField: 'name',
                            valueField: 'id',
                            fromTitle: '可选权限',
                            toTitle: '已选权限'
                        },{
                            xtype:'hiddenfield',
                            name:'user'
                        },{
                            xtype:'hiddenfield',
                            name:'id'   // 员工ID
                        }
                    ],
                    listeners:{
                        afterrender:function(view, eOpts){
                            var userId = view.up('window').rec.get('user');
                            var itemSelector = view.down('itemselector[name=employeePrivileges]');
                            itemSelector.fromField.store.removeAll()
                            var itemSelectorStore = itemSelector.getStore()
                            itemSelectorStore.load({params:{userId: userId}});
                            var record = view.up('window').listDom.getSelectionModel().getSelection()[0];
                            var employeeId = record.get('id');
                            Ext.Ajax.request({
                                url:'employee/getSelectedPrivilege?employeeId='+employeeId,
                                method:'POST',
                                timeout:4000,
                                async: false,
                                success:function(response,opts){
                                    var obj = Ext.JSON.decode(response.responseText);
                                    view.down('itemselector[name=employeePrivileges]').setValue(obj.selectedValue);
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
        var employeeId = formObj.down('hiddenfield[name=id]').getValue();
        var employeePrivileges = formObj.down('itemselector[name=employeePrivileges]').getValue();
        var params = {employeeId:employeeId,employeePrivileges:employeePrivileges};
        this.GridDoActionUtil.doAjax('employee/bindEmpPrivilege',params,null,false)
        this.close();
    },
    closeWin : function(o, e, eOpts){
        this.close();
    }
})