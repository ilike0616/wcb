/**
 * Created by guozhen on 2015-01-13.
 */
Ext.define('agent.view.loginLog.List', {
    extend: 'public.BaseList',
    alias: 'widget.loginLogList',
    autoScroll: true,
    store: Ext.create('agent.store.LoginLogStore'),
    title: '用户登录日志',
    forceFit:true,
    tbar:[
    ],
    columns:[
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },{
            text:'员工名称',
            dataIndex:'employeeName',
            sortable:false
        },{
            text:'企业名称',
            dataIndex:'userName',
            sortable:false
        },{
            text:'登录IP',
            dataIndex:'loginIp'
        },{
            text:'登录方式',
            dataIndex:'loginKind',
            renderer:function(value){
                if (value === 1) {
                    return '电脑';
                }
                return '手机';
            }
        },{
            text:'类型',
            dataIndex:'loginType',
            renderer:function(value){
                if (value === 1) {
                    return '登录成功';
                }
                return '安全退出';
            }
        },{
            text:'登录时间',
            dataIndex:'loginTime',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        },{
            text:'手机型号',
            dataIndex:'phoneModel'
        },{
            text:'mac地址',
            dataIndex:'macAddress'
        },{
            text:'创建时间',
            dataIndex:'dateCreated',
            xtype:'datecolumn',
            format:'Y-m-d H:i:s'
        }
    ]
});