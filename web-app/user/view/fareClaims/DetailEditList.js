Ext.define('user.view.fareClaims.DetailEditList', {
    extend: 'public.BaseEditList',
    alias: 'widget.fareClaimsDetailEditList',
    autoScroll: true,
    store:Ext.create('user.store.FareClaimsDetailStore'),
    title: '报销明细',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'FareClaimsDetailEditList'
});