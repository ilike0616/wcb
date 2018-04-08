/**
 * Created by like on 2015/7/16.
 */
Ext.define("user.view.financeIncome.Charge",{
    extend: 'public.BaseWin',
    alias: 'widget.financeIncomeCharge',
    requires: [
        'public.BaseForm'
    ],
    title: '财务记账',
    items: [
        {
            xtype: 'baseForm',
            viewId:'FinanceIncomeCharge',
            buttons: [{
                text:'记账',itemId:'save',iconCls:'table_save',autoUpdate:false,target:'financeIncomeList'
            },{
                text:'禁止',itemId:'forbid',iconCls:'table_remove',autoUpdate:false,target:'financeIncomeList'
            }]
        }
    ]
});