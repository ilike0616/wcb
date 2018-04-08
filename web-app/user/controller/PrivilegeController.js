/**
 * Created by shqv on 2014-6-17.
 */
Ext.define("user.controller.PrivilegeController",{
    extend:'Ext.app.Controller',
    views:['privilege.Main','privilege.List','privilege.BindUserOperation'
            ,'privilege.BindUserFieldMain','module.List','privilege.ModuleViewList','privilege.BindUserFieldList','employee.List'
            ,'privilege.BindUserPortal'],
    stores:['PrivilegeStore','BindPrivilegeUserFieldStore'],
    models:['PrivilegeModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs: [{
            ref: 'privilegeMain',
            selector: 'privilegeMain'
        },{
            ref: 'privilegeList',
            selector: 'privilegeMain privilegeList'
        },{
            ref:'moduleList',
            selector: 'privilegeMain bindUserFieldMain moduleList'
        },{
            ref:'employeeList',
            selector: 'privilegeMain employeeList'
        },{
            ref:'moduleViewList',
            selector: 'privilegeMain bindUserFieldMain moduleViewList'
        },{
            ref:'bindUserFieldList',
            selector: 'privilegeMain bindUserFieldMain bindUserFieldList'
        },{
            ref:'bindUserOperation',
            selector:'privilegeMain bindUserOperation'
        },{
            ref:'bindUserPortal',
            selector:'privilegeMain bindUserPortal'
        },{
            ref: 'privilegeDeleteBtn',
            selector: 'privilegeList button#deleteButton'
        }
    ],
    init:function(){
        this.control({
            'privilegeMain privilegeList':{
                itemclick:function( o, record, item, index, e, eOpts ){
                    var me = this;
                    var panel = o.up('privilegeMain').down('tabpanel');
                    var myMask = new Ext.LoadMask(panel, {
                        msg:"正在加载数据，请稍后..."
                    });
                    myMask.show();
                    Ext.defer(function(){
                        me.getBindUserOperation().setItems(record.get('id'));
                        me.getBindUserPortal().setItems(record.get('id'));
                        var employeeStore = me.getEmployeeList().getStore();
                        Ext.apply(employeeStore.proxy.extraParams,{privilegeId:record.get('id')});
                        employeeStore.load();
                        myMask.hide();
                    },100)
                },
                deselect:function(o,record, index, eOpts){
                }
            },
            // 权限绑定字段-》模块列表显示
            'privilegeMain bindUserFieldMain moduleList':{
                select:function( o, record, index, eOpts){
                    var store = this.getModuleViewList().getStore();
                    Ext.apply(store.proxy.extraParams,{module:record.get('module')});
                    store.load();
                },
                deselect:function( o,record, index, eOpts){
                }
            },
            // 权限绑定字段->模块视图和视图字段显示
            'privilegeMain bindUserFieldMain moduleViewList':{
                select:function( o, record, index, eOpts){
                    var privilegeRecord = this.getPrivilegeList().getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(privilegeRecord) == 'undefined'){
                        alert("请选择权限！");
                        return;
                    }
                    var privilegeId = privilegeRecord.get('id');
                    var viewId = record.get('id');
                    var store = this.getBindUserFieldList().getStore();
                    Ext.apply(store.proxy.extraParams,{privilegeId:privilegeId,viewId:viewId});
                    store.load();
                },
                deselect:function( o,record, index, eOpts){
                }
            },
            // 权限绑定字段->绑定按钮
            'privilegeMain bindUserFieldMain bindUserFieldList button#bindBtn':{
                click : function(btn){
                    var privilegeRecord = this.getPrivilegeList().getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(privilegeRecord) == 'undefined'){
                        alert("请选择权限！");
                        return;
                    }
                    var privilegeId = privilegeRecord.get('id');

                    this.GridDoActionUtil.doSave(this.getBindUserFieldList(),Object,{privilegeId:privilegeRecord.get('id')});
                }
            },
            // 权限新增
            'privilegeMain privilegeList button#addButton':{
                click:function(btn){
                    var view = Ext.widget("privilegeAdd");
                    view.show();
                }
            },
            // 权限修改
            'privilegeMain privilegeList button#updateButton':{
                click:function(btn){
                    var record = btn.up('privilegeList').getSelectionModel().getSelection()[0];
                    var view = Ext.widget('privilegeEdit');
                    view.down('form').loadRecord(record);
                    view.show();
                }
            },
            // 权限删除
            'privilegeList button#deleteButton':{
                click:function(btn){
                    var grid = btn.up("privilegeList");
                    this.GridDoActionUtil.doDelete(grid,'name',this.getPrivilegeDeleteBtn());
                }
            }
        });
    }
});