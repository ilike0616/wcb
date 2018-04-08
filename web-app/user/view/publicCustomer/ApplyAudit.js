Ext.define("user.view.publicCustomer.ApplyAudit",{
    extend: 'Ext.window.Window',
    alias: 'widget.publicCustomerApplyAudit',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width:300,
    height:150,
    modal:true,
    title: '申请审核',
    layout : 'anchor',
    initComponent : function() {
        var me = this;
        Ext.apply(me, {
            items:[{
                xtype: 'radiogroup',
                fieldLabel: '是否通过',
                labelWidth: 80,
                columns: 2,
                margin:'20 0 20 20',
                width: 200,
                items: [
                    { boxLabel: '是', name: 'isPass', inputValue: '1', checked: true},
                    { boxLabel: '否', name: 'isPass', inputValue: '2' }
                ]
            }],
            buttons: [
                {text:'审核',itemId:'save',iconCls:'table_save',listeners : {scope : me,click : me.allocate}},
                {text:'关闭',itemId:'close',iconCls:'cancel',listeners : {scope : me,click : me.closeWin}}
            ]
        });
        this.callParent(arguments);
    },
    allocate : function(o, e, eOpts){
        var me = this;
        var isPass = me.down("radiogroup").getValue();
        var records = me.listDom.getSelectionModel().getSelection();
        var customers = [];
        Ext.Array.each(records, function(record) {
            if(record.get('customerState') == 2){
                customers.push(record.get('id'));
            }
        });
        var applyLength = customers.length;
        if(applyLength <= 0){
            Ext.Msg.alert('提示', '您选择的记录中不包含申请中的记录！');
            me.close();
            return;
        }
        me.GridDoActionUtil.doAjax('publicCustomer/applyAudit',{isPass:isPass,customers:"["+customers.join(",")+"]"},me.listDom.getStore(),false);
        me.close();
        me.listDom.getSelectionModel().deselectAll();
        Ext.example.msg('提示', '您共选择'+records.length+"条数据<br>审核通过"+applyLength+"条数据");
    },
    closeWin : function(o, e, eOpts){
        this.close();
    }
});
