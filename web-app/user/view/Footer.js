Ext.define('user.view.Footer', {
    extend: 'Ext.Toolbar',
    xtype : 'pageFooter',
    ui   : 'sencha',
    cls  : 'wcb-custom-footer',
    height: 25,
    items: [ 
        '->',
        {
            xtype: 'component',
            html : '35CRM-最专业CRM运营服务平台'
        },'->'
    ]
});
