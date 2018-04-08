Ext.define('admin.view.Footer', {
    extend: 'Ext.Toolbar',
    xtype : 'pageFooter',
    ui   : 'sencha',
    height: 33,
    items: [
        '->',
        {
            xtype: 'component',
            cls  : 'x-footer',
            html : 'Copyright © 2013 上海傲融35CRM项目'
        },'->'
    ]
});
