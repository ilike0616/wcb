/**
 * Created by guozhen on 2015/4/22.
 */
Ext.define('user.view.workDay.Month', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workDayMonth',
    title: '考勤工作日',
    forceFit: true,
    layout: 'fit',
    initComponent: function () {
        var me = this;
        Ext.apply(me, {
            items: [
                {
                    xtype: 'dataview',
                    data: {
                        month:[
                            [{dx:'一月',xx:1}, {dx:'二月',xx:2}, {dx:'三月',xx:3}, {dx:'四月',xx:4}],
                            [{dx:'五月',xx:5}, {dx:'六月',xx:6}, {dx:'七月',xx:7}, {dx:'八月',xx:8}],
                            [{dx:'九月',xx:9}, {dx:'十月',xx:10}, {dx:'十一月',xx:11}, {dx:'十二月',xx:12}]
                        ]
                    },
                    tpl: me.monthTpl,
                    listeners: {
                        el: {
                            click: function(e, t, eOpts) {
                                alert(t.textContent);
                                alert(t.id);
                            }
                        }
                    }
                }
            ]
        });
        me.callParent(arguments);
    },
    monthTpl:Ext.create('Ext.XTemplate',
        '<tpl for="month">',
            '<table width="100%" cellpadding="0" cellspacing="0" style="top:{[this.getRowTop(xindex, xcount)]}%;height:{[this.getRowHeight(xcount)]}%;">',
                '<tbody>',
                    '<tr>',
                        '<tpl for=".">',
                            '<td width="25%" style="border:1px dashed red;" align="center" id="{xx}">{dx}</td>',
                        '</tpl>',
                    '</tr>',
                '</tbody>',
            '</table>',
        '</tpl>',{
            getRowTop: function(i, ln){
                return ((i-1)*(100/ln));
            },
            getRowHeight: function(ln){
                return 100/ln;
            }
        }
    )
})