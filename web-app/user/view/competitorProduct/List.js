Ext.define('user.view.competitorProduct.List', {
    extend: 'public.BaseList',
    alias: 'widget.competitorProductList',
    autoScroll: true,
    store:Ext.create('user.store.CompetitorProductStore'),
    title: '竞争产品',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'CompetitorProductList'
});