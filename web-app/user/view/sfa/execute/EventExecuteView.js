/**
 * Created by like on 2015/9/18.
 */
Ext.define("user.view.sfa.execute.EventExecuteView", {
    extend: 'public.BaseWin',
    alias: 'widget.sfaExecuteEventExecuteView',
    width:750,
    requires: [
        'public.BaseForm'
    ],
    title: '查看事件执行记录',
    items: [
        {
            xtype: 'baseForm',
            paramColumns:2,
            defaults:{
                readOnly:true
            },
            items: [
                {
                    xtype:'combo',
                    fieldLabel: "执行状态",
                    name:'state',
                    store:[
                        [1, '待执行'],
                        [2, '已执行'],
                        [3, '已禁止']
                    ]
                }, {
                    fieldLabel: "执行时间",
                    name: "executeDate",
                    xtype: "datetimefield",
                    format: "Y-m-d H:i:s"
                },{
                    xtype: 'fieldcontainer',
                    hideLabel: true,
                    layout: 'hbox',
                    colspan: 2,
                    defaults:{
                        readOnly:true
                    },
                    width:630,
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
                }, {
                    fieldLabel: "员工姓名",
                    name: "employees.name",
                    xtype: "textarea",
                    note: null,
                    width: 500,
                    colspan: 2
                },{
                    fieldLabel: "客户名称",
                    name: "customer.name",
                    xtype: "textfield"
                },{
                    xtype:'checkbox',
                    name:'isNotify',
                    fieldLabel: "是否提醒",
                    uncheckedValue: false,
                    inputValue: true,
                    colspan: 2
                },{
                    xtype:'textarea',
                    name:'notifyContent',
                    fieldLabel: "提醒",
                    colspan: 2,
                    width: 500,
                    grow: true
                },{
                    xtype:'checkbox',
                    name:'isSms',
                    fieldLabel: "是否发送短信",
                    uncheckedValue: false,
                    inputValue: true,
                    width:10
                },{
                    xtype: 'textarea',
                    name: 'smsContent',
                    fieldLabel: "短信",
                    colspan: 2,
                    width: 500,
                    grow: true
                },{
                    xtype:'checkbox',
                    name:'isEmail',
                    fieldLabel: "是否发送邮件",
                    uncheckedValue: false,
                    inputValue: true,
                    width:10
                },{
                    xtype: 'textfield',
                    name: 'emailSubject',
                    fieldLabel: "邮件主题",
                    colspan: 2,
                    width: 500,
                    grow: true
                },{
                    xtype: 'textarea',
                    name: 'emailContent',
                    fieldLabel: "邮件内容",
                    colspan: 2,
                    width: 500,
                    grow: true
                }
            ],
            buttons:[{
                text:'关闭',iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }]
        }
    ]
});