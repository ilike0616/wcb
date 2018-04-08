/**
 * Created by like on 2015/6/26.
 */
Ext.define('public.BaseSummary', {
    extend: 'Ext.Toolbar.TextItem',
    alias: 'widget.baseSummary',
    enableAllSummary: true,
    dock: 'bottom',
    border:'0 0 1 0',
    style: {
        borderColor:'#CCC',
        borderStyle: 'solid',
        backgroundColor: '#FFFFFF'
    },
    height: 50,
    initComponent:function() {
        var me = this,
            gridDom = me.gridDom,
            store = gridDom.store;

        var allSummaryBar = '';
        if (me.enableAllSummary == true) {
            allSummaryBar = '<tr class=""><td id="allSummary" style="text-align:center;height: 25px;width: 100%"></td></tr>';
        }
        Ext.apply(me,{
            text:[
               '<div id="{id}-innerCt">'+
               '<table cellPadding="0" cellSpacing="0" style="width: 100%">'+
               '<tr class=""><td id="pageSummary" style="text-align:center;height: 25px;width: 100%;"></td></tr>'+
               allSummaryBar+
               '</table>'+
               '</div>'
           ]
        });
        gridDom.on({
            afterrender: function () {
                gridDom.mon(store, 'load', function (store, record, operation, eOpts) {
                    me.onStoreUpdate(store, record, operation, eOpts, gridDom);
                });
                //gridDom.mon(store, 'update', function (store, record, operation, eOpts) {
                //    me.onStoreUpdate(store, record, operation, eOpts, gridDom);
                //});
            }
        });
        me.callParent(arguments);
    },
    onStoreUpdate: function (store, record, operation, eOpts, gridDom) {
        var me = this,pageBar,
            pageData = me.selectPageSummaryData(gridDom),
            allData = me.selectAllSummaryData(gridDom),
            pageBar = me.el.down('#pageSummary'),
            allBar = me.el.down('#allSummary');
        if (pageBar) {//本页的汇总
            var showPage = []
            Ext.Array.each(pageData, function (obj, index, all) {
                showPage.push(obj.text + ':' + '<font style="color: #ffa200;">' + obj.summary + '</font>');
            });
            if(showPage.length>0){
                pageBar.setHTML((me.enableAllSummary == true ?' <b>∑本页</b>  ':'') + showPage.join('  '));
            }
        }
        if (allBar) {//所有页的汇总
            var showAll = []
            Ext.Array.each(allData, function (obj, index, all) {
                showAll.push(obj.text + ':' + '<font style="color: #ffa200;">' + obj.summary + '</font>');
            });
            if(showAll.length>0){
                allBar.setHTML(' <b>∑全部</b>  ' + showAll.join('  '));
            }
        }
    },
    /**
     * 本页汇总数据
     * @param view
     * @returns {view.store.model|*}
     */
    selectPageSummaryData: function (gridDom) {
        var view = gridDom.view,
            columns = view.headerCt.getVisibleGridColumns(),
            store = gridDom.store,
            info = {
                records: store.getRange()
            },
            colCount = columns.length, i, column, sumData = [];
        //根据页面显示的列遍历，可排除因视图或权限不显示的列也显示汇总
        for (i = 0; i < colCount; i++) {
            column = columns[i];
            if (column.summaryType) {
                store.getSum(store.getRange(),column.dataIndex);
                var pageSummary = store.getSum(store.getRange(),column.dataIndex);
                sumData.push({text: column.text, summary: Ext.util.Format.usMoney(pageSummary)});
            }
        }
        return sumData;
    },
    /**
     * 所有页的汇总数据
     * @param view
     * @returns {Array}
     */
    selectAllSummaryData: function (gridDom) {
        var view = gridDom.view,
            columns = view.headerCt.getVisibleGridColumns(),
            store = gridDom.store,
            colCount = columns.length, i, column,
            remoteData=store.proxy.reader.rawData.summary,
            sumData = [];
        //根据页面显示的列遍历，可排除因视图或权限不显示的列也显示汇总
        for (i = 0; i < colCount; i++) {
            column = columns[i];
            if (column.dataIndex && column.summaryType) {
                Ext.Array.each(remoteData, function (obj, index, countriesItSelf) {
                    if (obj.fieldName == column.dataIndex) {
                        //allSummaryRecord.set(column.dataIndex, allSummary==null?'':Number(allSummary).toFixed(2));
                        sumData.push({text: column.text, summary:Ext.util.Format.usMoney(obj.summary)});
                    }
                });
            }
        }
        return sumData;
    }
});