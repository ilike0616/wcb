/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.ShareController',{
    extend : 'Ext.app.Controller',
    views:['share.Main','share.List','share.Add','share.Edit','share.View','share.DetailList'] ,
    stores:[],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
            'shareMain shareList':{
                itemdblclick:function(grid, record, item, index, e, eOpts){
                    if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                        grid = grid.ownerCt;
                    }
                    var tabpanel = grid.up('shareMain').down('tabpanel');
                    if(tabpanel.hidden){
                        tabpanel.show();
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,eOpts);
                    }else{
                        tabpanel.hide();
                    }
                },
                select:function(selectModel, record, index, eOpts){
                    var tabpanel = selectModel.view.up('shareMain').down('tabpanel');
                    if(tabpanel.hidden==false){
                        var detail = tabpanel.down('shareDetailList');
                        if(detail){
                            var store = detail.down('dataview').getStore();
                            Ext.apply(store.proxy.extraParams,{shareId:record.get('id')});
                            store.load(function(records,operation, success){
                                detail.setTitle(detail.title1+"("+this.getTotalCount()+")");
                            });
                        }
                    }
                }
            },
            'shareMain shareList dataview':{
                itemkeydown:function( view, record, item, index, e, eOpts){
                    if(e.getKey()==e.Q||e.getKey()==e.TAB){
                        var tab = view.up('shareMain').down('tabpanel');
                        if(tab.hidden==false){
                            tab.setActiveTab(1);
                            var activeIndex = tab.activeIndex+1;
                            if(tab.items.getCount()==activeIndex){
                                activeIndex = 0;
                            }
                            tab.activeIndex = activeIndex;
                            tab.setActiveTab(activeIndex);
                        }else{
                            tab.activeIndex = 0;
                            tab.setActiveTab(0);
                            tab.show();
                        }
                        var grid = view.up('baseList');
                        if(grid.ownerCt.lockable == true){ // 如果有锁定列，则应该取父grid
                            grid = grid.ownerCt;
                        }
                        grid.fireEvent('select',grid.getSelectionModel(), record,index,view);
                    }else if(e.getKey()==e.ESC){
                        view.up('shareMain').down('tabpanel').hide();
                    }
                }
            },
            'shareList button[operationId=share_add]' :{
                click:function(btn){
                    var win = Ext.widget('shareAdd');
                    var urlTypeCom = win.down('combo[name=urlType]');
                    var urlTypeValue = 1;
                    if(urlTypeCom) urlTypeValue = urlTypeCom.getValue();
                    this.changeComponentShow(win,urlTypeValue);
                    win.show();
                }
            },
            'shareList button[operationId=share_update]' : {
                click: function (btn) {
                    var record = btn.up('shareList').getSelectionModel().getSelection()[0];
                    var win = Ext.widget('shareEdit');
                    win.show();
                    if(win.down('form')) win.down('form').loadRecord(record);

                }
            },
            'shareList button[operationId=share_view]' : {
                click: function (btn) {
                    var record = btn.up('shareList').getSelectionModel().getSelection()[0];
                    var win = Ext.widget('shareView');
                    this.changeComponentShow(win,record.get('urlType'));
                    win.show();
                    if(win.down('form')) win.down('form').loadRecord(record);
                }
            },
            'shareAdd combo[name=urlType]':{
                change:function(o, item, eOpts){
                    var win = o.up('window');
                    var urlTypeValue = o.getValue();
                    this.changeComponentShow(win,urlTypeValue);
                }
            },
            'shareEdit combo[name=urlType]':{
                change:function(o, item, eOpts){
                    var win = o.up('window');
                    var urlTypeValue = o.getValue();
                    this.changeComponentShow(win,urlTypeValue);
                }
            },
            'shareList button[operationId=share_share]' : {
                click: function (btn) {
                    var record = btn.up('shareList').getSelectionModel().getSelection()[0];
                    var id = record.get('id');
                    if(id){
                        window.open('share/initShare?id='+id);
                    }
                }
            }
        });
    },
    changeComponentShow:function(win,urlTypeValue){
        var label = "原创分享";
        var value = "";
        var htmlContentCom = win.down('htmleditor[name=htmlContent]');
        var contentCom = win.down('textarea[name=content]');
        var urlCom = win.down('textfield[name=url]');
        if (htmlContentCom) label = htmlContentCom.fieldLabel;
        var newHtmlComponent = Ext.widget('htmleditor', {
            fieldLabel: label,
            name: 'htmlContent',
            colspan: 2,
            value:value,
            width: 500
        });
        var form = win.down('form');
        if (urlTypeValue == 1 || urlTypeValue == null) {
            if (htmlContentCom) htmlContentCom.hide();
            if (contentCom) contentCom.show();
            if (urlCom) {
                urlCom.show();
                urlCom.allowBlank = false;
            }
        } else {
            form.remove(form.down('htmleditor[name=htmlContent]'));
            newHtmlComponent.hidden = false;
            form.insert(3, newHtmlComponent);
            if (contentCom) contentCom.hide();
            if (urlCom) {
                urlCom.hide();
                urlCom.allowBlank = true;
            }
        }
    }
})
