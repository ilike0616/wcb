Ext.define('public.BaseCombo', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.baseCombo',
    displayField: 'text', 
    valueField:'id',
    typeAhead: true,
    triggerAction: 'all',
    initComponent: function() {
        var me = this,store = Ext.create('Ext.data.Store', {
        	fields : ['id','text'],  
            data: me.data  
        }); 
        Ext.applyIf(me, {
        	store : store
        });
        me.callParent(arguments);
    }
});