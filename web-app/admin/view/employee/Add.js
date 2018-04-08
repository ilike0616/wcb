Ext.define("admin.view.employee.Add",{
    extend: 'Ext.window.Window',
    alias: 'widget.employeeAdd',
    requires: [
        'public.BaseSpecialTextfieldTree'
    ],
    modal: true,
    width: 530,
    layout: 'anchor',
    title: '添加员工',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyStyle: 'padding:5px 5px 5px',
                    layout: {
                        type: 'vbox'
                    },
                    fieldDefaults: {
                        labelWidth: 100,
                        width : 400
                    },
                    defaults: {
                        msgTarget: 'side', //qtip title under side
                        xtype: 'textfield'
                    },
                    items: [{
                            fieldLabel : '员工姓名',
                            name : 'name',
                            allowBlank : false,
                            beforeLabelTextTpl: required
                        },{
                            fieldLabel : '员工账号',
                            name : 'account'
                        },{
                            fieldLabel : '密码',
                            name : 'password',
                            inputType: 'password',
                            allowBlank : false,
                            beforeLabelTextTpl: required,
                            maxLength : 50
                        },{
                            fieldLabel : '邮箱',
                            name : 'email',
                            unique:true,
                            domainClass:'com.uniproud.wcb.Employee',
                            allUser:true
                        },{
                            fieldLabel : '邮箱认证',
                            name : 'checkEmail',
                            xtype : 'checkboxfield'
                        },{
                            fieldLabel : '电话',
                            name : 'phone'
                        },{
                            fieldLabel : '手机',
                            name : 'mobile',
                            unique:true,
                            domainClass:'com.uniproud.wcb.Employee',
                            allUser:true
                        },{
                            fieldLabel : '手机认证',
                            name : 'checkMobile',
                            xtype : 'checkboxfield'
                        },{
                            fieldLabel : '传真',
                            name : 'fax'
                        },{
                            fieldLabel : '是否启用',
                            name : 'enabled',
                            xtype : 'checkboxfield',
                            checked : true
                        },{
                            fieldLabel : '是否锁定',
                            name : 'accountLocked',
                            xtype : 'checkboxfield'
                        },{
                            fieldLabel: '所属企业',
                            name : 'user',
                            allowBlank : false,
                            xtype : 'combobox',
                            autoSelect : true,
                            forceSelection:true,
                            displayField : 'name',
                            valueField : 'id',
                            emptyText : '-- 请选择 --',
                            beforeLabelTextTpl: required,
                            store : Ext.create('admin.store.UserStore',{pageSize:9999999,
                                listeners:{
                                    load : function(){  // load数据时，把前台页面的数据对应的设置进去
                                        if(me.listDom) {
                                            var uv = me.listDom.selectedUser;
                                            if (uv != "" && uv != null) {
                                                me.down("combo[name=user]").setValue(uv);
                                            }
                                        }
                                    }
                                }
                            }),
                            minChars: 1,
                            queryParam : 'searchValue',
                            matchFieldWidth: true,
                            listConfig: {
                                loadingText: '正在查找...',
                                emptyText: '没有找到匹配的数据'
                            },
                            listeners:{
                                change:function(o, newValue, oldValue, eOpts){
                                    me.down('baseSpecialTextfieldTree[name=parentEmployee.name]').down('textfield').setValue(null);
                                    me.down('baseSpecialTextfieldTree[name=parentEmployee.name]').down('hiddenfield').setValue(null);
                                    me.down('baseSpecialTextfieldTree[name=dept.name]').down('textfield').setValue(null);
                                    me.down('baseSpecialTextfieldTree[name=dept.name]').down('hiddenfield').setValue(null);
                                }
                            }
                        },{
                            fieldLabel: '上级',
                            name : 'parentEmployee.name',
                            hiddenName:'parentEmployee',
                            xtype : 'baseSpecialTextfieldTree',
                            width :400,
                            border:0,
                            findCondition: [{xtype:'combobox'},{name:'user'}],
                            store : Ext.create('admin.store.EmployeeTreeStore')
                        },{
                            xtype: 'baseSpecialTextfieldTree',
                            name : 'dept.name',
                            hiddenName: 'dept',
                            fieldLabel: '所属部门',
                            width :400,
                            border:0,
                            findCondition: [{xtype:'combobox'},{name:'user'}],
                            store : Ext.create('admin.store.DeptStore')
                        },{
                            fieldLabel : '是否记录轨迹',
                            name : 'isLocus',
                            xtype : 'checkboxfield',
                            checked : true
                        }
                    ],
                    buttons: [{
                        text:'保存',itemid:'save',iconCls:'table_save',autoInsert:true,target:'employeeList'
                    }]
                }
            ]
        });
        me.callParent(arguments);
    }
})