Ext.define("agent.view.user.Edit",{
    extend: 'Ext.window.Window',
    alias: 'widget.userEdit',
    modal: true,
    width: 600,
    layout: 'anchor',
    title: '修改企业信息',
    items: [{
        xtype: 'form',
        layout: 'column',
        defaults: {
            msgTarget: 'side',
            xtype: 'textfield',
            padding:'10 5 10 5',
            beforeLabelTextTpl: required
        },
        items: [{
            fieldLabel: '企业账号',
            name: 'userId',
            allowBlank : false,
            columnWidth: 1/2
        },{
            fieldLabel: '企业名称',
            name: 'name',
            allowBlank : false,
            columnWidth: 1/2
        },{
            xtype:'hiddenfield',
            name:'id'
        }]
    }],
    buttons: [{
        text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:'userList'
    }]
})