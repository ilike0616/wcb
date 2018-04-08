/**
 * Created by like on 2015/8/27.
 */
Ext.define('user.view.sfa.event.List', {
    extend: 'public.BaseWin',
    alias: 'widget.sfaEventList',
    width: 950,
    height: 450,
    modal : true,
    layout: 'border',
    title:'方案事件',
    initComponent: function() {
        var me = this;
            Ext.applyIf(me, {
                items: [
                    {
                        region:'center',
                        border : 0,
                        items : [{
                            xtype:'baseList',
                            autoScroll: true,
                            enableComplexQuery:false,
                            margin : '5 5 0 5',
                            height: 360,
                            overflowY: 'auto',
                            store : Ext.create('user.store.SfaEventStore'),
                            tbar:[
                                {xtype:'button',text:'新增',itemId:'addButton',iconCls:'table_add'},
                                {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
                                {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true,autoDelete:true,subject:'name'},
                                {xtype:'button',text:'上移',itemId:'up',iconCls:'Arrowup',disabled:true,autodisabled:true},
                                {xtype:'button',text:'下移',itemId:'down',iconCls:'Arrowdown',disabled:true,autodisabled:true},
                                {xtype:'button',text:'保存',itemId:'save',iconCls:'Disk'},
                            ],
                            columns:[
                                {
                                    xtype: 'rownumberer',
                                    width: 30,
                                    sortable: false
                                },
                                {
                                    text:'事件',
                                    dataIndex:'name'
                                },{
                                    text:'通知对象',
                                    dataIndex:'receiverType',
                                    xtype: "rowselecter",
                                    arry: [
                                        [1, '所有者'],
                                        [2,'客户'],
                                        [3,'相关员工']
                                    ]
                                },{
                                    text:'执行类型',
                                    dataIndex:'dateType',
                                    xtype: "rowselecter",
                                    arry: [
                                        [1, '绝对日期'],
                                        [2,'相对日期'],
                                        [3,'循环执行']
                                    ]
                                },{
                                    text:'执行日期',
                                    xtype:'templatecolumn',
                                    width:440,
                                    tpl:new Ext.XTemplate(
                                        '<tpl if="dateType == 1">',
                                            '{startDate:this.formatDate}到{endDate:this.formatDate}',
                                        '</tpl>',
                                        '<tpl if="dateType == 2">',
                                            '从<b><tpl for="dateField">{text}</tpl></b><tpl if="beforeEnd==1">前</tpl><tpl if="beforeEnd==2">后</tpl>',
                                            '<b>{difftime}</b><tpl if="diffPeriod==1">日</tpl><tpl if="diffPeriod==2">月</tpl><tpl if="diffPeriod==3">年</tpl>开始,持续<b>{lastingDays}</b>天',
                                        '</tpl>',
                                        '<tpl if="dateType == 3">',
                                            '从<b><tpl for="dateFieldCycle">{text}</tpl></b><tpl if="beforeEndCycle==1">前</tpl><tpl if="beforeEndCycle==2">后</tpl>',
                                            '<b>{difftimeCycle}</b><tpl if="diffPeriodCycle==1">日</tpl><tpl if="diffPeriodCycle==2">月</tpl><tpl if="diffPeriodCycle==3">年</tpl>开始,',
                                            '每<b>{interval}</b><tpl if="intervalPeriod==1">日</tpl><tpl if="intervalPeriod==2">月</tpl><tpl if="intervalPeriod==3">年</tpl>执行一次,',
                                            '持续<b>{lastingDaysCycle}</b>天,共循环<b>{cycleTimes}</b>次',
                                        '</tpl>', {
                                            formatDate: function(value) {
                                                if (!value) {
                                                    return '';
                                                }
                                                return Ext.Date.format(value, 'Y-m-d H:i');
                                            }
                                        }
                                    )
                                },{
                                    text:'创建时间',
                                    dataIndex:'dateCreated',
                                    xtype: "datecolumn",
                                    width:130,
                                    format: "Y-m-d H:i"
                                }
                            ]
                        }]
                    }
                ],
                buttons: [
                    {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
                ]
            })
        me.callParent(arguments);
    },
    closeWin : function(o, e, eOpts){
        this.close();
    }
});