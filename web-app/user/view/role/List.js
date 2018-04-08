/**
 * Created by guozhen on 2014-07-04.
 */
Ext.define('user.view.role.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.roleList',
    autoScroll: true,
    store:Ext.create('user.store.RoleStore'),
    title: '用户角色',
    forceFit:true,
    reserveScrollbar: true,
    showRowNumber : false,
    renderLoad:true,
    viewId:'RoleList'
});