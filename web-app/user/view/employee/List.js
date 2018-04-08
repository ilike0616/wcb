/**
 * Created by shqv on 2014-6-16.
 */
Ext.define('user.view.employee.List', {
    extend: 'public.BaseList',
    alias: 'widget.employeeList',
    autoScroll: true,
    store: Ext.create('user.store.EmployeeSubStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'name',
    viewId : 'EmployeeList'
});