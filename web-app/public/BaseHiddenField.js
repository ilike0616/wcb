/**
 * Created by zhen on 2015/7/16.
 */
Ext.define('public.BaseHiddenField', {
    extend: 'Ext.form.field.Hidden',
    alias: 'widget.baseHiddenField',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
        });
        me.callParent(arguments);
    },
    setValue:function(value){
        if(value != 'undefined'){
            if(Ext.typeOf(value) == 'object'){
                value = value['id'];
            }
        }
        this.callParent(arguments);
    }
})