Ext.define('agent.view.addBalance.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.addBalanceMain',
    layout: 'border',
    items: [
        {
            region:'center',
            xtype:'tabpanel',
            layout: 'fit',
            items:[
                {
                    xtype: 'addBalanceList',
                    store : Ext.create('admin.store.AddBalanceStore'),
                    title: '全部',
                    addBalanceKind:0,
                    split: true
                },
                {
                    xtype: 'addBalanceList',
                    store : Ext.create('admin.store.AddBalanceStore'),
                    title: '我的充值',
                    addBalanceType:'my',
                    split: true
                },
                {
                    xtype: 'addBalanceList',
                    store : Ext.create('admin.store.AddBalanceStore'),
                    title: '下级代理商充值',
                    addBalanceKind:3,
                    split: true
                },
                {
                    xtype: 'addBalanceList',
                    store : Ext.create('admin.store.AddBalanceStore'),
                    title: '企业充值',
                    addBalanceKind:4,
                    split: true
                }
            ]
        }
    ]
});