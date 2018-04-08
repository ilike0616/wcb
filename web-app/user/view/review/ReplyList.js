/**
 * Created by like on 2015/8/14.
 */
Ext.define('user.view.review.ReplyList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.reviewReplyList',
    autoScroll: true,
    split: true,
    forceFit: true,
    renderLoad: false,
    requires: [
        'Ext.ux.PreviewPlugin'
    ],
    store: Ext.create('Ext.data.Store', {
        fields: ['content', 'employee.name', 'dateCreated'],
        proxy: {
            type: 'ajax',
            api: {
                read: 'review/replyList'
            },
            reader: {
                type: 'json',
                root: 'data',
                successProperty: 'success',
                totalProperty: 'total'
            },
            simpleSortMode: true
        },
        autoLoad: false
    }),
        plugins: [{
            ptype: 'preview',
            bodyField: 'content',
            previewExpanded: true,
            pluginId: 'preview'
        }],
    columns: [
        {
            id: 'topic',
            text: '内容',
            flex: 1,
            sortable: false,
            dataIndex:'content',
            renderer: function(value, p, record) {
                return Ext.String.format(
                    '',
                    value
                );
            }
        },
        {
            text: '回复人',
            dataIndex: 'employee.name',
            width: 70
        },
        {
            text: '回复时间',
            dataIndex: 'dateCreated',
            xtype: "datecolumn",
            format: "Y-m-d H:i:s",
            width: 150
        }
    ],
    initComponent: function () {
        var me = this;
        //me.on({
        //    select: this.onselect,
        //    deselect: this.ondeselect
        //});
        me.callParent(arguments);
    }
    //onselect:function(selectModel, record, index, eOpts){
    //    var me = this,
    //        pre = me.getPlugin('preview');
    //    pre.previewExpanded = false;
    //    me.refesh();
    //},
    //ondeselect:function(selectModel, record, index, eOpts){
    //    var me = this,
    //        pre = me.getPlugin('preview');
    //    pre.previewExpanded = false;
    //    me.refesh();
    //}
});