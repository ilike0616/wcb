Ext.define("user.view.attendanceModel.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.attendanceModelAdd',
    modal: true,
    width: 700,
    layout: 'anchor',
    title: '添加考勤模板',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:10px 10px 10px',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 200,
                        labelAlign: 'right'
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                        fieldLabel : '模板名称',
                        name : 'name',
                        width: 500,
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        xtype:'fieldcontainer',
                        fieldLabel:'位置',
                        itemId:'locationContainer',
                        layout:{
                            type:'hbox',
                            columns:2
                        },
                        fieldDefaults:{
                            height:24,
                            margin:'0 10 5 0'
                        },
                        items:[{
                            xtype:'textfield',
                            name : 'location',
                            width:295
                        },{
                            xtype:'button',
                            text:'获取地址',
                            itemId:'locationButton',
                            width:80
                        }]
                    },{
                        xtype:'numberfield',
                        fieldLabel : '最大允许偏移距离(米)',
                        name : 'maxDistance',
                        value: 500
                    },{
                        xtype:'checkboxfield',
                        fieldLabel : '超过最大偏移距离仍允许签到',
                        name : 'ifMaxDistSign',
                        boxLabelAlign:'before',
                        inputValue:true,
                        uncheckedValue:false
                    },{
                        xtype: 'radiogroup',
                        fieldLabel: '时间模式',
                        width:450,
                        items: [
                            { boxLabel: '单时间模式', name: 'timeMode', inputValue: '1', checked: true},
                            { boxLabel: '双时间模式', name: 'timeMode', inputValue: '2' }
                        ],
                        listeners:{
                            change:function(o, newValue, oldValue, eOpts){
                                if(newValue.timeMode == 2){
                                    me.down('fieldcontainer#timeMode2Container').show();
                                }else{
                                    me.down('fieldcontainer#timeMode2Container').hide();
                                }
                                me.doLayout();
                            }
                        }
                    },{
                        xtype:'fieldcontainer',
                        fieldLabel:'时段1',
                        itemId:'timeMode1Container',
                        layout:{
                            type:'hbox',
                            columns:3
                        },
                        fieldDefaults : {
                            height:24
                        },
                        items:[{
                            xtype:'timefield',
                            format: 'H:i',
                            submitFormat:'H:i',
                            name:'startTime1',
                            emptyText:'例 09:00',
                            allowBlank : false
                        },{
                            xtype:'label',
                            text:'~',
                            width:10
                        },{
                            xtype:'timefield',
                            format: 'H:i',
                            submitFormat:'H:i',
                            name:'endTime1',
                            emptyText:'例 12:00',
                            allowBlank : false
                        }]
                    },{
                        xtype:'fieldcontainer',
                        fieldLabel:'时段2',
                        itemId:'timeMode2Container',
                        hidden:true,
                        layout:{
                            type:'hbox',
                            columns:3
                        },
                        fieldDefaults : {
                            height:24
                        },
                        items:[{
                            xtype:'timefield',
                            format: 'H:i',
                            submitFormat:'H:i',
                            name:'startTime2',
                            emptyText:'例 13:00'
                        },{
                            xtype:'label',
                            text:'~',
                            width:10
                        },{
                            xtype:'timefield',
                            format: 'H:i',
                            submitFormat:'H:i',
                            name:'endTime2',
                            emptyText:'例 18:00'
                        }]
                    },{
                        xtype:'numberfield',
                        fieldLabel : '最大迟到分钟数',
                        name : 'maxLateTime',
                        value: 10
                    },{
                        xtype:'numberfield',
                        fieldLabel : '最大早退分钟数',
                        name : 'maxEarlyWork',
                        value: 10
                    },{
                        xtype:'checkboxfield',
                        fieldLabel : '必须拍照',
                        name : 'havePictures',
                        boxLabelAlign:'before',
                        inputValue:true,
                        uncheckedValue:false
                    }/**,{
                        xtype:'checkboxfield',
                        fieldLabel : '是否允许其他设备签到',
                        name : 'otherEquSign',
                        boxLabelAlign:'before',
                        inputValue:true,
                        uncheckedValue:false
                    }**/,{
                        xtype:'textfield',
                        fieldLabel:'考勤统计生成日',
                        name:'statDay'
                    },{
                        xtype:'combo',
                        fieldLabel:'考勤日历',
                        name:'attendanceCalendar.id',
                        store: Ext.create('user.store.AttendanceCalendarStore'),
                        displayField: 'name',
                        valueField: 'id'
                    },{
                        xtype:'fieldcontainer',
                        fieldLabel:'设置工作日',
                        itemsId:'workDayContainer',
                        margin:'0 10 0 0',
                        width:650,
                        layout:{
                            type:'hbox',
                            columns:7
                        },
                        fieldDefaults:{
                            margin:'0 10 0 0'
                        },
                        items:[{
                            xtype:'checkboxfield',
                            boxLabel : '周一',
                            name : 'monday',
                            inputValue:true,
                            uncheckedValue:false,
                            checked:true
                        },{
                            xtype:'checkboxfield',
                            boxLabel : '周二',
                            name : 'tuesday',
                            inputValue:true,
                            uncheckedValue:false,
                            checked:true
                        },{
                            xtype:'checkboxfield',
                            boxLabel : '周三',
                            name : 'wednesday',
                            inputValue:true,
                            uncheckedValue:false,
                            checked:true
                        },{
                            xtype:'checkboxfield',
                            boxLabel : '周四',
                            name : 'thursday',
                            inputValue:true,
                            uncheckedValue:false,
                            checked:true
                        },{
                            xtype:'checkboxfield',
                            boxLabel : '周五',
                            name : 'friday',
                            inputValue:true,
                            uncheckedValue:false,
                            checked:true
                        },{
                            xtype:'checkboxfield',
                            boxLabel : '周六',
                            name : 'saturday',
                            inputValue:true,
                            uncheckedValue:false
                        },{
                            xtype:'checkboxfield',
                            boxLabel : '周日',
                            name : 'sunday',
                            inputValue:true,
                            uncheckedValue:false
                        }]
                    },{
                        xtype:'numberfield',
                        fieldLabel : '提前N分钟通知签到/签退',
                        name : 'earlyNotifyMinutes',
                        value: 5,
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        xtype:'textarea',
                        fieldLabel : '提醒内容',
                        name:'notifyMessage',
                        width:500,
                        grow: true,
                        colspan:2,
                        allowBlank : false,
                        beforeLabelTextTpl: required
                    },{
                        xtype:'hiddenfield',
                        name : 'longtitude'
                    },{
                        xtype:'hiddenfield',
                        name : 'latitude'
                    }
                    ],
                    buttons: [{
                        text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:'attendanceModelList'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})