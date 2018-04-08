Ext.define('user.view.competitorDynamic.List', {
    extend: 'public.BaseList',
    alias: 'widget.competitorDynamicList',
    autoScroll: true,
    store:Ext.create('user.store.CompetitorDynamicStore'),
    title: '对手动态',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'content',
    viewId:'CompetitorDynamicList'
});