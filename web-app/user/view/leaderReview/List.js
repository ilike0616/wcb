/**
 * Created by like on 2015/8/13.
 */
Ext.define('user.view.leaderReview.List', {
    extend: 'public.BaseList',
    alias: 'widget.leaderReviewList',
    autoScroll: true,
    store:Ext.create('user.store.LeaderReviewStore'),
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'LeaderReviewList',
    viewConfig:{
        getRowClass: function(record){
            return record.get("isRead") == 0 ? "boldFont" : "";
        }
    }
});