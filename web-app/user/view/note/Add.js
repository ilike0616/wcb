Ext.define("user.view.note.Add",{
    extend: 'public.BaseWin',
    alias: 'widget.noteAdd',
    requires: [
               'public.BaseComboBoxTree',
               'public.BaseForm'
    ],
    title: '添加我的客户',
    items: [
       {
           xtype: 'baseForm',
           paramColumns: 1,
           items: [{
        	   xtype: "hidden",
        	   name:"customer"
           },{
        	   xtype: "hidden",
        	   name:"customer.name"
           },{
               fieldLabel: "标签",
               name: "tag",
               xtype: "combo",
               note: null,
               autoSelect: true,
               forceSelection: true,
               typeAhead: true,
               emptyText: "-- 请选择 --",
               store: [
                   [
                       1,
                       "汇报记录"
                   ],
                   [
                       2,
                       "打电话"
                   ],
                   [
                       3,
                       "见面拜访"
                   ],
                   [
                       4,
                       "活动"
                   ],
                   [
                       5,
                       "商务宴请"
                   ]
               ]
           },{
        	   fieldLabel: "随笔",
        	   xtype: 'textarea',
    		   name: "content"
           }],
           buttons: [{
               text:'保存',itemId:'save',iconCls:'table_save',target:'noteList'
               }]
           }
       ]
});
