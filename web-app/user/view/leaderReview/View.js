/**
 * Created by like on 2015/8/13.
 */
Ext.define("user.view.leaderReview.View",{
    extend: 'public.BaseWin',
    alias: 'widget.leaderReviewView',
    requires: [
        'public.BaseComboBoxTree',
        'public.BaseForm'
    ],
    items: [
        {
            xtype: 'baseForm',
            defaults:{
                readOnly:true
            },
            viewId:'LeaderReviewView',
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        }
    ]
});