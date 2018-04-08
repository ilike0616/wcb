/**
 * Created by like on 2015-01-13.
 */
Ext.define('user.view.loginLog.List', {
    extend: 'public.BaseList',
    alias: 'widget.loginLogList',
    autoScroll: true,
    store:Ext.create('user.store.LoginLogStore'),
    title: '登录日志',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'LoginLogList'
});