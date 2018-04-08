/**
 * Created by guozhen on 2015/07/23.
 */
Ext.define('user.controller.AimPerformanceController',{
    extend : 'Ext.app.Controller',
    views:['aimPerformance.Main','aimPerformance.List','aimPerformance.Chart','aimPerformance.Detail'] ,
    stores:[],
    models:[],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'aimPerformanceList button#aimDetailButton':{
                click:function(btn){
                    var grid = btn.up('aimPerformanceList');
                    var year = grid.down('numberfield[name=aimYear]').getValue();
                    var record = grid.getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(record) == 'undefined'){
                        alert("请选择要查看明细的记录!");
                        return;
                    }
                    Ext.widget('aimPerformanceDetail',{
                        owner:record.get('owner'),
                        ownerName:record.get('ownerName'),
                        aimYear:year,
                        aimType:record.get('aimType')
                    }).show();
                }
            },
            'aimPerformanceMain aimPerformanceList':{
                select : function(o, record, index, eOpts ){
                    var main = o.view.up('aimPerformanceMain');
                    var aimPerformanceList = main.down('aimPerformanceList');
                    var aimYear = aimPerformanceList.down('numberfield[name=aimYear]').getValue();
                    var chart = main.down('aimPerformanceChart');
                    chart.loadData(record,aimYear);
                }
            }
        });
    }
})
