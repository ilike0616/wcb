/**
 * Created by guozhen on 2014/9/24.
 */
Ext.define('public.uploadPanel.BaseUploadWindow', {
    extend:'Ext.window.Window',
    alias : 'widget.baseUploadWindow',
    requires : [
        'public.uploadPanel.BaseUploadPanel'
    ],
    title: '文件上传',
    height: 400,
    width: 700,
    modal : true,
    layout: 'fit',
    initComponent : function(){
        var me = this;
        Ext.applyIf(me, {
            items : Ext.create('public.uploadPanel.BaseUploadPanel',{
                addFileBtnText : me.addFileBtnText,
                uploadBtnText : me.uploadBtnText,
                removeBtnText : me.removeBtnText,
                cancelBtnText : me.cancelBtnText,
                debug : me.debug,
                file_size_limit : me.file_size_limit,//MB
                file_types : me.file_types,
                file_types_description : me.file_types_description,
                file_upload_limit : me.file_upload_limit,
                file_queue_limit : me.file_queue_limit,
                post_params : me.post_params,
                upload_url : me.upload_url,
                flash_url : "public/uploadPanel/swfupload.swf",
                flash9_url : "public/uploadPanel/swfupload_fp9.swf",
                buttons: [
                    {text:'确定',itemId:'confirm',iconCls:'table_save',listeners : {scope : me,click : me.confirm}},
                    {text:'关闭',itemId:'close',iconCls:'cancel',listeners : {scope : me,click : me.closeWin}}
                ]
            })
        });
        me.callParent(arguments);
    },
    confirm : function(btn, e, eOpts){
        var me = this;
        var grid = btn.up("baseUploadWindow").down("uploadpanel");
        var store = grid.getStore();
        var fileDocSeeds = [];
        var fileNames = [];
        for(var i=0;i<store.getCount();i++) {
            var record = store.getAt(i);
            var docSeed = record.get('docSeed');
            var serverFile = record.get("serverFile");
            if(docSeed!="" && docSeed != 0){
                fileDocSeeds.push({xtype:'hiddenfield',name:me.hiddenName,value:docSeed,itemId:docSeed,width:200});
                var showName = record.get('name');
                if(me.file_upload_limit != "1"){    // 多个文件的话，截取名称
                    if(showName.length > 13){
                        showName = showName.substr(0,13)+"...";
                    }
                }
                fileNames.push({
                    xtype: 'toolbar',
                    itemId: docSeed,
                    layout: {
                        type: 'table',
                        columns: 2
                    },
                    items: [{
                        xtype: 'component',
                        html: "<a href="+serverFile+" target='_blank'>" + showName +"</a>"
                    }, {
                        xtype: 'button',
                        iconCls:'trash',
                        itemId : docSeed,
                        listeners : {
                            click : function(o){
                                var items = me.showNameObj.items.items;
                                Ext.Array.each(items, function(it) {
                                    if(it.itemId == o.itemId){
                                        Ext.Array.each(me.parentMe.items.items, function(hidden) {  // 移除hidden域
                                            if(it.itemId == hidden.itemId){
                                                me.parentMe.remove(hidden);
                                                return false;
                                            }
                                        });
                                        me.showNameObj.remove(it);
                                        if(me.file_upload_limit == "1"){    // 如果只允许上传一个文件，移除时应该把上传按钮置为可用
                                            me.uploadBtn.setDisabled(false);
                                        }
                                        return false;
                                    }
                                });
                            }
                        }
                    }]
                })
            }
        }
        if(fileNames.length > 0){
            if(me.file_upload_limit == "1"){
                me.uploadBtn.setDisabled(true);
            }
            me.parentMe.add(fileDocSeeds);
            me.showNameObj.add(fileNames);
        }
        if(store.getCount() != 0){
            store.removeAll();
        }
        grid.swfupload.uploadStopped = false;
        btn.up("baseUploadWindow").close();
    },
    closeWin : function(btn, e, eOpts){
        var winForm = btn.up("baseUploadWindow");
        winForm.close();
    }
})