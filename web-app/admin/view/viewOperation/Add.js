/**
 * Created by guozhen on 2014/11/17.
 */
Ext.define('admin.view.viewOperation.Add', {
    extend: 'Ext.window.Window',
    alias: 'widget.viewOperationAdd',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    width: 600,
    height: 335,
    modal : true,
    layout: 'border',
    title : '绑定操作',
    initComponent: function() {
        var me = this,
            items = me.items;
        if(!items && me.grid.paramsObj.userId && me.grid.paramsObj.moduleId){
            Ext.Ajax.request({
                url:'viewOperation/searchUserOperation?userId='+me.grid.paramsObj.userId+"&moduleId="+me.grid.paramsObj.moduleId+"&viewId="+me.grid.paramsObj.view,
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
                        title: '可选操作',
                        margin : '0 5 0 5',
                        overflowY: 'auto',
                        height : 250,
                        items : [{
                            xtype: 'checkboxgroup',
                            name : 'checkBox',
                            columns: 5,
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
        var me = this;  // me就是win
        var store = me.grid.down('grid').store;
        // checkbox组
        var checkboxGroup = me.down("fieldset checkboxgroup[name=checkBox]");
        // 选中的checkbox的值
        var checkedValue = checkboxGroup.getValue();
        if(checkedValue != null && Ext.typeOf(checkedValue) != 'undefined'){
            checkedValue = {data: checkedValue,viewId: me.grid.paramsObj.view};
            this.GridDoActionUtil.doAjax('viewOperation/enableUserOperation',checkedValue,store);
            me.close();
        }else{
            Ext.example.msg('提示', '请选择要启用的字段！');
        }
    },
    closeWin : function(){   // 关闭
        this.close();
    }
});