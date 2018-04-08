/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.view.employeeNotifyModel.List', {
    extend: 'public.BaseList',
    alias: 'widget.employeeNotifyModelList',
    autoScroll: true,
    store:Ext.create('user.store.EmployeeNotifyModelStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    enableSearchField:false,
    enableComplexQuery:false,
    alertName:'notifyModel.name',
    viewId:'EmployeeNotifyModelList'
});