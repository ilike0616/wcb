/**
 * Created by like on 2015/9/17.
 */
Ext.define('user.view.sfa.execute.SfaList', {
    extend : 'public.BaseList',
    alias : 'widget.sfaExecuteSfaList',
    autoScroll: true,
    enableComplexQuery:false,
    enableSearchField:false,
    enableStatisticBtn:false,
    enableToolbar:false,
    enableSummary:false,
    showRowNumber:false,
    enableBasePaging:false,
    margin : '5 5 0 5',
    overflowY: 'auto',
    selType:'rowmodel',
    store : Ext.create('user.store.SfaExecuteStore',{autoLoad:false}),
    columns:[
        {
            xtype: 'rownumberer',
            width: 30,
            sortable: false
        },
        {
            text:'方案名称',
            dataIndex:'sfa.name'
        },{
            text:'执行状态',
            dataIndex:'state',
            xtype:'rowselecter',
            arry: [
                [1, '启用'],
                [2, '禁用'],
            ]
        }
    ]
});