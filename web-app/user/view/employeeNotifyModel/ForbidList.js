/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.view.employeeNotifyModel.ForbidList', {
    extend: 'public.BaseList',
    alias: 'widget.employeeNotifyModelForbidList',
    autoScroll: true,
    store:Ext.create('user.store.EmployeeNotifyModelForbidStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    enableSearchField:false,
    enableComplexQuery:false,
    alertName:'notifyModel.name',
    viewId:'EmployeeNotifyModelForbidList'
});