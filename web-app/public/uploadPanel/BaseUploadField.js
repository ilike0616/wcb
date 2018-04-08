
Ext.define('public.uploadPanel.BaseUploadField', {
    extend: 'Ext.form.Panel',
    alias: 'widget.baseUploadField',
    store : Ext.create('Ext.data.JsonStore',{
        autoLoad : false,
        fields : ['id','name','type','size','percent','status','fileName','docSeed']
    }),
    layout: 'anchor',
    border: 0,
    defaults: {
        msgTarget: 'side'
    },
    uploadFileIndex : 1,
    uploadTotalFileNum : 0,
    kkk : this.fileTypes,
    config:{
        fieldLabel : '附件',
        buttonText : '上传',
        file_size_limit : 20*1024,//文件大小
        file_types : '*.*', // 文件格式，多个以;隔开
        file_types_description : '所有格式',
        file_upload_limit : 50, // 最多能上传多少个文件,0表示不限制
        file_queue_limit : 0,
        save_dir : 'upload',    // 文件目录
        post_params : {},
        upload_url : 'base/uploadFile', // 上传文件路径
        flash_url : "public/uploadPanel/swfupload.swf",
        flash9_url : "public/uploadPanel/swfupload_fp9.swf"
    },
    initComponent: function() {
        var me = this;
        this.config.file_types = me.fileTypes;
        var button = Ext.widget('button',{
            name: 'btn_'+me.name,
            text: me.buttonText,
            iconCls : 'up'
        });
        if(me.readOnly){
            button.hidden = true;
        }
        Ext.applyIf(me, {
            defaults :{
                padding : '3 0 3 0'
            },
            items:[{
                xtype: 'fieldcontainer',
                fieldLabel: me.fieldLabel,
                beforeLabelTextTpl:me.beforeLabelTextTpl,
                labelWidth: 90,
                items:[
                    button,
                    {
                        xtype:'label',
                        itemId:'error_'+me.hiddenName,
                        html:''
                    }]
            },{
                border: 0,
                xtype : 'panel',
                layout: {
                    type: 'table',
                    columns: 2
                },
                defaults: {
                    border : 0,
                    bodyStyle: 'padding-top:5px'
                },
                name : 'showName_'+me.name,
                objectName : me.name,
                objectHiddenName : me.hiddenName,
                items: []
            }],
            listeners : {
                render : function(o,epts){
                    me.generateHiddenField(o); // 临时hidden域，只用于判断
                }
            }
        });
        me.callParent(arguments);
        if(!me.readOnly){   // 如果为查看，则不允许增加此事件
            me.down("fieldcontainer button[name=btn_"+me.name+"]").on({
                afterrender : function(btn){
                    var config = me.getSWFConfig(btn);
                    me.swfupload = new SWFUpload(config);
                    Ext.get(me.swfupload.movieName).setStyle({
                        position : 'absolute',
                        top : 0,
                        left : -2
                    });
                },
                scope : me,
                buffer:300
            });
        }
    },
    getSWFConfig : function(btn){
        var me = this;
        var placeHolderId = Ext.id();
        var em = btn.getEl().child('em');
        if(em==null){
            em = Ext.get(btn.getId()+'-btnWrap');
        }
        em.setStyle({
            position : 'relative',
            display : 'block'
        });
        em.createChild({
            tag : 'div',
            id : placeHolderId
        });
        return {
            flash_url : me.flash_url,
            flash9_url : me.flash9_url,
            upload_url: me.upload_url,
            post_params: me.post_params||{savePath:me.save_dir+'\\'},
            file_size_limit : me.file_size_limit,//MB
            file_types : me.fileTypes,
            file_types_description : me.fileTypesDesc,
            file_upload_limit : me.file_upload_limit,
            file_queue_limit : me.file_queue_limit,
            custom_settings : {
                scope_handler : me
            },
            button_width: em.getWidth(),
            button_height: em.getHeight(),
            button_window_mode:SWFUpload.WINDOW_MODE.TRANSPARENT,
            button_cursor: SWFUpload.CURSOR.HAND,
            button_placeholder_id: placeHolderId,
            swfupload_preload_handler : me.swfupload_preload_handler,
            file_queue_error_handler : me.file_queue_error_handler,
            swfupload_load_failed_handler : me.swfupload_load_failed_handler,
            upload_start_handler : me.upload_start_handler,
            upload_progress_handler : me.upload_progress_handler,
            upload_error_handler : me.upload_error_handler,
            upload_success_handler : me.upload_success_handler,
            upload_complete_handler : me.upload_complete_handler,
            file_queued_handler : me.file_queued_handler,
            file_dialog_complete_handler : me.file_dialog_complete_handler
        };
    },
    swfupload_preload_handler : function(){
        if (!this.support.loading) {
            Ext.Msg.show({
                title : '提示',
                msg : '浏览器Flash Player版本太低,不能使用该上传功能！',
                width : 250,
                icon : Ext.Msg.ERROR,
                buttons :Ext.Msg.OK
            });
            return false;
        }
    },
    file_queue_error_handler : function(file, errorCode, message){
        switch(errorCode){
            case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED : msg('上传文件列表数量超限,不能选择！');
                break;
            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT : msg('文件大小超过限制, 不能选择！');
                break;
            case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE : msg('该文件大小为0,不能选择！');
                break;
            case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE : msg('该文件类型不允许上传！');
                break;
        }
        function msg(info){
            Ext.Msg.show({
                title : '提示',
                msg : info,
                width : 250,
                icon : Ext.Msg.WARNING,
                buttons :Ext.Msg.OK
            });
        }
    },
    swfupload_load_failed_handler : function(){
        Ext.Msg.show({
            title : '提示',
            msg : 'SWFUpload加载失败！',
            width : 180,
            icon : Ext.Msg.ERROR,
            buttons :Ext.Msg.OK
        });
    },
    upload_start_handler : function(file){  // 开始上传
        var me = this.settings.custom_settings.scope_handler;
        // 模拟上传文件时的动态效果
        var v = me.uploadFileIndex / parseFloat(me.uploadTotalFileNum);
        Ext.Msg.updateProgress(v,me.uploadFileIndex + "/" + me.uploadTotalFileNum);
        me.uploadFileIndex = me.uploadFileIndex + 1;

        var rec = me.store.getById(file.id);
        this.setFilePostName(encodeURIComponent(rec.get('fileName')));
    },
    upload_progress_handler : function(file, bytesLoaded, bytesTotal){
    },
    upload_error_handler : function(file, errorCode, message){
    },
    upload_success_handler : function(file, serverData, responseReceived){  // 上传成功时
        var me = this.settings.custom_settings.scope_handler;
        var formObj = me.ownerCt.ownerCt;
        var showNameObj = formObj.down("panel[name=showName_"+me.name+"]");
        // 设置返回的结果
        var serverResult = Ext.JSON.decode(serverData);
        var rec = me.store.getById(file.id);
        if(serverResult.success){
            rec.set('docSeed',serverResult.docSeed);
        }else{
            rec.set('docSeed',0);
        }
        rec.commit();
        if (me.swfupload.getStats().files_queued > 0 && me.swfupload.uploadStopped == false) {
            me.swfupload.startUpload();
        }else{
            me.showUploadFiles();
            Ext.Msg.hide();
            me.down('label#error_'+me.hiddenName).el.dom.innerHTML = "";
        }
    },
    upload_complete_handler : function(file){   // 上传完之后
    },
    file_queued_handler : function(file){
        var me = this.settings.custom_settings.scope_handler;
        me.store.add({
            id : file.id,
            name : file.name,
            fileName : file.name,
            size : file.size,
            type : file.type,
            status : file.filestatus,
            percent : 0
        });
    },
    file_dialog_complete_handler : function(){  // 选择文件之后。
        var me = this.settings.custom_settings.scope_handler;
        me.swfupload.uploadStopped = false;
        me.uploadTotalFileNum = me.swfupload.getStats().files_queued;   // 上传文件数量
        me.uploadFileIndex = 1; // 当前上传文件的索引
        if(me.uploadTotalFileNum > 0){
            Ext.Msg.show({
                msg: '上传中...',
                progressText: me.uploadFileIndex+"/"+me.uploadTotalFileNum,
                width: 300,
                icon:'ext_download',
                iconHeight: 50,
                wait:true,
                closable: false
            });
            // 开始上传
            me.swfupload.startUpload();
        }
    },
    showUploadFiles : function () {
        var me = this;
        var formObj = me.ownerCt.ownerCt;
        var showNameObj = formObj.down("panel[name=showName_"+me.name+"]");
        var uploadBtn = formObj.down("button[name=btn_"+me.name+"]");
        var store = me.store;
        var fileDocSeeds = [];
        var fileNames = [];
        for(var i=0;i<store.getCount();i++) {
            var record = store.getAt(i);
            var docSeed = record.get('docSeed');
            if(docSeed!="" && docSeed != 0){
                fileDocSeeds.push({xtype:'hiddenfield',name:me.hiddenName,value:docSeed,itemId:docSeed,width:200});
                var showName = record.get('name');
                if(me.file_upload_limit != "1"){    // 多个文件的话，截取名称
                    if(showName.length > 25){
                        showName = showName.substr(0,25)+"...";
                    }
                }
                var downloadUrl = "base/downloadFile?id="+docSeed;
                fileNames.push({
                    xtype: 'toolbar',
                    itemId: docSeed,
                    width:320,
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'component',
                        html: "<a href='"+downloadUrl+"' target='_target'>" + showName +"</a>"
                    },{
                        xtype: 'button',
                        iconCls:'trash',
                        itemId : docSeed,
                        listeners : {
                            click : function(o){
                                var items = showNameObj.items.items;
                                Ext.Array.each(items, function(it) {
                                    if(it.itemId == o.itemId){
                                        Ext.Array.each(me.items.items, function(hidden) {  // 移除hidden域
                                            if(it.itemId == hidden.itemId){
                                                me.remove(hidden);
                                                return false;
                                            }
                                        });
                                        showNameObj.remove(it);
                                        if(me.file_upload_limit == "1"){    // 如果只允许上传一个文件，移除时应该把上传按钮置为可用
                                            uploadBtn.setDisabled(false);
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
                uploadBtn.setDisabled(true);
            }
            me.add(fileDocSeeds);
            showNameObj.add(fileNames);
        }
        if(store.getCount() != 0){
            store.removeAll();
        }
    },
    generateHiddenField : function(o){
        var me = this;
        var hiddenFieldId = {
            xtype : 'hiddenfield',
            name : 'temp_'+me.hiddenName,
            tempName:me.hiddenName,
            itemId : 'tempBaseUploadFieldHidden_'+me.hiddenName,
            allowBlank:me.allowBlank
        };
        me.add(hiddenFieldId);
    }
})
