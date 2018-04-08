/**
 * Created by guozhen on 2014/12/17.
 */
Ext.define('user.view.schedule.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.scheduleMain',
    layout: 'border',
    items: [{
        region:'center',
        split: true,
        layout: 'fit',
        xtype: 'tabpanel',
        tabBar: {
            padding: '0 10 0 0',
            items: [
                {xtype: 'tbfill'},
                {
                    xtype: 'baseSpecialTextfield',
                    itemId:'ownerCombo',
                    name:'owner.name',
                    storeObjectName:'Employee',
                    store:'EmployeeSubStore',
                    viewId:'EmployeeList',
                    hiddenName:'owner'
                }
            ]
        },
        items : [{
            title: '日程安排',
            itemId:'schedulePlanMain',
            xtype:'schedulePlanMain'
        },
        {
            title: '日程总结',
            itemId:'scheduleSummaryMain',
            xtype:'scheduleSummaryMain'
        }]
    }]
});