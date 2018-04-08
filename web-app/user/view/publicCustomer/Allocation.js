Ext.define("user.view.publicCustomer.Allocation",{
    extend: 'Ext.window.Window',
    alias: 'widget.publicCustomerAllocation',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    requires: [
        'public.BaseComboBoxTree'
    ],
    width:400,
    height:150,
    modal:true,
    title: '分配客户',
    layout : 'anchor',
    initComponent : function() {
        var me = this;
        Ext.apply(me, {
            items:[{
                xtype: 'baseComboBoxTree',
                name: 'owner',
                fieldLabel: '分配给',
                displayField: 'name',
                valueField:'id',
                labelWidth:50,
                rootVisible: false,
                minPickerHeight: 200,
                width:300,
                margin:'20 0 20 20',
                store : Ext.create('user.store.EmployeeStoreForEdit')
            }],
            buttons: [
                {text:'分配',itemId:'save',iconCls:'table_save',listeners : {scope : me,click : me.allocate}},
                {text:'关闭',itemId:'close',iconCls:'cancel',listeners : {scope : me,click : me.closeWin}}
            ]
        });
        this.callParent(arguments);
    },
    allocate : function(o, e, eOpts){
        var me = this;
        var owner = me.down("baseComboBoxTree").getValue();
        if(Ext.typeOf(owner) == 'undefined') {
            Ext.example.msg('提示', '请选择分配给哪位员工！');
            return;
        }
        var records = me.listDom.getSelectionModel().getSelection();
        var customers = [];
        Ext.Array.each(records, function(record) {
            if(record.get('customerState') == 1){
                customers.push(record.get('id'));
            }
        });
        var allocationLength = customers.length;
        if(allocationLength <= 0){
            Ext.Msg.alert('提示', '您选择的记录中不包含空闲中的记录！');
            me.close();
            return;
        }
        me.GridDoActionUtil.doAjax('publicCustomer/allocation',{owner:owner,customers:"["+customers.join(",")+"]"},me.listDom.getStore(),false);
        me.close();
        me.listDom.getSelectionModel().deselectAll();
        Ext.example.msg('提示', '您共选择'+records.length+"条数据<br>成功分配"+allocationLength+"条数据");
    },
    closeWin : function(o, e, eOpts){
        this.close();
    }
});
