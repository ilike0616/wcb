/**
 * Created by guozhen on 2015/04/23
 */
Ext.define('user.view.share.DetailList', {
    extend: 'public.BaseList',
    alias: 'widget.shareDetailList',
    autoScroll: true,
    store: Ext.create('user.store.ShareDetailStore'),
    title: '分享明细',
    split:true,
    forceFit:true,
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text:'分享者',
            dataIndex:'employeeName'
        },
        {
            text:'分享到',
            dataIndex:'shareTo'
        },
        {
            text:'分享时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }
    ]
});