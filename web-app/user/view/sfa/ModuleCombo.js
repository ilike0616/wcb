/**
 * Created by like on 2015/9/14.
 */
Ext.define("user.view.sfa.ModuleCombo", {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.sfaModuleCombo',
    displayField: 'text',
    valueField: 'moduleId',
    typeAhead: true,
    triggerAction: 'all',
    initComponent: function () {
        var me = this, store = Ext.create('Ext.data.Store', {
            fields: ['moduleId', 'text'],
            data: [{moduleId: 'public_customer', text: '公海客户'},
                {moduleId: 'customer', text: '我的客户'},
                {moduleId: 'contact', text: '我的联系人'},
                {moduleId: 'customer_follow', text: '客户跟进'},
                {moduleId: 'sale_chance', text: '销售商机'},
                {moduleId: 'sale_chance_follow', text: '商机跟进'},
                //{moduleId: 'service_task', text: '服务派单'},
                {moduleId: 'contract_order', text: '订单管理'}]
        });
        Ext.applyIf(me, {
            store: store
        });
        me.callParent(arguments);
    }
});