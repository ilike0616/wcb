/**
 * Created by like on 2015-04-15.
 */
Ext.define("user.view.notifyModel.FilterMain",{
    extend: 'public.BaseWin',
    width: 850,
    height: 600,
    alias: 'widget.notifyModelFilterMain',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'notifyModelFilterTree',
        title:'',
        flex: 2
    },{
        xtype: 'notifyModelFilterDetail',
        flex: 3
    }]
});