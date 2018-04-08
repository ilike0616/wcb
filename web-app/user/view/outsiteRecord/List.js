/**
 * Created by guozhen on 2014-7-31.
 */
Ext.define('user.view.outsiteRecord.List', {
    extend: 'public.BaseList',
    alias: 'widget.outsiteRecordList',
    autoScroll: true,
    store:Ext.create('user.store.OutsiteRecordStore'),
    title: '现场记录',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'OutsiteRecordList'
});