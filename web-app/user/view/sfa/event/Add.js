/**
 * Created by like on 2015/8/31.
 */
Ext.define("user.view.sfa.event.Add", {
    extend: 'public.BaseWin',
    alias: 'widget.sfaEventAdd',
    width:750,
    requires: [
        'public.BaseForm'
    ],
    title: '添加事件',
    items: [
        {
            xtype: 'baseForm',
            paramColumns:2,
            items: [
                {
                    xtype: "hidden",
                    name: "sfa"
                },
                {
                    xtype: "textfield",
                    name: "name",
                    fieldLabel: "事件名称",
                    allowBlank:false,
                    beforeLabelTextTpl: required,
                    width:700,
                    colspan: 2
                },{
                    xtype:"fieldset",
                    title: '执行时间',
                    colspan: 2,
                    width:710,
                    collapsible: true,
                    defaultType: 'textfield',
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    border: 1,
                    items :[
                        {
                            xtype: 'fieldcontainer',
                            hideLabel: true,
                            layout: 'hbox',
                            items: [{
                                xtype: 'radio',
                                name:'dateType',
                                hideLabel: true,
                                inputValue:1,
                                checked:true,
                                width:25
                            }, {
                                xtype: 'label',
                                text: '绝对日期:',
                                style: {
                                            color:'red'
                                        },
                                margin:'5 0 0 0',
                                width:70
                            }, {
                                fieldLabel: "开始时间",
                                labelWidth:70,
                                name: "startDate",
                                xtype: "datetimefield",
                                width:220,
                                note: null,
                                format: "Y-m-d H:i:s",
                                submitFormat: "Y-m-d H:i:s"
                            }, {
                                fieldLabel: "结束时间",
                                labelWidth:70,
                                name: "endDate",
                                xtype: "datetimefield",
                                width:220,
                                note: null,
                                format: "Y-m-d H:i:s",
                                submitFormat: "Y-m-d H:i:s"
                            }]
                        },{//==========================相对日期开始===========================
                            xtype: 'fieldcontainer',
                            hideLabel: true,
                            layout: 'hbox',
                            items: [{
                                xtype: 'radio',
                                name:'dateType',
                                hideLabel: true,
                                inputValue:2,
                                width:25
                            }, {
                                xtype: 'label',
                                text: '相对日期:',
                                style: {
                                    color:'red'
                                },
                                margin:'5 0 0 0',
                                width:70
                            },{
                                xtype:'combo',
                                name:'dateField',
                                fieldLabel:'基准日期',
                                labelWidth:70,
                                width:190,
                                store:Ext.create('user.store.UserFieldStore'),
                                forceSelection : true,
                                displayField: 'text',
                                valueField: 'id'
                            },{
                                xtype: 'label',
                                text: '后',
                                margin:'5 0 0 0',
                                width:15
                            },{
                                xtype:'radio',
                                name:'beforeEnd',
                                hideLabel: true,
                                inputValue:2,
                                checked:true,
                                width:20
                            },{
                                xtype: 'label',
                                text: '(前',
                                margin:'5 0 0 0',
                                width:20
                            },{
                                xtype:'radio',
                                name:'beforeEnd',
                                inputValue:1,
                                hideLabel: true,
                                width:20
                            },{
                                xtype: 'label',
                                text: ')',
                                margin:'5 0 0 0',
                                width:10
                            },{
                                xtype:'textfield',
                                hideLabel: true,
                                width:50,
                                name:'difftime'
                            },{
                                xtype:'combo',
                                name:'diffPeriod',
                                hideLabel: true,
                                forceSelection : true,
                                margin:'0 0 0 2',
                                width:50,
                                value:1,
                                store:[
                                    [1,'日'],
                                    [2,'月'],
                                    [3,'年']
                                ]
                            },{
                                xtype: 'label',
                                text: ',开始执行,',
                                margin:'5 0 0 0',
                                width:123
                            }]
                        },{
                            xtype: 'fieldcontainer',
                            hideLabel: true,
                            layout: 'hbox',
                            items: [{
                                xtype: 'label',
                                text: '持续',
                                margin:'5 0 0 130',
                                width:40
                            },{
                                xtype:'textfield',
                                hideLabel: true,
                                width:50,
                                name:'lastingDays'
                            },{
                                xtype: 'label',
                                text: '天,结束执行。',
                                margin:'5 0 0 0',
                                width:102
                            }]
                        },{//==========================循环执行开始===========================
                            xtype: 'fieldcontainer',
                            hideLabel: true,
                            layout: 'hbox',
                            items: [{
                                xtype: 'radio',
                                name:'dateType',
                                hideLabel: true,
                                inputValue:3,
                                width:25
                            }, {
                                xtype: 'label',
                                text: '循环执行:',
                                style: {
                                    color:'red'
                                },
                                margin:'5 0 0 0',
                                width:70
                            },{
                                xtype:'combo',
                                name:'dateFieldCycle',
                                fieldLabel:'基准日期',
                                labelWidth:70,
                                width:190,
                                store:Ext.create('user.store.UserFieldStore'),
                                forceSelection : true,
                                displayField: 'text',
                                valueField: 'id'
                            },{
                                xtype: 'label',
                                text: '后',
                                margin:'5 0 0 0',
                                width:15
                            },{
                                xtype:'radio',
                                name:'beforeEndCycle',
                                hideLabel: true,
                                inputValue:2,
                                checked:true,
                                width:20
                            },{
                                xtype: 'label',
                                text: '(前',
                                margin:'5 0 0 0',
                                width:20
                            },{
                                xtype:'radio',
                                name:'beforeEndCycle',
                                inputValue:1,
                                hideLabel: true,
                                width:20
                            },{
                                xtype: 'label',
                                text: ')',
                                margin:'5 0 0 0',
                                width:10
                            },{
                                xtype:'textfield',
                                hideLabel: true,
                                width:50,
                                name:'difftimeCycle'
                            },{
                                xtype:'combo',
                                name:'diffPeriodCycle',
                                hideLabel: true,
                                forceSelection : true,
                                margin:'0 0 0 2',
                                width:50,
                                value:1,
                                store:[
                                    [1,'日'],
                                    [2,'月'],
                                    [3,'年']
                                ]
                            },{
                                xtype: 'label',
                                text: ',开始执行,',
                                margin:'5 0 0 0',
                                width:123
                            }]
                        },{
                            xtype: 'fieldcontainer',
                            hideLabel: true,
                            layout: 'hbox',
                            items: [{
                                xtype: 'label',
                                text: '每',
                                margin:'5 0 0 130',
                                width:15
                            },{
                                xtype:'textfield',
                                hideLabel: true,
                                width:50,
                                name:'interval'
                            },{
                                xtype:'combo',
                                name:'intervalPeriod',
                                hideLabel: true,
                                forceSelection : true,
                                margin:'0 0 0 2',
                                width:50,
                                value:1,
                                store:[
                                    [1,'日'],
                                    [2,'月'],
                                    [3,'年']
                                ]
                            },{
                                xtype: 'label',
                                text: '执行一次,每次持续',
                                margin:'5 0 0 0',
                                width:132
                            },{
                                xtype:'textfield',
                                hideLabel: true,
                                width:50,
                                name:'lastingDaysCycle'
                            },{
                                xtype: 'label',
                                text: '天结束,共循环',
                                margin:'5 0 0 0',
                                width:105
                            },{
                                xtype:'textfield',
                                hideLabel: true,
                                width:50,
                                name:'cycleTimes'
                            },{
                                xtype: 'label',
                                text: '次。',
                                margin:'5 0 0 0',
                                width:15
                            }]
                        },{
                            xtype: 'fieldcontainer',
                            hideLabel: true,
                            layout: 'hbox',
                            items: [{
                                xtype: 'label',
                                text: '当日执行时间：',
                                margin:'5 0 0 0',
                                width:110
                            },{
                                xtype:'numberfield',
                                name:'timeHour',
                                hideLabel: true,
                                maxValue: 23,
                                minValue: 0,
                                width:50
                            },{
                                xtype: 'label',
                                text: '时',
                                margin:'5 0 0 0',
                                width:15
                            },{
                                xtype:'numberfield',
                                name:'timeMinute',
                                hideLabel: true,
                                maxValue: 59,
                                minValue: 0,
                                width:50
                            },{
                                xtype: 'label',
                                text: '分,若不选择则以基准日期的时间为准。',
                                margin:'5 0 0 0',
                                width:280
                            }]
                        }
                    ]
                },{
                    xtype:"fieldset",
                    title: '执行动作',
                    colspan: 2,
                    width:710,
                    collapsible: true,
                    defaultType: 'textfield',
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    border: 1,
                    items :[{
                        xtype: 'fieldcontainer',
                        hideLabel: true,
                        layout: 'hbox',
                        items: [{
                            xtype: 'label',
                            text: '通知对象：',
                            margin:'5 0 0 0',
                            width:80
                        },{
                            xtype:'radio',
                            name:'receiverType',
                            inputValue:1,
                            checked:true,
                            fieldLabel:'所有者',
                            labelWidth:60,
                            width:100
                        },{
                            xtype:'radio',
                            name:'receiverType',
                            inputValue:2,
                            fieldLabel:'客户',
                            labelWidth:50,
                            width:100
                        },{
                            xtype:'radio',
                            name:'receiverType',
                            inputValue:3,
                            fieldLabel:'相关员工',
                            labelWidth:80,
                            width:100
                        }]
                    },{
                            fieldLabel: "员工姓名",
                            name: "acceptors.name",
                            xtype: "baseMultiSelectTextareaField",
                            note: null,
                            viewId: "EmployeeList",
                            store: "EmployeeStore",
                            hiddenName: "acceptors",
                            colspan: 2
                    },{
                        xtype:'checkbox',
                        name:'isNotify',
                        fieldLabel: "是否提醒",
                        uncheckedValue: false,
                        inputValue: true,
                        width:10
                    },{
                        xtype:'baseTemplateTextAreaField',
                        name:'notifyContentTemplate',
                        fieldLabel: "提醒",
                        storeId: "user.store.UserFieldStore",
                        margin:'5 0 0 0'
                    },{
                        xtype:'checkbox',
                        name:'isSms',
                        fieldLabel: "是否发送短信",
                        uncheckedValue: false,
                        inputValue: true,
                        width:10
                    },{
                        xtype: 'baseTemplateTextAreaField',
                        name: 'smsContentTemplate',
                        fieldLabel: "短信",
                        storeId: "user.store.UserFieldStore",
                        margin:'5 0 0 0'
                    },{
                        xtype:'checkbox',
                        name:'isEmail',
                        fieldLabel: "是否发送邮件",
                        uncheckedValue: false,
                        inputValue: true,
                        width:10
                    },{
                        xtype: 'baseTemplateTextAreaField',
                        name: 'emailSubjectTemplate',
                        fieldLabel: "邮件主题",
                        storeId: "user.store.UserFieldStore",
                        margin:'5 0 0 0',
                        height:25
                    },{
                        xtype: 'baseTemplateTextAreaField',
                        name: 'emailContentTemplate',
                        fieldLabel: "邮件内容",
                        storeId: "user.store.UserFieldStore",
                        width:600,
                        margin:'5 0 5 0'
                    }]
                },{
                    xtype:'textarea',
                    name:'remark',
                    fieldLabel: "备注",
                    colspan: 2,
                    width: 700,
                    grow: true
                }
            ],
            buttons: [
                {
                    text: '保存', itemId: 'save', iconCls: 'table_save', autoInsert: false, target: 'sfaEventDetail'
                }
            ]
        }
    ]
});