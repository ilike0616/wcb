Ext.define('agent.view.Login',{
	extend:'Ext.window.Window',
	alias:'widget.login',
	autoShow:true,
//	height:170,
	width:360,
	layout:{
		type:'fit'
	},
	
	iconCls:'key',
	title:'代理商登陆',
	closeAction:'hide',
	closeable:false,
	items: [
        {
            xtype: 'form',
            frame: false,
            bodyPadding: 15,
            defaults: {
                xtype: 'textfield',
                anchor: '100%',
                labelWidth: 60,
//                allowBlank: false,
//                minLength: 3,
                msgTarget: 'under'
            },
            fieldDefaults: {
                labelAlign:'right'
            },
            items: [
                {
					xtype: 'textfield',
                    name: 'agentId',
                    fieldLabel: '帐号',
                    value: 'agent',
                    vtype:''
                },
                {
                    inputType: 'password',
                    name: 'password',
                    fieldLabel: '密码',
                    enableKeyEvents: true,
                    value: 'agent',
                    //vtype: 'customPass',
                    msgTarget: 'side'
                },{
                    xtype:'hiddenfield',
                    name: 'clientType'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    items: [
                        {
                            xtype: 'tbfill'
                        },
                        {
                            xtype: 'button',
                            itemId: 'cancel',
                            iconCls: 'cancel',
                            text: '取消'
                        },
                        {
                            xtype: 'button',
                            itemId: 'submit',
                            formBind: true,
                            iconCls: 'key-go',
                            text: '登陆'
                        }
                    ]
                }
            ]
        }
    ]
});