/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.contact.List', {
    extend: 'public.BaseList',
    alias: 'widget.contactList',
    autoScroll: true,
    store:Ext.create('user.store.ContactStore'),
    title: '联系人管理',
    split:true,
//    forceFit:true,
    renderLoad:true,
    viewId:'ContactList'
});