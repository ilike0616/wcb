/**
 * Created by guozhen on 2015/4/2.
 */
Ext.define("admin.store.EmployeeTreeStore",{
    extend : 'Ext.data.TreeStore',
    model : 'admin.model.EmployeeModel',
    proxy : {
        type : 'ajax',
        api:{
            read: 'employee/treeListForAdmin'
        },
        reader : {
            type : 'json',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    folderSort : true,
    listeners: {
        exception: function(proxy, type, action, options, res){
            Ext.Msg.show({
                title: 'ERROR',
                msg: res.message,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.Msg.OK
            });
        }
    },
    root: {
        expanded: true,
        name: "员工管理"
    }
})