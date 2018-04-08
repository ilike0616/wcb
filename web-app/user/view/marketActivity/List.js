Ext.define('user.view.marketActivity.List', {
    extend: 'public.BaseList',
    alias: 'widget.marketActivityList',
    autoScroll: true,
    store:Ext.create('user.store.MarketActivityStore'),
    title: '市场活动',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'MarketActivityList'
});