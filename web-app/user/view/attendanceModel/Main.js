Ext.define('user.view.attendanceModel.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.attendanceModelMain',
    layout : {
        type: 'border',
        pack : 'start ',
        align: 'stretch'
    },
    defaults:{
        split:true,
        border: false
    },
    items: [
        {
            xtype: 'attendanceModelList',
            store:Ext.create('user.store.AttendanceModelStore'),
            title:'',
            region: 'center',
            flex : 1 ,
            split: true,
            floatable: true
        },
        {
        	xtype:'tabpanel',
        	hidden:true,
        	activeIndex:0,
        	region: 'south',
        	flex : 1,
        	split: true,
            floatable: true,
        	items:[
        	    {
    				xtype: 'employeeList',
    				store:Ext.create('user.store.EmployeeStore'),
    			    enableSearchField:false,//false 关闭搜索框
    			    enableComplexQuery:false//false 关闭查询功能
        	    }
        	]
        } 
    ]
});