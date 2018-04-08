/**
 * Created by like on 2015/7/16.
 */
Ext.define("user.view.financeExpense.Wrong",{
    extend: 'public.BaseWin',
    alias: 'widget.financeExpenseWrong',
    requires: [
        'public.BaseForm'
    ],
    title: '红字更正',
    items: [
        {
            xtype: 'baseForm',
            viewId:'FinanceExpenseEdit',
            buttons: [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:false,target:'financeExpenseList'
            }]
        }
    ]
});