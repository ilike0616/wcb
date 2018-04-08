Ext.define('user.view.competitor.List', {
    extend: 'public.BaseList',
    alias: 'widget.competitorList',
    autoScroll: true,
    store:Ext.create('user.store.CompetitorStore'),
    title: '竞争对手',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'CompetitorList'
});