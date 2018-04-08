/**
 * Created by guozhen on 2015/07/23.
 */
Ext.define("user.model.SaleAimModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'user'},
        {name: 'userName', type: 'string'},
        {name: 'employee'},
        {name: 'employeeName', type: 'string'},
        {name: 'owner'},
        {name: 'ownerName', type: 'string'},
        {name: 'dept'},
        {name: 'deptName', type: 'string'},
        {name: 'aimYearMonth', type: 'string'},
        {name: 'aimType', type: 'int'},

        {name: 'aimCustomer_1', type: 'int'},
        {name: 'aimCustomer_2', type: 'int'},
        {name: 'aimCustomer_3', type: 'int'},
        {name: 'aimCustomer_4', type: 'int'},
        {name: 'aimCustomer_5', type: 'int'},
        {name: 'aimCustomer_6', type: 'int'},
        {name: 'aimCustomer_7', type: 'int'},
        {name: 'aimCustomer_8', type: 'int'},
        {name: 'aimCustomer_9', type: 'int'},
        {name: 'aimCustomer_10', type: 'int'},
        {name: 'aimCustomer_11', type: 'int'},
        {name: 'aimCustomer_12', type: 'int'},
        {name: 'aimCustomer_sum', type: 'int'},

        {name: 'aimCustomerFollow_1', type: 'int'},
        {name: 'aimCustomerFollow_2', type: 'int'},
        {name: 'aimCustomerFollow_3', type: 'int'},
        {name: 'aimCustomerFollow_4', type: 'int'},
        {name: 'aimCustomerFollow_5', type: 'int'},
        {name: 'aimCustomerFollow_6', type: 'int'},
        {name: 'aimCustomerFollow_7', type: 'int'},
        {name: 'aimCustomerFollow_8', type: 'int'},
        {name: 'aimCustomerFollow_9', type: 'int'},
        {name: 'aimCustomerFollow_10', type: 'int'},
        {name: 'aimCustomerFollow_11', type: 'int'},
        {name: 'aimCustomerFollow_12', type: 'int'},
        {name: 'aimCustomerFollow_sum', type: 'int'},

        {name: 'aimOrderMoney_1', type: 'float'},
        {name: 'aimOrderMoney_2', type: 'float'},
        {name: 'aimOrderMoney_3', type: 'float'},
        {name: 'aimOrderMoney_4', type: 'float'},
        {name: 'aimOrderMoney_5', type: 'float'},
        {name: 'aimOrderMoney_6', type: 'float'},
        {name: 'aimOrderMoney_7', type: 'float'},
        {name: 'aimOrderMoney_8', type: 'float'},
        {name: 'aimOrderMoney_9', type: 'float'},
        {name: 'aimOrderMoney_10', type: 'float'},
        {name: 'aimOrderMoney_11', type: 'float'},
        {name: 'aimOrderMoney_12', type: 'float'},
        {name: 'aimOrderMoney_sum', type: 'float'},

        {name: 'aimPayMoney_1', type: 'float'},
        {name: 'aimPayMoney_2', type: 'float'},
        {name: 'aimPayMoney_3', type: 'float'},
        {name: 'aimPayMoney_4', type: 'float'},
        {name: 'aimPayMoney_5', type: 'float'},
        {name: 'aimPayMoney_6', type: 'float'},
        {name: 'aimPayMoney_7', type: 'float'},
        {name: 'aimPayMoney_8', type: 'float'},
        {name: 'aimPayMoney_9', type: 'float'},
        {name: 'aimPayMoney_10', type: 'float'},
        {name: 'aimPayMoney_11', type: 'float'},
        {name: 'aimPayMoney_12', type: 'float'},
        {name: 'aimPayMoney_sum', type: 'float'},

        {name: 'aimOrderProfit_1', type: 'float'},
        {name: 'aimOrderProfit_2', type: 'float'},
        {name: 'aimOrderProfit_3', type: 'float'},
        {name: 'aimOrderProfit_4', type: 'float'},
        {name: 'aimOrderProfit_5', type: 'float'},
        {name: 'aimOrderProfit_6', type: 'float'},
        {name: 'aimOrderProfit_7', type: 'float'},
        {name: 'aimOrderProfit_8', type: 'float'},
        {name: 'aimOrderProfit_9', type: 'float'},
        {name: 'aimOrderProfit_10', type: 'float'},
        {name: 'aimOrderProfit_11', type: 'float'},
        {name: 'aimOrderProfit_12', type: 'float'},
        {name: 'aimOrderProfit_sum', type: 'float'},

        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});