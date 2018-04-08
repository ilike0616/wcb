/**
 * Created by shqv on 2014-8-27.
 */
Ext.define('admin.view.view.detail.Main', {
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailMain',
    layout: 'border',
    modal: true,
    submitNotClose:true,
    title: '明细操作',
    width: 1300,
    height: 550,
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [{
                region:'west',
                xtype: 'viewDetailViewList',
                collapsible: true,
                split: true,
                paramsObj : me.paramsObj
            },{
                region:'center',
                xtype: 'viewDetailList',
                paramsObj : me.paramsObj
            },{
                region:'east',
                xtype:'panel',
                itemId:'relatedOperatePanel',
                width :490,
                title:'相关操作',
                layout:'accordion',
                items:[{
                        xtype: 'viewOperationList',
                        split: true,
                        paramsObj : me.paramsObj
                    },{
                        title: '属性设置',
                        xtype:'viewEmptyField',
                        name:'propertySet'
                    }
                ]
            }]
        });
        me.callParent(arguments);
    }
});