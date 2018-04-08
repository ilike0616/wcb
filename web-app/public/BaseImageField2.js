/**
 * 自定义文件字段
 * Created by guozhen on 2014/10/15.
 */
Ext.define('public.BaseImageField2', {
    extend: 'Ext.container.Container',
    alias: 'widget.baseImageField2',
    layout: {
        type: 'column',
       
        columns: 2
    },
    defaults: {
        margin : '5 0 5 0'
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items : [{
                xtype: 'label', 
                align:'right',
                text: me.fieldLabel+':',
                width : 95
            },{
                xtype: 'fieldset',
                layout: 'hbox',
                objectHiddenName : me.hiddenName,
                items:[{
                    xtype: 'image',
                    itemId: 'image_'+me.hiddenName,
                    src: 'uploadFile/default.jpg',
                    width: 130,
                    height: 100
                }]
            }],
            listeners : {
                render : function(o,epts){
                   me.generateHiddenField(o);
                }
            }
        });
        me.callParent(arguments);
    },
    uploadFile : function (v) {
        var me = this;
        var win = me.ownerCt.ownerCt;
        var frm = win.down("form");
        frm.submit({
            url:'onsiteObject/uploadSingleFile?fileName=file_'+me.name,
            method: 'POST',
            success: function(frm,action) {
                if(action.result.success){
                    me.down("image#image_"+me.hiddenName).setSrc(action.result.serverFile);
                    me.down("hiddenfield[name="+me.hiddenName+"]").setValue(action.result.docSeed);
                }
            },
            failure:function(response,opts){
            }
        });
    },
    generateHiddenField : function(o){
        var me = this;
        var hiddenFieldId = {
            xtype : 'hiddenfield',
            name : me.hiddenName
        };
        // 根据页面上是否存在me.hiddenName对应的隐藏变量，如果存在，则不添加，防止重复。否则，则生成一个
        var win = o.ownerCt.ownerCt;
        var hiddenField = win.down("hiddenfield[name="+me.hiddenName+"]");
        if(hiddenField == null || Ext.typeOf(hiddenField) == 'undefined'){
            me.add(hiddenFieldId);
        }
    }
})
