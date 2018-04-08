/**
 * Created by like on 2015/5/26.
 */
Ext.define("user.view.employeeCheck.Main", {
    extend: 'Ext.panel.Panel',
    alias: 'widget.employeeCheckMain',
    title: '账号认证',
    layout: {
        align: 'center',
        pack: 'center',
        type: 'vbox'
    },
    items: [
        {
            xtype: 'tbfill'
        }, {
            xtype: 'employeeCheckEmail'
        },{
            xtype:'panel',
            title: '邮箱验证',
            itemId:'emailChecked',
            width:415,
            height:100,
            hidden:true,
            html:'<p id="email">&nbsp;</p>'
        }, {
            xtype: 'tbfill',
            height: 50
        }, {
            xtype: 'employeeCheckMobile'
        },{
            xtype:'panel',
            title: '手机验证',
            itemId:'mobileChecked',
            width:415,
            height:100,
            hidden:true,
            html:'<p id="mobile">&nbsp;</p>'
        }, {
            xtype: 'tbfill'
        }
    ]
})