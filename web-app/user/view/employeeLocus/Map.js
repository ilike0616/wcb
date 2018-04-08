/**
 * Created by like on 2015-04-07.
 */
Ext.define("user.view.EmployeeLocus.Map",{
    extend: 'public.BaseMap',
    alias: 'widget.employeeLocusMap',
    store: Ext.create('user.store.EmployeeLocusStore'),
    title: '地图'
})