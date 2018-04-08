/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.publicCustomer.List', {
    extend: 'public.BaseList',
    alias: 'widget.publicCustomerList',
    autoScroll: true,
    store:Ext.create('user.store.PublicCustomerStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'PublicCustomerList'
});