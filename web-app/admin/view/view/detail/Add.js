/**
 * Created by guozhen on 2014/11/17.
 */
Ext.define('admin.view.view.detail.Add', {
    extend: 'Ext.window.Window',
    alias: 'widget.viewDetailAdd',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 700,
    height: 335,
    modal : true,
    layout: 'border',
    title : '启用字段',
    initComponent: function() {
        var me = this,
            items = me.items;
        if(!items && me.parentWin.paramsObj.view){
            Ext.Ajax.request({
                url:'viewDetail/searchEnableField?view='+me.parentWin.paramsObj.view,
                method:'POST',
                timeout:4000,
                async: false,
                success:function(response,opts){
                    items = Ext.JSON.decode(response.responseText).data;
                } ,
                failure:function(e,op){
                    Ext.Msg.alert("发生错误");
                }
            })
        }
        Ext.applyIf(me, {
            items: [
                {
                    region:'center',
                    border : 0,
                    items : [{
                        xtype:'baseFieldSet',
                        checkboxToggle:true,
                        title: '可选字段',
                        margin : '0 5 0 5',
                        overflowY: 'auto',
                        height : 250,
                        items : [{
                            xtype: 'checkboxgroup',
                            name : 'chooseField',
                            columns: 3,
                            defaults : {
                                padding : '5px 0px 5px 0px'
                            },
                            items: items
                        }]
                    }]
                }
            ],
            buttons: [
                {text:'启用',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                {text:'关闭',itemId:'close',iconCls:'table_save',listeners : {scope : me,click : me.closeWin}}
            ]
        })
        me.callParent(arguments);
    },
    confirm : function(o, e, eOpts){    // 确定
        var me = this;
        var win = o.up("viewDetailAdd");
        // checkbox组
        var checkboxGroup = win.down("fieldset checkboxgroup[name=chooseField]");
        // 选中的checkbox的值
        var checkedValue = checkboxGroup.getValue();
        if(checkedValue != null && checkedValue != 'undefined'){
            checkedValue = {data: checkedValue,viewId: me.parentWin.paramsObj.view};
            var success = true;
            success = this.GridDoActionUtil.doAjax('viewDetail/enableField',checkedValue,null,false);
            var store = me.parentWin.down('grid').getStore();
            Ext.apply(store.proxy.extraParams, {view:me.parentWin.paramsObj.view});
            store.load();
            win.close();
        }else{
            Ext.example.msg('提示', '请选择要启用的字段！');
        }
    },
    closeWin : function(o, e, eOpts){   // 关闭
        this.close();
    }
});