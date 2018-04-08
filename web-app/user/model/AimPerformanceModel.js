/**
 * Created by guozhen on 2015/07/23.
 */
Ext.define("user.model.AimPerformanceModel",{
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
        {name: 'aimType', type: 'int'},

        {name: 'aim_1', type: 'float'},
        {name: 'aim_2', type: 'float'},
        {name: 'aim_3', type: 'float'},
        {name: 'aim_4', type: 'float'},
        {name: 'aim_5', type: 'float'},
        {name: 'aim_6', type: 'float'},
        {name: 'aim_7', type: 'float'},
        {name: 'aim_8', type: 'float'},
        {name: 'aim_9', type: 'float'},
        {name: 'aim_10', type: 'float'},
        {name: 'aim_11', type: 'float'},
        {name: 'aim_12', type: 'float'},
        {name: 'aim_sum', type: 'float'},

        {name: 'aimComplete_1', type: 'float'},
        {name: 'aimComplete_2', type: 'float'},
        {name: 'aimComplete_3', type: 'float'},
        {name: 'aimComplete_4', type: 'float'},
        {name: 'aimComplete_5', type: 'float'},
        {name: 'aimComplete_6', type: 'float'},
        {name: 'aimComplete_7', type: 'float'},
        {name: 'aimComplete_8', type: 'float'},
        {name: 'aimComplete_9', type: 'float'},
        {name: 'aimComplete_10', type: 'float'},
        {name: 'aimComplete_11', type: 'float'},
        {name: 'aimComplete_12', type: 'float'},
        {name: 'aimComplete_sum', type: 'float'},

        {name: 'aimCompletePercent_1', type: 'string'},
        {name: 'aimCompletePercent_2', type: 'string'},
        {name: 'aimCompletePercent_3', type: 'string'},
        {name: 'aimCompletePercent_4', type: 'string'},
        {name: 'aimCompletePercent_5', type: 'string'},
        {name: 'aimCompletePercent_6', type: 'string'},
        {name: 'aimCompletePercent_7', type: 'string'},
        {name: 'aimCompletePercent_8', type: 'string'},
        {name: 'aimCompletePercent_9', type: 'string'},
        {name: 'aimCompletePercent_10', type: 'string'},
        {name: 'aimCompletePercent_11', type: 'string'},
        {name: 'aimCompletePercent_12', type: 'string'},
        {name: 'aimCompletePercent_sum', type: 'string'},


        {name: 'aim_sum', type: 'float'},
        {name: 'aimComplete_sum', type: 'float'},
        {name: 'aimCompletePercent_sum', type: 'string'},

        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});