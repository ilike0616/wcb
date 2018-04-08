/**
 * Created by like on 2015-05-06.
 */
Ext.define("admin.model.PaymentRecordModel", {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'user.id', type: 'int'},
        {name: 'user.name', type: 'string'},
        {name: 'amountFee', type: 'float'},
        {name: 'preBalance', type: 'float'},
        {name: 'postBalance', type: 'float'},
        {name: 'syNum', type: 'int'},
        {name: 'kfNum', type: 'int'},
        {name: 'dateCreated', type: 'date'},
        {name: 'lastUpdated', type: 'date'}
    ]
});