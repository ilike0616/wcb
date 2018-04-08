/**
 * Created by guozhen on 2014/6/25.
 */
Ext.define('admin.view.dept.DeptUserList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.deptUserList',
    autoScroll: true,
    store: 'UserStore',
    title: '用户管理',
    split:true,
    columns:[
        {
            text:'用户名称',
            width : '95%',
            dataIndex:'name'
        }
    ]
});