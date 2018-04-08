/**
 * Created by like on 2015/7/16.
 */
Ext.define("user.view.financeExpense.Charge",{
    extend: 'public.BaseWin',
    alias: 'widget.financeExpenseCharge',
    requires: [
        'public.BaseForm'
    ],
    title: '财务出账',
    items: [
        {
            xtype: 'baseForm',
            viewId:'FinanceExpenseCharge',
            buttons: [{
                text:'记账',itemId:'save',iconCls:'table_save',autoUpdate:false,target:'financeExpenseList'
            },{
                text:'禁止',itemId:'forbid',iconCls:'table_remove',autoUpdate:false,target:'financeExpenseList'
            }]
        }
    ]
});