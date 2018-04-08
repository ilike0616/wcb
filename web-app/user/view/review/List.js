/**
 * Created by like on 2015/8/12.
 */
Ext.define('user.view.review.List', {
    extend: 'public.BaseList',
    alias: 'widget.reviewList',
    autoScroll: true,
    store:Ext.create('user.store.ReviewStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'ReviewList',
    viewConfig:{
        getRowClass: function(record){
            return record.get("isRead") == 0 ? "boldFont" : "";
        }
    }
});