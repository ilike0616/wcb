/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.customer.List', {
    extend: 'public.BaseList',
    alias: 'widget.customerList',
    autoScroll: true,
    store:Ext.create('user.store.CustomerStore'),
    split:true,
    //forceFit:true,
    renderLoad:true,
    //stateful: true,
    //stateId: "customerList",
    viewId:'CustomerList'
});