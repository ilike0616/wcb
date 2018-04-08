/**
 * Created by like on 2015/7/7.
 */
Ext.define('user.view.fareClaims.List', {
    extend: 'public.BaseList',
    alias: 'widget.fareClaimsList',
    autoScroll: true,
    store:Ext.create('user.store.FareClaimsStore'),
    title: '费用报销',
    split:true,
    forceFit:true,
    renderLoad:true,
    alertName:'subject',
    viewId:'FareClaimsList'
});