Ext.define('public.BaseImageField', {
    extend: 'Ext.container.Container',
    alias: 'widget.baseImageField',
    config:{
        buttonText : '上传',
        file_size_limit : 2*1024,//文件大小
        file_types : '*.jpg;*.jpeg;*.gif;*.bmp;*.png', // 文件格式，多个以;隔开
        file_types_description : '图片格式',
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
        var uploadButton = Ext.widget('button',{
            name: 'btn_'+me.name,
            text: '上传'
        });
        if(me.readOnly){
            uploadButton.hidden = true;
        }
        Ext.applyIf(me, {
            items : [{
                xtype: 'form',
                border: 0,
                items:[
                    {
                        xtype: 'fieldcontainer',
                        objectHiddenName : me.hiddenName,
                        fieldLabel: me.fieldLabel,
                        beforeLabelTextTpl:me.beforeLabelTextTpl,
                        labelWidth: 90,
                        layout:{
                            type:'hbox',
                            columns:2
                        },
                        items:[{
                            xtype: 'image',
                            itemId: 'image_'+me.hiddenName,
                            src: 'uploadFile/default.jpg',
                            height:100,
                            width:120,
                            listeners: {
                                el: {
                                    click: function() {
                                    }
                                }
                            }
                        },
                            uploadButton,
                            {
                                xtype:'label',
                                itemId:'error_'+me.hiddenName,
                                html:''
                            }
                        ]
                    }
                ]
            }],
            listeners : {
                render : function(o,epts){
                   me.generateHiddenField(o);
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
            file_types : me.file_types,
            file_types_description : me.file_types_description,
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
            case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE : msg('只允许上传图片格式（jpg，jpeg，gif，bmp，png）');
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
    },
    upload_progress_handler : function(file, bytesLoaded, bytesTotal){
    },
    upload_error_handler : function(file, errorCode, message){
    },
    upload_success_handler : function(file, serverData, responseReceived){  // 上传成功时
        var me = this.settings.custom_settings.scope_handler;
        var formObj = me.ownerCt.ownerCt;
        // 设置返回的结果
        var serverResult = Ext.JSON.decode(serverData);
        if(serverResult.success){
            formObj.down("image#image_"+me.hiddenName).setSrc(serverResult.httpFilePath);
            formObj.down("hiddenfield[name="+me.hiddenName+"]").setValue(serverResult.docSeed);
            formObj.down('label#error_'+me.hiddenName).el.dom.innerHTML = "";
        }else{
            alert("出错了，请联系管理员");
            return;
        }
    },
    upload_complete_handler : function(file){   // 上传完之后
    },
    file_queued_handler : function(file){
    },
    file_dialog_complete_handler : function(){  // 选择文件之后。
        var me = this.settings.custom_settings.scope_handler;
        me.swfupload.uploadStopped = false;
        me.uploadTotalFileNum = me.swfupload.getStats().files_queued;   // 上传文件数量
        if(me.uploadTotalFileNum > 0){
            // 开始上传
            me.swfupload.startUpload();
        }
    },
    generateHiddenField : function(o){
        var me = this;
        var hiddenFieldId = {
            xtype : 'hiddenfield',
            name : me.hiddenName,
            itemId : 'baseImageFieldHidden_'+me.hiddenName,
            allowBlank:me.allowBlank
        };
        // 根据页面上是否存在me.hiddenName对应的隐藏变量，如果存在，则不添加，防止重复。否则，则生成一个
        var win = o.ownerCt.ownerCt;
        var hiddenField = win.down("hiddenfield[name="+me.hiddenName+"]");
        if(hiddenField == null || Ext.typeOf(hiddenField) == 'undefined'){
            me.add(hiddenFieldId);
        }else{
            hiddenField.allowBlank = me.allowBlank;
            hiddenField.itemId = 'baseImageFieldHidden_'+me.hiddenName;
        }
    }
})
