/**
 * Created by zhen on 2015/7/2.
 */
Ext.define('public.form.field.MonthField', {
    extend: 'Ext.form.field.Picker',
    alias: 'widget.monthField',
    format: "Y-m",
    altFormats: "m/y|m/Y|m-y|m-Y|my|mY|y/m|Y/m|y-m|Y-m|ym|Ym|Y",
    //invalidText: "{0} is not a valid date - it must be in the format {1}",
    triggerCls: Ext.baseCSSPrefix + 'form-date-trigger',
    matchFieldWidth: false,
    startDay: new Date(),

    initComponent: function () {
        var me = this;
        me.disabledDatesRE = null;
        me.callParent();
    },

    initValue: function () {
        var me = this,
            value = me.value;
        if (Ext.isString(value)) {
            me.value = Ext.Date.parse(value, this.format);
        }
        if (me.value)
            me.startDay = me.value;
        me.callParent();
    },

    rawToValue: function (rawValue) {
        return Ext.Date.parse(rawValue, this.format) || rawValue || null;
    },

    valueToRaw: function (value) {
        return this.formatDate(value);
    },

    formatDate: function (date) {
        return Ext.isDate(date) ? Ext.Date.dateFormat(date, this.format) : date;
    },
    createPicker: function () {
        var me = this,
            format = Ext.String.format;

        return Ext.create('Ext.picker.Month', {
            pickerField: me,
            ownerCt: me.ownerCt,
            renderTo: document.body,
            floating: true,
            shadow: false,
            focusOnShow: true,
            format: me.format,
            listeners: {
                scope: me,
                cancelclick: me.onCancelClick,
                okclick: me.onOkClick,
                yeardblclick: me.onOkClick,
                monthdblclick: me.onOkClick
            },
            keyNavConfig: {
                esc: function() {
                    me.collapse();
                }
            }
        });
    },

    onExpand: function () {
        this.picker.setValue(this.startDay);
    },

    onOkClick: function (picker, value) {
        var me = this,
            month = value[0],
            year = value[1],
            date = new Date(year, month, 1);
        me.startDay = date;
        me.setValue(date);
        me.collapse();
    },

    onCancelClick: function () {
        this.collapse();
    }
});