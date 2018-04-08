/**
 * Created by like on 2015/10/16.
 */
Ext.define("public.BaseWinForm",{
    extend: 'public.BaseWin',
    alias: 'widget.baseWinForm',
    requires: [
        'public.BaseComboBoxTree',
        'public.BaseForm'
    ],
    //title: '添加我的客户',
    initComponent: function () {
        var me = this,
            moduleId=me.moduleId,
            optType = me.optType,
            viewId = me.viewId,
            title = me.title,
            listDom = me.listDom,
            targetEl = me.targetEl,
            target,btns,defaults={};
        if(me.targetEl){
            target = me.targetEl;
        }else{
            target = listDom.xtype
        }
        if(optType=='add'){
            btns = [{
                text:'保存',itemId:'save',iconCls:'table_save',autoInsert:true,target:target
            }];
        }else if(optType == 'update'){
            btns = [{
                text:'保存',itemId:'save',iconCls:'table_save',autoUpdate:true,target:target
            }];
        }else if(optType == "view"){
            defaults = {
                readOnly:true
            };
            btns =  [{
                text:'关闭',
                iconCls:'cancel',
                handler:function(btn){
                    btn.up('window').close();
                }
            }];
        }
        Ext.apply(me,{
            title: title,
            items: [
                {
                    xtype: 'baseForm',
                    defaults:defaults,
                    moduleId:moduleId,
                    viewId:viewId,
                    buttons: btns
                }
            ]
        });
        this.callParent(arguments);
    }
});