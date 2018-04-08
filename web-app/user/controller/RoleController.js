/**
 * Created by guozhen on 2014/6/23.
 */

Ext.define('user.controller.RoleController',{
    extend : 'Ext.app.Controller',
    views:['role.Main','role.List','role.Add','role.Edit'] ,
    stores:['RoleStore','RoleStoreForEdit'],
    models:['RoleModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'roleList',
            selector : 'roleList'
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
        this.application.getController("PrivilegeController");
        this.control({
            'roleList': {
                select: function (o, record, index, eOpts) {
                    var grid = o.view.headerCt.grid;
                    grid.query('button[itemid=deleteButton]')[0].setDisabled(false);
                    grid.query('button[itemid=updateButton]')[0].setDisabled(false);

                },
                deselect: function( o,record, index, eOpts){
                    var grid = o.view.headerCt.grid;
                    grid.query('button[itemid=deleteButton]')[0].setDisabled(true);
                    grid.query('button[itemid=updateButton]')[0].setDisabled(true);
                }
            },
            'roleList button[itemid=addButton]': {
                click: function (btn) {
                    var view = Ext.widget('roleAdd');
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
                    var record = btn.ownerCt.ownerCt.getSelectionModel().getSelection()[0];
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
