Ext.define('user.view.contact.Deatil', {
    extend: 'public.BaseEditList',
    alias: 'widget.contactDetail',
    autoScroll: true,
    store:Ext.create('user.store.ContactStore'),
    title: '联系人管理',
    split:true,
//    forceFit:true,
    renderLoad:true,
    viewId:'ContactList'
});