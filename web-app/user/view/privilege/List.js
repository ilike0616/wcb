/**
 * Created by guozhen on 2014-6-16.
 */
Ext.define('user.view.privilege.List', {
    extend: 'public.BaseList',
    alias: 'widget.privilegeList',
    autoScroll: true,
    store:Ext.create('user.store.PrivilegeStore'),
    title: '权限管理',
    split:true,
    forceFit:true,
    enableBasePaging : false,
    renderLoad:true,
    viewId:'PrivilegeList'
});