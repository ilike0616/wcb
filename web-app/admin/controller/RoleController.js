/**
 * Created by guozhen on 2014/6/23.
 */

Ext.define('admin.controller.RoleController',{
    extend : 'Ext.app.Controller',
    views:['role.Main','role.List','role.Add','role.Edit','dept.DeptUserList','privilege.List'] ,
    stores:['RoleStore'],
    models:['RoleModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'roleList',
            selector : '#roleMain roleList'
        },
        {
            ref : 'roleUserList',
            selector : 'deptUserList'
        },
        {
            ref : 'privilegeList',
            selector : 'privilegeList'
        },
        {
            ref:'roleAddForm',
            selector:'roleAdd form'
        },
        {
            ref:'roleAddWin',
            selector:'roleAdd'
        },
        {
            ref:'roleEditForm',
            selector:'roleEdit form'
        },
        {
            ref:'roleEditWin',
            selector:'roleEdit'
        },
        {
            ref: 'roleDeleteBtn',
            selector: 'roleList button[itemid=deleteButton]'
        },
        {
            ref: 'roleUpdateBtn',
            selector: 'roleList button[itemid=updateButton]'
        }
    ],
    init:function() {
        this.control({
            '#roleMain deptUserList': {
                itemclick : function(o,record, item, index, e, eOpts){
                    var roleStore = this.getRoleList().getStore();
                    roleStore.load({params:{userId:record.get("id")}});

                    var privilegeStore = this.getPrivilegeList().getStore();
                    privilegeStore.load({params:{userId:record.get("id")}});
                }
            },
            'roleList': {
                select: function (o, record, index, eOpts) {
                    this.getRoleDeleteBtn().setDisabled(false);
                    this.getRoleUpdateBtn().setDisabled(false);

                    var privilegeStore = this.getPrivilegeList().getStore();
                    privilegeStore.load({params:{roleId:record.get("id")}});
                },
                deselect: function () {
                    this.getRoleDeleteBtn().setDisabled(true);
                    this.getRoleUpdateBtn().setDisabled(true);
                }
            },
            'roleList button[itemid=addButton]': {
                click: function (btn) {
                    var value = "";
                    var userRecord = Ext.getCmp('tabPanel').getActiveTab().items.items[0].getSelectionModel().getSelection()[0];
                    if(!userRecord){
                        Ext.Msg.alert("提示","请您先选择用户！");
                        return;
                    }else{
                        value = userRecord.get("id");
                    }
                    var view = Ext.widget('roleAdd');
                    Ext.getCmp("roleAddUser").setValue(value);

                    view.show();
                }
            },
            'roleAdd button[itemid=save]': {
                click: function (btn) {
                    this.GridDoActionUtil.doInsert(this.getRoleList(), this.getRoleAddForm(), this.getRoleAddWin());
                }
            },
            'roleList button[itemid=updateButton]': {
                click: function (btn) {
                    var record = this.getRoleList().getSelectionModel().getSelection()[0];
                    var view = Ext.widget('roleEdit');
                    view.down('form').loadRecord(record);
                    view.show();
                }
            },
            'roleEdit button[itemid=save]': {
                click: function (btn) {
                    this.GridDoActionUtil.doUpdate(this.getRoleList(), this.getRoleEditForm(), this.getRoleEditWin());
                }
            },
            'roleList button[itemid=deleteButton]': {
                click: function (btn) {
                    var grid = btn.up("roleList");
                    this.GridDoActionUtil.doDelete(grid, 'roleName',this.getRoleDeleteBtn());
                }
            }
        });
    }
})
