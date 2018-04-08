Ext.define('user.view.audit.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.auditMain',
    layout: 'border',
    items: [{
        region: 'center',
        xtype: 'tabpanel',
        tabPosition: 'top',
        items: [{
            title: '全部',
            auditType: '3',
            xtype: 'auditList',
            viewId: 'AuditSendList',
            store: Ext.create('user.store.AuditStore')
        }, {
            title: '待审',
            auditType: '1',
            xtype: 'auditList',
            viewId: 'AuditingList',
            store: Ext.create('user.store.AuditStore')
        }, {
            title: '已审',
            auditType: '2',
            xtype: 'auditList',
            viewId: 'AuditedList',
            store: Ext.create('user.store.AuditStore')
        }]
    }]
});