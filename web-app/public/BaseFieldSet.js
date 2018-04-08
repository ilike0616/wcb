/**
 * Created by shqv on 2014-9-16.
 */
Ext.define('public.BaseFieldSet', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget.baseFieldSet',
    initComponent: function() {
        var me = this
        me.callParent(arguments);
    },
    listeners:{
        beforecollapse : function(o, eOpts ){   // 取消选中
            var checkboxGroup = o.down("checkboxgroup");
            Ext.Array.each(checkboxGroup.items.items, function(checkbox) {
                if(checkbox.checked && !checkbox.readOnly){
                    checkbox.setValue(false);
                }
            });
            return false;
        },
        beforeexpand : function(o, eOpts){  // // 选中
            var checkboxGroup = o.down("checkboxgroup");
            Ext.Array.each(checkboxGroup.items.items, function(checkbox) {
                if(!checkbox.checked){
                    checkbox.setValue(true);
                }
            });
            return false;
        },
        beforerender : function(o, eOpts){
            var checkboxGroup = o.down('checkboxgroup');
            var allTrue = true;
            Ext.Array.each(checkboxGroup.items.items, function(checkbox) {
                if(!checkbox.checked){
                    allTrue = false;
                    return false;
                }
            });
            if(checkboxGroup.items.getCount() == 0){    // 该菜单没有权限
                allTrue = false;
            }
            o.checkboxCmp.setValue(allTrue);
        }
    },
    setExpanded: function(expanded) {
        var me = this,
            checkboxCmp = me.checkboxCmp,
            operation = expanded ? 'expand' : 'collapse';
        if (checkboxCmp) {
            checkboxCmp.setValue(expanded);
        }
        if (!me.rendered || me.fireEvent('before' + operation, me) !== false) {
            if (me.rendered) {
                me.updateLayout({ isRoot: false });
                me.fireEvent(operation, me);
            }
        }
        return me;
    }

})
