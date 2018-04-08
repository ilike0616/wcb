/**
 * Created by like on 2015/7/16.
 */
Ext.define("user.view.financeIncome.Wrong",{
    extend: 'public.BaseWin',
    alias: 'widget.financeIncomeWrong',
    requires: [
        'public.BaseForm'
    ],
    title: '红字更正',
    items: [
        {
            xtype: 'baseForm',
            viewId:'FinanceIncomeEdit',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:false,target:'financeIncomeList'
            }]
        }
    ]
});