Ext.define("user.view.taskAssigned.Report", {
    extend: 'public.BaseWin',
    alias: 'widget.taskAssignedReport',
    modal: true,
    title: '回复',
    initComponent: function () {
        Ext.applyIf(this, {
            items: [
                {
                    xtype: 'baseForm',
                    paramColumns: 1,
                    items: [
                        {
                            fieldLabel: "完成进度",
                            name: "state",
                            xtype: "combo",
                            note: null,
                            autoSelect: true,
                            forceSelection: true,
                            typeAhead: true,
                            allowBlank: false,
                            beforeLabelTextTpl: required,
                            emptyText: "-- 请选择 --",
                            store: [
                                [1, "未开始"],
                                [2, "进行中"],
                                [3, "完成"],
                                [4, "关闭"]
                            ]
                        }, {
                            fieldLabel: '汇报：',
                            xtype: 'textarea',
                            name: 'content',
                            grow: true,
                            width: 500,
                            allowBlank: false,
                            beforeLabelTextTpl: required
                        }, {
                            fieldLabel: '附件：',
                            name: "photos",
                            xtype: "baseUploadField",
                            note: null,
                            colspan: 2,
                            hiddenName: "photos"
                        }, {
                            xtype: 'hiddenfield',
                            name: 'id'
                        }
                    ],
                    buttons: [{
                        text: '保存', itemId: 'report', iconCls: 'table_save'
                    }]
                }
            ]
        });
        this.callParent(arguments);
    }
})