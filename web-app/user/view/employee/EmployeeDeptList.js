/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('user.view.employee.EmployeeDeptList', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.employeeDeptList',
    autoScroll: true,
    store: Ext.create('user.store.DeptStore'),
    title: '部门管理',
    forceFit:true,
    reserveScrollbar: true,
    rootVisible : false,
    tools:[{
        type:'refresh',
        tooltip: '刷新',
        handler: function(event, toolEl, panelHeader) {
            var refresh = this;
            refresh.setDisabled(true);
            var treePanel = this.up('treepanel');
            Ext.create("admin.util.GridDoActionUtil").loadStore(treePanel);
            Ext.defer(function(){
                refresh.setDisabled(false);
            },10000);
        }
    }],
    columns: [{
        xtype: 'treecolumn',
        text: '部门名称',
        dataIndex: 'name'
    }]
});