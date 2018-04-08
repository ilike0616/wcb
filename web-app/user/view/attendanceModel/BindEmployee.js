Ext.define("user.view.attendanceModel.BindEmployee",{
    extend: 'Ext.window.Window',
    alias: 'widget.attendanceModelBindEmployee',
    requires:[
        'Ext.ux.form.ItemSelector'
    ],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    modal: true,
    width: 500,
    layout: 'anchor',
    title: '绑定用户',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    items:[{
                        xtype: 'baseComboBoxTree',
                        name: 'dept',
                        fieldLabel: '请选择部门',
                        displayField: 'text',
                        valueField:'id',
                        labelWidth:100,
                        rootVisible: false,
                        minPickerHeight: 200,
                        width:350,
                        margin:'10 0 10 10',
                        store : Ext.create('user.store.DeptStoreForEdit'),
                        listeners:{
                            select:function(o, newValue,eOpts ){
                                var itemSelector = me.down('itemselector');
                                var itemSelectorStore = itemSelector.getStore();
                                itemSelector.fromField.store.removeAll()
                                itemSelectorStore.load({params:{deptId: o.getValue()}});
                            }
                        }
                    },{
                            xtype: 'itemselector',
                            name: 'employees',
                            buttons:['add', 'remove'],
                            anchor: '100%',
                            height:300,
                            style:'margin:3px 5px 5px 5px',
                            imagePath: 'ext-4.2.1/examples/ux/css/images',
                            store: Ext.create('user.store.EmployeeStore'),
                            displayField: 'name',
                            valueField: 'id',
                            fromTitle: '可选员工',
                            toTitle: '已选员工'
                        },{
                            xtype:'hiddenfield',
                            name:'id'   // 模板ID
                        }
                    ],
                    listeners:{
                        afterrender:function(view, eOpts){
                            var record = view.up('window').listDom.getSelectionModel().getSelection()[0];
                            var attendanceModelId = record.get('id');
                            Ext.Ajax.request({
                                url:'attendanceModel/getSelectedEmployee?attendanceModelId='+attendanceModelId,
                                method:'POST',
                                timeout:4000,
                                async: false,
                                success:function(response,opts){
                                    var obj = Ext.JSON.decode(response.responseText);
                                    view.down('itemselector[name=employees]').setValue(obj.selectedValue);
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
        var attendanceModelId = formObj.down('hiddenfield[name=id]').getValue();
        var employees = formObj.down('itemselector[name=employees]').getValue();
        var params = {attendanceModelId:attendanceModelId,employees:employees};
        this.GridDoActionUtil.doAjax('attendanceModel/bindEmployee',params,null,false)
        this.close();
    },
    closeWin : function(o, e, eOpts){
        this.close();
    }
})