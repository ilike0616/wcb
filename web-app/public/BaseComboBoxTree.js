/**
 * A Picker field that contains a tree panel on its popup, enabling selection of tree nodes.
 */
Ext.define('public.BaseComboBoxTree', {
    extend: 'Ext.form.field.ComboBox',
    alias : 'widget.baseComboBoxTree',

    uses: [
        'Ext.tree.Panel'
    ],

    triggerCls: Ext.baseCSSPrefix + 'form-arrow-trigger',

    config: {
        /**
         * @cfg {Ext.data.TreeStore} store
         * A tree store that the tree picker will be bound to
         */
        store: null,

        /**
         * @cfg {String} displayField
         * The field inside the model that will be used as the node's text.
         * Defaults to the default value of {@link Ext.tree.Panel}'s `displayField` configuration.
         */
        displayField: null,

        /**
         * @cfg {Array} columns
         * An optional array of columns for multi-column trees
         */
        columns: null,

        /**
         * @cfg {Boolean} selectOnTab
         * Whether the Tab key should select the currently highlighted item. Defaults to `true`.
         */
        selectOnTab: true,

        /**
         * @cfg {Number} maxPickerHeight
         * The maximum height of the tree dropdown. Defaults to 300.
         */
        maxPickerHeight: 300,

        /**
         * @cfg {Number} minPickerHeight
         * The minimum height of the tree dropdown. Defaults to 100.
         */
        minPickerHeight: 100,

        checkModel : false,

        rootVisible : true

    },
   
    editable: false,

    initComponent: function() {
        var me = this;
        me.callParent(arguments);

        me.addEvents(
            /**
             * @event select
             * Fires when a tree node is selected
             * @param {Ext.ux.TreePicker} picker        This tree picker
             * @param {Ext.data.Model} record           The selected record
             */
            'select'
        );

        me.mon(me.store, {
            scope: me,
            load: me.onLoad,
            update: me.onUpdate
        });
    },

    /**
     * Creates and returns the tree panel to be used as this field's picker.
     */
    createPicker: function() {
        var me = this,
            picker = new Ext.tree.Panel({
                shrinkWrapDock: 2,
                store: me.store,
                floating: true,
                autoScroll : true,
                displayField: me.displayField,
                columns: me.columns,
                minHeight: me.minPickerHeight,
                maxHeight: me.maxPickerHeight,
                manageHeight: false,
                checkModel : me.checkModel,
                shadow: false,
                height : me.minPickerHeight,
                rootVisible : me.rootVisible,
                listeners: {
                    scope: me,
                    itemclick: me.onItemClick,
                    checkchange: me.checkchange
                },
                viewConfig: {
                    listeners: {
                        scope: me,
                        render: me.onViewRender
                    }
                }
            }),

            view = picker.getView();

        if (Ext.isIE9 && Ext.isStrict) {
            // In IE9 strict mode, the tree view grows by the height of the horizontal scroll bar when the items are highlighted or unhighlighted.
            // Also when items are collapsed or expanded the height of the view is off. Forcing a repaint fixes the problem.
            view.on({
                scope: me,
                highlightitem: me.repaintPickerView,
                unhighlightitem: me.repaintPickerView,
                afteritemexpand: me.repaintPickerView,
                afteritemcollapse: me.repaintPickerView
            });
        }

        if(!(me.value) && me.checkModel){
            var root = me.store.getRootNode();
            root.cascadeBy(function(node) {
                /**2014-12-05 父类应该不让有复选框**/
                //node.set('checked', false);
            });
        }

        return picker;
    },
    
    onViewRender: function(view){
        view.getEl().on('keypress', this.onPickerKeypress, this);
    },

    /**
     * repaints the tree view
     */
    repaintPickerView: function() {
        var style = this.picker.getView().getEl().dom.style;

        // can't use Element.repaint because it contains a setTimeout, which results in a flicker effect
        style.display = style.display;
    },
        /**
     * Aligns the picker to the input element
     */
    alignPicker: function() {
        var me = this,
            picker;

        if (me.isExpanded) {
            picker = me.getPicker();
            if (me.matchFieldWidth) {
                // Auto the height (it will be constrained by max height)
                picker.setWidth(me.bodyEl.getWidth());
            }
            if (picker.isFloating()) {
                me.doAlign();
            }
        }
    },

    /**
     * Handles a click even on a tree node
     * @private
     * @param {Ext.tree.View} view
     * @param {Ext.data.Model} record
     * @param {HTMLElement} node
     * @param {Number} rowIndex
     * @param {Ext.EventObject} e
     */
    onItemClick: function(view, record, node, rowIndex, e) {
        var me = this;
        var checkModel = me.checkModel;
        if (!checkModel) {
            this.selectItem(record);
        }
    },

    checkchange : function(record, checked,eOpts) {
        var me = this;
        if(checked){
            record.set('checked', true);
        }else{
            record.set('checked', false);
        }
       /* if(record.get('leaf')) {
            if (!checked) {
                record.set('checked', false);
            }
        }else{
            if (checked) {
                record.cascadeBy(function(record) {
                    record.set('checked', true);
                });
            } else {
                record.cascadeBy(function (record) {
                    record.set('checked', false);
                });
            }
        }*/
        var records = me.picker.getView().getChecked(),
        names = [],
        values = [];
        Ext.Array.each(records,function(rec) {
            names.push(rec.get('text'));
            values.push(rec.get('id'));
        });
        me.setRawValue(names.join(','));
        me.setValue(values.join(','));
    },

    /**
     * Handles a keypress event on the picker element
     * @private
     * @param {Ext.EventObject} e
     * @param {HTMLElement} el
     */
    onPickerKeypress: function(e, el) {
        var key = e.getKey();

        if(key === e.ENTER || (key === e.TAB && this.selectOnTab)) {
            this.selectItem(this.picker.getSelectionModel().getSelection()[0]);
        }
    },

    /**
     * Changes the selection to a given record and closes the picker
     * @private
     * @param {Ext.data.Model} record
     */
    selectItem: function(record) {
        var me = this;
        me.setValue(record.getId());
        me.picker.hide();
        me.inputEl.focus();
        me.fireEvent('select', me, record)
    },

    /**
     * Runs when the picker is expanded.  Selects the appropriate tree node based on the value of the input element,
     * and focuses the picker so that keyboard navigation will work.
     * @private
     */
    onExpand: function() {
        var me = this,
            picker = me.picker,
            store = picker.store,
            value = me.value,
            node;


        if (value) {
            node = store.getNodeById(value);
        }
        
        if (!node) {
            node = store.getRootNode();
        }
        
        picker.selectPath(node.getPath());

        Ext.defer(function() {
            picker.getView().focus();
        }, 1);
    },

    /**
     * Sets the specified value into the field
     * @param {Mixed} value
     * @return {Ext.ux.TreePicker} this
     */
    setValue: function(value) {
        var me = this,
        record;

        me.value = value;

        if (me.store.loading) {
            // Called while the Store is loading. Ensure it is processed by the onLoad method.
            return me;
        }

        if(Ext.typeOf(me.store) == 'string'){
            //me.store = Ext.data.StoreManager.lookup(me.store);
            var tempStore = Ext.create('user.store.'+me.store);
            if(Ext.typeOf(tempStore) == 'undefined') {
                tempStore = Ext.create('admin.store.'+me.store)
            }
            me.store = tempStore;
        }

        if(!me.checkModel){
            // try to find a record in the store that matches the value
            record = value ? me.store.getNodeById(value) : null;
            if (value === undefined || value == null) {
                //record = me.store.getRootNode();
                me.value = null;
            } else {
                record = me.store.getNodeById(value);
            }

            // set the raw value to the record's display field if a record was found
            me.setRawValue(record ? record.get(me.displayField) : '');
        }else{
            var value = this.getValue();
            if(value && me.checkModel){
                var valueArr = new Array();
                if(Ext.typeOf(value) != 'array'){
                    valueArr = this.getValue().split(",");
                }else{
                    valueArr = value.toString();
                }
                var rawValue = "";
                var root = me.store.getRootNode();
                root.cascadeBy(function(node) {
                    if(Ext.Array.contains(valueArr,node.get("id").toString())){
                        rawValue += node.get(me.displayField)+",";
                        node.set('checked', true);
                    }else{
                        node.set('checked', false);
                    }
                });

                if(rawValue != "") rawValue = rawValue.substring(0,rawValue.length-1);
                me.setRawValue(rawValue);
            }
        }
        return me;
    },

    getSubmitValue: function(){
        var value = this.value;
        if(value == null) return null;
        value = value.toString();
        if(value.indexOf(",") > -1){
            return value.split(",");
        }else{
            return value;
        }
    },

    /**
     * Returns the current data value of the field (the idProperty of the record)
     * @return {Number}
     */
    getValue: function() {
        return this.value;
    },

    /**
     * Handles the store's load event.
     * @private
     */
    onLoad: function() {
        var value = this.value;
        if (value) {
            this.setValue(value);
        }
    },
    
    onUpdate: function(store, rec, type, modifiedFieldNames){
        var display = this.displayField;
        if (type === 'edit' && modifiedFieldNames && Ext.Array.contains(modifiedFieldNames, display) && this.value === rec.getId()) {
            this.setRawValue(rec.get(display));
        }
    }

});

