/**
 * Created by like on 2015/8/12.
 */
Ext.define("user.view.review.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.reviewAdd',
    requires: [
        'public.BaseComboBoxTree',
        'public.BaseForm'
    ],
    title: '添加领导评阅',
    items: [
        {
            xtype: 'baseForm',
            viewId:'ReviewAdd',
            buttons: [{
                text:'保存',itemId:'review_save',iconCls:'table_save',autoInsert:false,target:'reviewList'
            }]
        }
    ]
});