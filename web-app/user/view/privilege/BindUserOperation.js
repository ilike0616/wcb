/**
 * Created by guozhen on 2014-12-04.
 */
Ext.define('user.view.privilege.BindUserOperation', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.bindUserOperation',
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    layout: 'border',
    title : '用户操作',
    privilegeId : 0,
    autoScroll: true,
    initComponent: function() {
        var me = this,
            items = me.items;
        Ext.applyIf(me, {
            tbar: [{
                xtype: 'button',
                text: '绑定',
                iconCls:'table_save',
                listeners:{
                    click : me.bind,
                    scope : me
                }
            }],
            margin:'5 5 5 10',
            items: [{
                border : 0,
                autoScroll : true,
                region:'center',
                xtype:'panel',
                name:'modulePanel',
                defaults : {
                    xtype:'baseFieldSet',
                    checkboxToggle:true,
                    border : 0
                },
                items : items
            }]
        })
        me.callParent(arguments);
    },
    bind : function(btn){    // 绑定
        var me = this;
        // checkbox组
        var checkboxGroups = me.query("baseFieldSet checkboxgroup[name^=userOperation]");
        // 选中的checkbox的值
        var checkedValue = "";
        Ext.Array.each(checkboxGroups, function(checkboxGroup) {
            var checkedObjs = checkboxGroup.getChecked();
            Ext.Array.each(checkedObjs, function(checkedObj) {
                checkedValue += checkedObj.inputValue+",";
            })
        });
        checkedValue = checkedValue.substr(0,checkedValue.length - 1);
        checkedValue = {data: checkedValue,privilegeId: me.privilegeId};
        this.GridDoActionUtil.doAjax('privilege/bindUserOperation',checkedValue,null);
    },
    closeWin : function(o, e, eOpts){   // 关闭
        this.close();
    },
    setItems : function(privilegeId){
        var me = this;
        me.privilegeId = privilegeId;
        Ext.Ajax.request({
            url:'privilege/searchUserOperation?privilegeId='+privilegeId,
            method:'POST',
            timeout:4000,
            async: false,
            success:function(response,opts){
                var data = Ext.JSON.decode(response.responseText).data;
                var items = [];
                Ext.Array.each(data,function(it){
                    items.push(it);
                })
                var modulePanel = me.down("panel[name=modulePanel]");
                modulePanel.removeAll();
                modulePanel.add(items);
            } ,
            failure:function(e,op){
                Ext.Msg.alert("发生错误");
            }
        })
    }
});