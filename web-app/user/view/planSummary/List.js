/**
 * Created by guozhen on 2014-11-13.
 */
Ext.define('user.view.planSummary.List', {
    extend: 'public.BaseList',
    alias: 'widget.planSummaryList',
    autoScroll: true,
    store:Ext.create('user.store.PlanSummaryStore'),
    title: '计划总结',
    split:true,
    forceFit:true,
    viewId:'PlanSummaryList'
});