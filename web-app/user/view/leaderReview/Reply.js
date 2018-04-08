/**
 * Created by like on 2015/8/13.
 */
Ext.define("user.view.leaderReview.Reply",{
    extend: 'public.BaseWin',
    alias: 'widget.leaderReviewReply',
    modal: true,
    width: 400,
    layout: 'anchor',
    title: '回复',
    initComponent: function() {
        Ext.applyIf(this, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'column'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width : 330
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textarea'
                    },
                    items: [{
                        fieldLabel : '回复：',
                        name : 'content',
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    }
                    ],
                    buttons: [{
                        text:'保存',itemId:'reply',iconCls:'table_save'
                    }]
                }
            ]
        });
        this.callParent(arguments);
    }
});