/**
 * Created by shqv on 2014-9-12.
 */
//Ext.override(Ext.form.field.Time, {
//   getSubmitValue: function() {
//       var format = this.submitFormat || this.format,
//           value = this.getValue();
//       return value ? Ext.Date.format(value, 'Y-m-d H:i:s') : '';
//   }
//});

Ext.override(Ext.layout.container.Table, {
	calculateCells: function(items) {
        var cells = [],rowPlus=false,
            rowIdx = 0,
            colIdx = 0,
            cellIdx = 0,
            totalCols = this.columns || Infinity,
            rowspans = [], //rolling list of active rowspans for each column
            i = 0, j,
            len = items.length,
            item;

        for (; i < len; i++) {
            item = items[i];
            rowPlus = false;
            // Find the first available row/col slot not taken up by a spanning cell
            while (colIdx >= totalCols || rowspans[colIdx] > 0) {
                if (colIdx >= totalCols) {
                    // move down to next row
                    colIdx = 0;
                    cellIdx = 0;
                    rowIdx++;
                    rowPlus = true;
                    // decrement all rowspans
                    for (j = 0; j < totalCols; j++) {
                        if (rowspans[j] > 0) {
                            rowspans[j]--;
                        }
                    }
                } else {
                    colIdx++;
                }
            }
            if(item.colspan>1){
            	colIdx = 0;
                cellIdx = 0;
                if(rowPlus==false) rowIdx++;
            }
            // Add the cell info to the list
            cells.push({
                rowIdx: rowIdx,
                cellIdx: cellIdx
            });

            // Increment
            for (j = item.colspan || 1; j; --j) {
                rowspans[colIdx] = item.rowspan || 1;
                ++colIdx;
            }
            ++cellIdx;
        }

        return cells;
    }
});
Ext.override(Ext.form.CheckboxGroup, {
	setValue: function(vals) {
		if(vals.indexOf(",")){
			vals = vals.split(',');
		}
		var a = [];
		if (Ext.isArray(vals)) {
			a = vals;
		} else {
			a = [vals];
		} 
		this.items.each(function(item) {
			for ( var i = 0 ; i < a.length ; i ++ ) {
				var val = a[i];
				if ( val == item.inputValue ) {
					item.setValue(true);
				}
			};
		});
	}
});
Ext.override(Ext.grid.column.Column,{
    getSortParam:function(){
        if(this.sortName){
            return this.sortName;
        }else{
            return this.dataIndex;
        }
    }
});


Ext.override(Ext.selection.RowModel,{
    onSelectChange: function(record, isSelected, suppressEvent, commitFn) {
        var me      = this,
            views   = me.views,
            viewsLn = views.length,
            rowIdx  = views[0].indexOf(record),
            eventName = isSelected ? 'select' : 'deselect',
            i = 0;

        if ((suppressEvent || me.fireEvent('before' + eventName, me, record, rowIdx)) !== false &&
            commitFn() !== false) {

            for (; i < viewsLn; i++) {
                if (isSelected) {
                    views[i].onRowSelect(rowIdx, suppressEvent);
                } else {
                    views[i].onRowDeselect(rowIdx, suppressEvent);
                }
            }
            if (!suppressEvent) {
                me.fireEvent(eventName, me, record, rowIdx,views[0]);
            }
        }
    }
});

Ext.override(Ext.form.Panel, {
    loadRecord: function (record) {
        var view = this;
        this.getForm().loadRecord(record);

        // 处理多图片上传
        this.baseUploadImageField(view,record);
        // 处理文件上传
        this.baseUploadField(view,record);
        // 处理图片
        this.baseImageField(view,record);
        // 处理textarea选中的问题
    },
    baseUploadField : function(view,record){
        var filesObj = view.query('baseUploadField panel');
        Ext.Array.each(filesObj, function(filePanel) {
            var basePanel = filePanel.ownerCt.ownerCt;
            var uploadBtn = basePanel.down("button[name=btn_"+filePanel.objectName+"]");
            var file_upload_limit = basePanel.file_upload_limit;
            var fileDocSeeds = [];
            var fileNames = [];
            var files = record.get(filePanel.objectHiddenName);
            if(Ext.typeOf(files) != 'undefined' && files != ''){
                Ext.Array.each(files,function(file){
                    var docSeed = file.id;
                    if(docSeed!="" && docSeed != 0){
                        fileDocSeeds.push({xtype:'hiddenfield',name:filePanel.objectHiddenName,value:docSeed,itemId:docSeed,width:200});
                        var showName = file.orgName;
                        if(file.file_upload_limit != "1"){    // 多个文件的话，截取名称
                            if(showName.length > 25){
                                showName = showName.substr(0,25)+"...";
                            }
                        }
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
                                html: "<a href='base/downloadFile?id="+docSeed+"'>" + showName +"</a>"
                            }, {
                                xtype: 'button',
                                iconCls:'trash',
                                itemId : docSeed,
                                listeners : {
                                    click : function(o){
                                        var items = filePanel.items.items;
                                        Ext.Array.each(items, function(it) {
                                            if(it.itemId == o.itemId){
                                                Ext.Array.each(basePanel.items.items, function(hidden) {  // 移除hidden域
                                                    if(it.itemId == hidden.itemId){
                                                        if(items.length == 1){
                                                            hidden.setValue(null);
                                                        }else{
                                                            basePanel.remove(hidden);
                                                        }
                                                        return false;
                                                    }
                                                });
                                                filePanel.remove(it);
                                                if(file_upload_limit == "1"){    // 如果只允许上传一个文件，移除时应该把上传按钮置为可用
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
                })
            }
            if(fileNames.length > 0){
                if(file_upload_limit == "1"){
                    uploadBtn.setDisabled(true);
                }
                filePanel.add(fileNames);
                basePanel.add(fileDocSeeds);
            }
        });
    },
    baseUploadImageField : function(view,record){
        var filesObj = view.query('baseUploadImageField panel');
        Ext.Array.each(filesObj, function(filePanel) {
            var basePanel = filePanel.ownerCt.ownerCt;
            var uploadBtn = basePanel.down("button[name=btn_"+filePanel.objectName+"]");
            var file_upload_limit = basePanel.file_upload_limit;
            var fileDocSeeds = [];
            var fileNames = [];
            var docSeedMap = new Ext.util.HashMap();
            var files = record.get(filePanel.objectHiddenName);
            if(Ext.typeOf(files) != 'undefined' && files != ''){
                Ext.Array.each(files,function(file){
                    var docSeed = file.id;
                    if(docSeed!="" && docSeed != 0){
                        docSeedMap.add(docSeed,"base/downloadFile?id="+docSeed);
                        fileDocSeeds.push({xtype:'hiddenfield',name:filePanel.objectHiddenName,value:docSeed,itemId:docSeed,width:200});
                        fileNames.push({
                            xtype: 'toolbar',
                            itemId: docSeed,
                            width:120,
                            layout: {
                                type: 'vbox',
                                pack:'center'
                            },
                            items: [{
                                xtype:'image',
                                id:'image_'+docSeed,
                                height:110,
                                width:110,
                                src:"base/downloadFile?id="+docSeed
                            },{
                                xtype:'container',
                                layout:'hbox',
                                width:120,
                                margin:'0 0 0 15',
                                items:[{
                                    xtype: 'component',
                                    html: "<a href='base/downloadFile?id="+docSeed+"' target='_target'>下载&nbsp;|</a>"
                                },{
                                    xtype: 'component',
                                    html: "<a href='#1' itemId="+docSeed+">&nbsp;删除</a>",
                                    listeners:{
                                        el:{
                                            click:function(o){
                                                var items = filePanel.items.items;
                                                Ext.Array.each(items, function(it) {
                                                    if(it.itemId == o.target.attributes['itemId'].value){
                                                        Ext.Array.each(basePanel.items.items, function(hidden) {  // 移除hidden域
                                                            if(it.itemId == hidden.itemId){
                                                                basePanel.remove(hidden);
                                                                return false;
                                                            }
                                                        });
                                                        filePanel.remove(it);
                                                        if(file_upload_limit == "1"){    // 如果只允许上传一个文件，移除时应该把上传按钮置为可用
                                                            uploadBtn.setDisabled(false);
                                                        }
                                                        return false;
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }]
                            }]
                        })
                    }
                })
            }
            if(fileNames.length > 0){
                if(file_upload_limit == "1"){
                    uploadBtn.setDisabled(true);
                }
                filePanel.add(fileNames);
                basePanel.add(fileDocSeeds);

                docSeedMap.each(function(key, value, length){
                    Ext.create('Ext.tip.ToolTip', {
                        target:'image_'+key,
                        anchor: 'left',
                        trackMouse:true,
                        items:[{
                            xtype:'image',
                            src: value,
                            maxWidth:500
                        }]
                    });
                });
            }
        });
    },
    baseImageField : function(view,record){
        var imageFieldPanels = view.query('baseImageField');
        Ext.Array.each(imageFieldPanels, function(imageFieldPanel) {
            var fieldcontainer = imageFieldPanel.down("fieldcontainer");
            var obj = record.get(fieldcontainer.objectHiddenName);
            if(obj == null) return;
            var image = fieldcontainer.down("image");
            var httpUrl = configParamsFactory.getParamValueByParamName('HTTP_FILE_URL');
            image.setSrc(httpUrl + "/" + obj.name);
            var hiddenField = view.down("hiddenfield[name="+fieldcontainer.objectHiddenName+"]");
            if(hiddenField == null){
                hiddenField = imageFieldPanel.down("hiddenfield[name="+fieldcontainer.objectHiddenName+"]");
            }
            if(hiddenField == null) return;
            hiddenField.setValue(obj.id);
        })
    }
});
Ext.override(Ext.util.Format,{
    usMoney : function(v) {
        var negativeSign = '',
            format = ",0",
            i = 0,
            currencySign='￥',
            decimals=2;
        v = v - 0;
        if (v < 0) {
            v = -v;
            negativeSign = '-';
        }
        format += (decimals > 0 ? '.' : '');
        for (; i < decimals; i++) {
            format += '0';
        }
        v = Ext.util.Format.number(v, format);
        return Ext.String.format("{0}{1}{2}", negativeSign, currencySign || currencySign, v);
    }
});

Ext.override(Ext.data.reader.Reader,{
    read: function(response) {
        var data;

        if (response) {
            data = response.responseText ? this.getResponseData(response) : this.readRecords(response);
        }
        return data || this.nullResultSet;
    },
    readRecords: function(data) {
        var me = this,
            success,
            recordCount,
            records,
            root,
            total,
            value,
            summary,
            message;

        /*
         * We check here whether fields collection has changed since the last read.
         * This works around an issue when a Model is used for both a Tree and another
         * source, because the tree decorates the model with extra fields and it causes
         * issues because the readers aren't notified.
         */
        if (me.lastFieldGeneration !== me.model.prototype.fields.generation) {
            me.buildExtractors(true);
        }

        /**
         * @property {Object} rawData
         * The raw data object that was last passed to {@link #readRecords}. Stored for further processing if needed.
         */
        me.rawData = data;

        data = me.getData(data);

        success = true;
        recordCount = 0;
        records = [];

        if (me.successProperty) {
            value = me.getSuccess(data);
            if (value === false || value === 'false') {
                success = false;
            }
        }

        if (me.messageProperty) {
            message = me.getMessage(data);
        }

        summary = data.summary

        // Only try and extract other data if call was successful
        if (me.readRecordsOnFailure || success) {
            // If we pass an array as the data, we dont use getRoot on the data.
            // Instead the root equals to the data.
            root = Ext.isArray(data) ? data : me.getRoot(data);

            if (root) {
                total = root.length;
            }

            if (me.totalProperty) {
                value = parseInt(me.getTotal(data), 10);
                if (!isNaN(value)) {
                    total = value;
                }
            }

            if (root) {
                records = me.extractData(root);
                recordCount = records.length;
            }
        }

        return new Ext.data.ResultSet({
            total  : total || recordCount,
            count  : recordCount,
            records: records,
            success: success,
            summary:summary,
            message: message
        });
    }
});

Ext.override(Ext.form.Basic,{
    isValid: function() {
        var me = this,
            invalid,validCount=0;
        Ext.suspendLayouts();
        var errorMsg = '<div class="x-form-error-msg x-form-invalid-icon" ' +
            'data-errorqtip="<ul class=&quot;x-list-plain&quot;><li>该输入项为必输项</li></ul>"></div>';

        invalid = me.getFields().filterBy(function(field) {
            var itemIdType = Ext.typeOf(field.itemId);
            if(field.itemId && itemIdType=='string' && field.itemId.indexOf('baseImageFieldHidden_')==0){
                var allowBlank = field.allowBlank;
                if(allowBlank == false && !field.value){
                    validCount++;
                    field.up('baseImageField').down('label#error_'+field.name).el.dom.innerHTML = errorMsg;
                }
            }else if(field.itemId && itemIdType=='string' && field.itemId.indexOf('tempBaseUploadFieldHidden_')==0){
                var allowBlank = field.allowBlank;
                var valueObj = field.up('window').query('hiddenfield[name='+field.tempName+']');
                if(allowBlank == false && (!valueObj || valueObj.length <= 0)){
                    validCount++;
                    field.up('baseUploadField').down('label#error_'+field.tempName).el.dom.innerHTML = errorMsg;
                }
            }else if(field.itemId && itemIdType=='string' && field.itemId.indexOf('tempBaseUploadImageFieldHidden_')==0){
                console.info("111");
                var allowBlank = field.allowBlank;
                var valueObj = field.up('window').query('hiddenfield[name='+field.tempName+']');
                if(allowBlank == false && (!valueObj || valueObj.length <= 0)){
                    validCount++;
                    field.up('baseUploadImageField').down('label#error_'+field.tempName).el.dom.innerHTML = errorMsg;
                }
            }else{
                return !field.validate();
            }
        });
        Ext.resumeLayouts(true);
        return (invalid.length + validCount) < 1;
    },
    setValues: function(values) {
        var me = this,
            v, vLen, val, field;

        function setVal(fieldId, val) {
            if(Ext.isObject(val)){                          //添加处理关联对象
                Ext.iterate(val,function(oname,oval){
                    var field;
                    if(oname == 'id'){
                        field = me.findField(fieldId);
                    }else{
                        field = me.findField(fieldId+'.'+oname);
                    }
                    if (field ) {
                        field.setValue(oval);
                        if (me.trackResetOnLoad) {
                            field.resetOriginalValue();
                        }
                    }
                });
            }else{
                var field = me.findField(fieldId);
                if (field) {
                    field.setValue(val);
                    if (me.trackResetOnLoad) {
                        field.resetOriginalValue();
                    }
                }
            }
        }

        // Suspend here because setting the value on a field could trigger
        // a layout, for example if an error gets set, or it's a display field
        Ext.suspendLayouts();
        if (Ext.isArray(values)) {
            // array of objects
            vLen = values.length;

            for (v = 0; v < vLen; v++) {
                val = values[v];
                setVal(val.id, val.value);
            }
        } else {
            // object hash
            Ext.iterate(values, setVal);
        }
        Ext.resumeLayouts(true);
        return this;
    }
});

Ext.override(Ext.form.field.Text, {
    getErrors: function(value) {
        var me = this,
            errors = me.callParent(arguments),
            validator = me.validator,
            vtype = me.vtype,
            vtypes = Ext.form.field.VTypes,
            regex = me.regex,
            format = Ext.String.format,
            msg, trimmed, isBlank;
        value = value || me.processRawValue(me.getRawValue());
        trimmed = me.allowOnlyWhitespace ? value : Ext.String.trim(value);
        if(me.up('form') && trimmed.length < 1) {
            var associatedRequired = me.up('form').associatedRequired
            if (associatedRequired && Ext.Array.contains(Ext.Array.flatten(associatedRequired), me.name)) {
                Ext.Array.each(associatedRequired,function(requires,index,iteself){
                    if(Ext.Array.contains(requires,me.name)) {
                        var flag = false;
                        var requiredMsg = "";
                        Ext.Array.each(requires, function (fieldName, idx, self) {
                            var field = me.up('form').down('field[name='+fieldName+']');
                            if(field != null) {
                                if (requiredMsg != "") {
                                    requiredMsg += ","
                                }
                                requiredMsg += field.fieldLabel
                                if (fieldName != me.name) {
                                    var associatedValue = field.getValue();
                                    associatedValue = associatedValue || field.processRawValue(field.getRawValue());
                                    associatedValue = field.allowOnlyWhitespace ? associatedValue : Ext.String.trim(associatedValue);
                                    var type = field.xtype;
                                    switch (type) {
                                        case 'textfield':
                                        case 'textarea':
                                        case 'textareafield':
                                        case 'baseUploadField':
                                            if (associatedValue.length > 0) {
                                                flag = true;
                                            }
                                            break;
                                        case 'combo':
                                        case 'combobox':
                                        case 'baseCascadeCombo':
                                        case 'numberfield':
                                        case 'checkbox':
                                        case 'checkboxfield':
                                            if (associatedValue != '') {
                                                flag = true;
                                            }
                                            break;
                                    }
                                }
                            }
                        });
                        if(!flag){
                            if(requiredMsg.indexOf(',') > 0)
                                errors.push(requiredMsg+"至少输入一项");
                            else
                                errors.push('该输入项为必填项');
                        }
                    }
                });
            }
        }else if(me.up('form')){
            var associatedRequired = me.up('form').associatedRequired
            if (associatedRequired && Ext.Array.contains(Ext.Array.flatten(associatedRequired), me.name)) {
                Ext.Array.each(associatedRequired,function(requires,index,iteself){
                    if(Ext.Array.contains(requires,me.name)) {
                        Ext.Array.each(requires, function (fieldName, idx, self) {
                            var field = me.up('form').down('field[name='+fieldName+']');
                            if (field != null && fieldName != me.name) {
                                field.clearInvalid();
                            }
                        });
                    }
                });
            }
        }
        if(me.unique && trimmed.length > (me.minLength?me.minLength:0)){
            var form = me.up('form'),
                moduleId = form.moduleId,
                domainClass = me.domainClass,
                allUser = me.allUser,
                id;
            if(form.getRecord()){
                id=form.getRecord().get('id');
            }
            var notExist = true;
            Ext.Ajax.request({
                url: 'base/uniqueVerify',
                params: {fieldName:me.name,value: trimmed,moduleId:moduleId,domainClass:domainClass,allUser:allUser,id:id},
                method: 'POST',
                async:false,
                timeout: 4000,
                success: function (response, opts) {
                    var d = Ext.JSON.decode(response.responseText);
                    if (d.success) {
                        notExist = true;
                    } else {
                        notExist = false;
                    }
                },
                failure: function (response, opts) {
                    notExist = true;
                }
            });
            if(!notExist){
                if(me.uniqueText){
                    errors.push(me.uniqueText);
                }else {
                    errors.push("您输入的的值已存在，请重新输入！");
                }
            }
        }
        return errors;
    }
});

Ext.override(Ext.form.field.Text, {
    checkDirty: function() {
        var me = this,
            isDirty = me.isDirty();
        if (isDirty !== me.wasDirty) {
            me.fireEvent('dirtychange', me, isDirty);
            me.onDirtyChange(isDirty);
            me.wasDirty = isDirty;
            Ext.defer(function(){
                if(Ext.typeOf(me.ownerCt) != 'undefined' && Ext.typeOf(me.ownerCt.editingPlugin) != 'undefined'){
                    if(me.isValid() == false){
                        var tip = new Ext.tip.ToolTip({
                            cls: Ext.baseCSSPrefix + 'grid-row-editor-errors',
                            title: this.errorsText,
                            closable: true,
                            anchor: 'left',
                            anchorToTarget: false
                        });
                        tip.showAt([0, 0]);
                        tip.update(me.getErrors());

                        var context = me.ownerCt.editingPlugin.context,
                            row = Ext.get(context.row),
                            viewEl = me.ownerCt.editingPlugin.view.el,
                            viewHeight = viewEl.dom.clientHeight,
                            viewTop = 10,
                            viewBottom = viewTop + viewHeight,
                            rowHeight = row.getHeight(),
                            rowTop = row.getOffsetsTo(me.ownerCt.editingPlugin.view.body)[1],
                            rowBottom = rowTop + rowHeight;
                        if (rowBottom > viewTop && rowTop < viewBottom) {
                            tip.showAt(tip.getAlignToXY(viewEl, 'tl-tr', [15, row.getOffsetsTo(viewEl)[1]]));
                        } else {
                            tip.hide();
                        }
                        tip.enable();
                    }
                }
            },50)
        }
    }
});

Ext.override(Ext.grid.locking.Lockable, {
    lockText:'锁定',
    unlockText:'解锁'
});

/**
 * 自定义验证：验证两次输入的密码是否一致
 */
Ext.apply(Ext.form.field.VTypes, {
    validPwd: function(val, field) {
        var form = field.up('form');
        var pwd = form.down('textfield[name=newPassword]').getValue();
        var confirmPwd = form.down('textfield[name=confirmPassword]').getValue();
        if(pwd != confirmPwd){
            return false;
        }
        return true;
    },
    validPwdText: '两次密码输入不一致，请重新输入！'
});