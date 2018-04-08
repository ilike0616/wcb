/**
 * Created by guozhen on 2014/12/15.
 */
Ext.define("admin.view.viewOperation.List", {
    extend : 'Ext.panel.Panel',
    alias : 'widget.viewOperationList',
    modal : true,
    layout : 'fit',
    height:400,
    forceFit:true,
    title : '操作排序',
    listeners:{
        afterrender:function(){
            var view = this.paramsObj.view?this.paramsObj.view:0;
            Ext.apply(this.down('grid').getStore().proxy.extraParams, {view:view});
            this.down('grid').getStore().load();
        }
    },
    items : [ {
        xtype : 'baseList',
        forceFit:true,
        store : Ext.create('admin.store.ViewOperationStore'),
       /* viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop',
                dragText: 'Drag and drop to reorganize'
            },
            listeners: {
                drop : function (node, data, overModel, dropPosition, eOpts) {
                    var store = data.view.getStore();
                    store.each(function(record,index){
                        record.set('orderIndex',index+1);
                    })
                }
            }
        },*/
        tbar:[
            {xtype:'button',text:'上移',itemId:'up',iconCls:'Arrowup',disabled:true,autodisabled:true},
            {xtype:'button',text:'下移',itemId:'down',iconCls:'Arrowdown',disabled:true,autodisabled:true},
            {xtype:'button',text:'保存',itemId:'save',iconCls:'Disk'},
            {xtype:'button',text:'绑定',itemId:'addButton',iconCls:'table_add'},
            {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true}
        ],
        columns : [
            {
                xtype : 'rownumberer',
                width : 30,
                sortable : false
            },{
                text:'操作名称',
                dataIndex:'userOperation.text',
                sortable:false
            }
        ],
        dockedItems:[]
    }]
})