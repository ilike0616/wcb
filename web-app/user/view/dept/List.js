/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('user.view.dept.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.deptList',
    autoScroll: true,
    store:Ext.create('user.store.DeptStore'),
    title: '部门管理',
    forceFit:true,
    reserveScrollbar: true,
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop',
            displayField : 'name',
            containerScroll: true
        }
    },
    showRowNumber : false,
    renderLoad:true,
    viewId:'DeptList'
});