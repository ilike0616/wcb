Ext.define('user.view.module.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.moduleList',
    autoScroll: true,
    store: Ext.create('user.store.ModuleStore'),
    title: '系统模块',
    split:true,
    forceFit:true,
    operateBtn:false,
    columns:[{
            text:'模块名称',
            dataIndex:'text',
            xtype: 'treecolumn'
        }
    ]
});