/**
 * Created by shqv on 2014-9-4.
 */
Ext.define("admin.view.userField.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.userFieldAdd',
    layout: 'border',
    width: 1000,
    height:600,
    modal: true,
    title:'字段管理',
    items: [
        {
            width: 600,
            title:'固定字段',
            xtype:'fieldList',
            store:Ext.create('admin.store.FieldStore'),
            region: 'west'
        },
        {
            region: 'center',
            title:'字段设置'
        }
    ]
});