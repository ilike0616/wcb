Ext.define('user.view.locus.List', {
    extend: 'public.BaseList',
    alias: 'widget.locusList',
    autoScroll: true,
    store:Ext.create('user.store.LocusStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'LocusList'
});