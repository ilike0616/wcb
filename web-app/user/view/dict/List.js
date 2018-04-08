/**
 * Created by shqv on 2014-9-13.
 */
var store = Ext.create('user.store.DataDictStore');
Ext.define('user.view.dict.List', {
    extend: 'public.BaseList',
    alias: 'widget.dictList',
    autoScroll: true,
    store:store,
    title: '数据字典',
    split:true,
    forceFit:true,
    renderLoad:true,
    tbar:[
        {
            width:300,
            labelWidth : 50,
            fieldLabel : '搜索',
            xtype : 'baseSearchField',
            store : store
        }
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'名称',
            dataIndex:'text'
        }
    ]
});