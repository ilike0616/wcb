/**
 * 国家、省、市联动
 */
Ext.define('public.BaseCascadeCombo', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.baseCascadeCombo',
    forceSelection:true,
    emptyText : '-- 请选择 --',
    meObj : null,
    initComponent: function() {
        var myStore = [];
        var me = this;
        this.name = me.name;
        if(me.storeKind == 1){ // 国家
            myStore = countryProvinceCityFactory.getCountry();
        }
        Ext.applyIf(me, {
            store:myStore
        });
        me.callParent(arguments);
    },
    listeners:{
        render:function(o,eOpts){
            var me = this;
            if(me.storeKind == 2){ // 设置默认值时，要执行
                var win = me.up('window')
                var countryObj = win.down('combo[name=country]');
                var provinceObj = win.down('combo[name=province]');
                if(Ext.typeOf(countryObj) != 'undefined' && countryObj && Ext.typeOf(provinceObj) != 'undefined'){
                    var countryValue = countryObj.getValue();
                    var data = countryProvinceCityFactory.getProvince(countryValue);
                    provinceObj.store.loadData(data);
                }
            }
        },
        change:function(o, newValue, oldValue, eOpts){
            var me = this;
            var win = me.up('window');
            if(me.storeKind == 1 || me.storeKind == 2){
                var cityObj = win.down('combo[name=city]');
                if(Ext.typeOf(cityObj) != 'undefined'){ // 清空城市
                    cityObj.clearValue();
                }
                if(me.storeKind == 1){ // 国家
                    var provinceObj = win.down('combo[name=province]');
                    if(Ext.typeOf(provinceObj) != 'undefined'){ // 如果页面显示省
                        provinceObj.clearValue();
                        var data = countryProvinceCityFactory.getProvince(newValue);
                        provinceObj.store.loadData(data);
                    }
                }else if(me.storeKind == 2){
                    var countryObj = win.down('combo[name=country]');
                    if(Ext.typeOf(countryObj) != 'undefined' && countryObj && Ext.typeOf(cityObj) != 'undefined'){
                        var countryValue = countryObj.getValue();
                        var data = countryProvinceCityFactory.getCity(countryValue,newValue);
                        cityObj.store.loadData(data);
                    }
                }
            }
        }
    }
});