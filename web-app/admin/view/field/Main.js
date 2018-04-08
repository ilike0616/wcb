/**
 * Created by shqv on 2014-8-29.
 */
Ext.define('admin.view.field.main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.fieldMain',
    layout: 'border',
    items: [
       /* {
            region:'west',
            title:'用户管理',
            xtype: 'userList',
            width: 300,
            collapsible: true,
            layout: 'fit',
            tbar:[],
            columns:[
                {
                    text:'用户名称',
                    width : '95%',
                    dataIndex:'name'
                }
            ],
            dockedItems: [
                {
                    xtype: 'basePaging',
                    store: 'UserStore',
                    dock: 'right'
                }
            ]
        },*/
        {
            region:'west',
            width: 300,
            xtype: 'moduleList'
        },
        {
            region:'center',
            xtype: 'fieldList'
        }
    ]
});