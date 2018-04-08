/**
 * Created by like on 2015/7/7.
 */
Ext.define('user.view.fareClaims.DetailViewList', {
    extend: 'public.BaseEditList',
    alias: 'widget.fareClaimsDetailViewList',
    autoScroll: true,
    store:Ext.create('user.store.FareClaimsDetailStore'),
    title: '报销明细',
    split:true,
    forceFit:true,
    renderLoad:true,
    enableEdit:false,
    viewId:'FareClaimsDetailViewList'
});