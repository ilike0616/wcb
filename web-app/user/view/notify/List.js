/**
 * Created by like on 2015-04-22.
 */
Ext.define('user.view.notify.List', {
    extend: 'public.BaseList',
    alias: 'widget.notifyList',
    autoScroll: true,
    store:Ext.create('user.store.NotifyStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'NotifyList',
    viewConfig:{
        getRowClass: function(record){
            return record.get("isRead") == 0 ? "boldFont" : "";
        }
    }
});