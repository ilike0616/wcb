/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define('public.BaseTreeGrid', {
    extend: 'Ext.tree.Panel',
    alias : 'widget.baseTreeGrid',
    height: 300,
    useArrows: true,
    rootVisible: false,
    singleExpand: false,
    operateBtn : true,
    alertName : 'name',
    extraBtn:[],
    initComponent: function() {
        var me = this,
            columns=me.columns,moduleId,
            tbar=[];

        operateBtn = me.operateBtn;
        if(Ext.typeOf(me.showRowNumber) == 'undefined') me.showRowNumber = false;
        if(me.viewId){
            Ext.Ajax.request({
                url:'view?viewId='+me.viewId+"&showRowNumber="+me.showRowNumber,
                method:'POST',
                timeout:4000,
                async: false,
                success:function(response,opts){
                    var view = Ext.JSON.decode(response.responseText);
                    columns = view.columns;
                    moduleId = view.moduleId;
                    if(operateBtn == true){
                        tbar = view.tbar;
                    }
//                    window.sessionStorage.setItem(me.viewId,response.responseText);
                } ,
                failure:function(e,op){
                    Ext.Msg.alert("发生错误");
                }
            });
        }
        var btn = [];
        if(operateBtn == true&&(tbar==undefined||tbar.length==0)){
            btn = [
//        	       {xtype:'button',text:'添加',itemId:'addButton',iconCls:'table_add'},
//        	       {xtype:'button',text:'修改',itemId:'updateButton',iconCls:'table_save',disabled:true,autodisabled:true},
//        	       {xtype:'button',text:'删除',itemId:'deleteButton',iconCls:'table_remove',disabled:true,autodisabled:true}
            ];
        }

        if(me.extraBtn.length>0){
            btn = me.extraBtn;
        }
        if(tbar.length==0){
            tbar = btn;
        }
        var btnToolbar = Ext.create('Ext.toolbar.Toolbar', {
            border : 0,
            items : tbar
        });

        var contextMenu;
        if(tbar!=undefined&&tbar.length>0){
            var finalTbar = [];
            var menu = Ext.Array.every(tbar,function(o){ //单纯的遍历数组
                delete o.xtype;
                delete o.disabled;
                if(o.optRecords != 'no') finalTbar.push(o);
                return true;
            });
            contextMenu = Ext.create('Ext.menu.Menu', {
                auto:true,
                formGrid:me,
                items: finalTbar
            });
        }

        Ext.applyIf(me, {
            tbar : btnToolbar,
            columns:columns,
            moduleId:moduleId,
            listeners: {
                itemcontextmenu: function(view, rec, node, index, e) {
                    if(contextMenu){
                        contextMenu.itemId = rec.get('id');
                        e.stopEvent();
                        contextMenu.showAt(e.getXY());
                        return false;
                    }
                },
                afterrender:function(grid,eOpts){
                    // 锁定视图
                    var locked = grid.lockedGrid;
                    if(Ext.typeOf(locked) != 'undefined'){
                        var lockedView = locked.getView();
                        var lockedPanel = lockedView.panel;
                        // 获取所有的toolbar对象(包括分页条)
                        var toolbar = lockedPanel.getDockedItems('toolbar');
                        // 移除所有的toolbar对象
                        Ext.Array.each(toolbar,function(tb){
                            lockedPanel.removeDocked(tb);
                        })
                        // 移除tbar
                        var tbarContainer = lockedPanel.getDockedItems('container[itemId="tbarContainer"]');
                        lockedPanel.removeDocked(tbarContainer[0]);
                    }
                    // 正常视图(暂时保留)
                    var normal = grid.normalGrid;
                    if(Ext.typeOf(normal) != 'undefined'){
                        var normalView = normal.getView();
                        //console.info(normalView.panel.getDockedItems('container[itemId="tbarContainer"]'))
                    }

                }
            }
        });
        this.callParent();
    }
});