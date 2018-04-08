/**
 * Created by guozhen on 2015/07/23.
 */
Ext.define('user.controller.SaleAimController',{
    extend : 'Ext.app.Controller',
    views:['saleAim.List','saleAim.Set'] ,
    stores:[],
    models:[],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'saleAimList button#aimSetButton':{
                click:function(btn){
                    var grid = btn.up('saleAimList');
                    var year = grid.down('numberfield[name=aimYear]').getValue();
                    var record = grid.getSelectionModel().getSelection()[0];
                    if(Ext.typeOf(record) == 'undefined'){
                        alert("请选择要设置销售目标的记录!");
                        return;
                    }
                    Ext.widget('saleAimSet',{
                        owner:record.get('owner'),
                        ownerName:record.get('ownerName'),
                        aimType:record.get('aimType'),
                        aimYear:year,
                        parentGrid:grid
                    }).show();
                }
            }
        });
    }
})
