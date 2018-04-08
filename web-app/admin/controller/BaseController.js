/**
 * Created by shqv on 2014-9-11.
 */
Ext.define('admin.controller.BaseController', {
    extend: 'Ext.app.Controller',
    
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    init:function() {
        this.control({
        	'field':{
        		 render: function (field, p) {
        			 if(field.note){
	                     Ext.create('Ext.tip.ToolTip', {
	                    	 target: field.el,
	                    	 html: field.note
	                	 }); 
        			 }
                  }
        	},
            'grid[renderLoad=true]':{
                render:function(grid){
                    grid.getStore().load();
                }
            },
            'baseTreeGrid':{
                select:function(selectModel, record, index, eOpts){
                    Ext.Array.forEach(eOpts.up('treepanel').query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(false);
                    });
                }, deselect:function(selectModel, record, index, eOpts){
                    Ext.Array.forEach(eOpts.up('treepanel').query('button[autodisabled=true]'),function(o,index){
                        o.setDisabled(true);
                    });
                }
            },
            'baseList':{
                select:function(selectModel, record, index, eOpts){
                    var grid = selectModel.view.up('grid');
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var selectedCount = eOpts.up('baseList').getSelectionModel().getSelection().length;
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        if(selectedCount > 1){  // 选中条数大于1，则optRecords='many'的可用，其他的不可用
                            if(o.optRecords == 'many'){
                                o.setDisabled(false);
                            }else if(o.optRecords == 'one'){
                                o.setDisabled(true);
                            }
                        }else{  // 选中条数等于1，则所有的都可用
                            o.setDisabled(false);
                        }
                    });
                }, deselect:function(selectModel, record, index, eOpts){
                    var grid = selectModel.view.up('baseList');
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var selectedCount = 0;
                    var selectedRecord = eOpts.up('baseList').getSelectionModel().getSelection();
                    if(Ext.typeOf(selectedRecord) != 'undefined') selectedCount = selectedRecord.length;
                    Ext.Array.forEach(grid.query('button[autodisabled=true]'),function(o,index){
                        if(selectedCount == 0){ // 最后一条选择的被取消，则全部不可用
                            o.setDisabled(true);
                        }else if(selectedCount == 1){ // 当前就剩一条选中
                            o.setDisabled(false);
                        }else{ // 当前还有多条记录被选中，则optRecords='many'的可用，其他的不可用
                            if(o.optRecords == 'one'){
                                o.setDisabled(true);
                            }else if(o.optRecords == 'many'){
                                o.setDisabled(false);
                            }
                        }
                    });
                }
            },
            'baseList dataview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    var baseList = view.up('baseList');
                    if(baseList){
                        if(e.getKey()==e.ESC){
                            baseList.getSelectionModel().deselectAll();
                        }else if(e.getKey()==e.ENTER){
                            baseList.fireEvent('itemdblclick',baseList);
                        }else if(e.getKey()==e.R){
                            view.up('grid').getStore().load();
                        }else if(e.getKey()==e.RIGHT){
                            if(baseList.down('pagingtoolbar'))baseList.down('pagingtoolbar').moveNext();
                        }else if(e.getKey()==e.LEFT){
                            if(baseList.down('pagingtoolbar'))baseList.down('pagingtoolbar').movePrevious();
                        }
                        view.focus(false,1000);
                    }
                }
            },
            'button[autoInsert=true]':{
                click:function(btn){
                    this.GridDoActionUtil.doInsert(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'));
                }
            },
            'button[autoUpdate=true]':{
                click:function(btn){
                    this.GridDoActionUtil.doUpdate(Ext.ComponentQuery.query(btn.target)[0],btn.up('window').down('form'),btn.up('window'));
                }
            },
            'button[autoDelete=true]':{
                click:function(btn){
                    var grid = btn.up('gridpanel')
                    if(Ext.typeOf(grid) == 'undefined'){
                        grid = btn.up('baseTreeGrid');
                    }
                    this.GridDoActionUtil.doDelete(grid,grid.alertName,btn);
                }
            },
            'button[autoShowAdd=true]':{
                click:function(btn){
                    if(btn.target) {
                        var  baseList = btn.up('baseList');
                        var view = Ext.widget(btn.target,{listDom:baseList});
                        view.show();
                    }else{
                        Ext.Msg.alert('提示', '按钮的target属性未指定！');
                    }
                }
            },
            'button[autoShowEdit=true]':{
                click:function(btn){
                    var grid = btn.up('gridpanel');
                    var record = grid.getSelectionModel().getSelection()[0]
                    if(btn.target) {
                        var view = Ext.widget(btn.target,{listDom:grid});
                        view.down('form').loadRecord(record);
                        view.show();
                    }else{
                        Ext.Msg.alert('提示', '按钮的target属性未指定！');
                    }
                }
            },
            'baseForm':{
                afterrender:function(form){
                    if(form.up("window")){
                        if(form.changeWinTitle)	form.up("window").setTitle(form.winTitle);
                        if(form.up("window").listDom&&form.up("window").optType=='add'){
                            form.form.setValues(form.up("window").listDom.initValues);
                        }
                    }
                    //form宽度加20个像素，为Y滚动条预留位置
                    form.setWidth(form.getWidth()+20);
                }
            }
        });
    }
});