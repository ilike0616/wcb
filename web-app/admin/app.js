Ext.override(Ext.Ajax, {
    defaultHeaders : {u:'admin'}
});
Ext.QuickTips.init();
Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', 'res/ux');
Ext.Loader.setPath('public', 'public');
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.Ajax.on('requestexception',function( conn, response, options, eOpts ){
    if(response.status==401) {
        Ext.Msg.show({
            modal: true,
            title: '系统超时',
            msg: "由于您长时间未操作，系统为保护您的数据免受侵害，请重新登陆！",
            icon: Ext.MessageBox.WARNING,
            closable: false,
            buttons: Ext.Msg.OK,
            fn:function(){
                Ext.Ajax.request({
                    url: 'logout/admin',
                    success: function(conn, response, options, eOpts){
                        var result = Util.Util.decodeJSON(conn.responseText);
                        if (result.success) {
                            Ext.ComponentQuery.query('adminviewport')[0].destroy();
                            window.location.reload();
                        } else {
                            Util.Util.showErrorMsg(result.msg);
                        }
                    },
                    failure: function(conn, response, options, eOpts) {
                        Util.Util.showErrorMsg(conn.responseText);
                    }
                });
            }
        });
        //Ext.widget('login',{modal: true});
    }
}, this);
Ext.onReady(function(){
    // Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this); 
    Ext.application({
        name: 'admin',
        appFolder: 'admin',
        autoCreateViewport: false,
        controllers: [
            'LoginController','BaseController'
        ],
        requires: [
            'public.column.RowSelecter','public.form.field.DateTime','public.BaseSpecialTextfield','public.BaseFieldSet'
            ,'public.BaseImageField','public.BaseImageField2','public.BaseFieldSet','public.uploadPanel.BaseUploadField'
            ,'public.AddBalance','public.ReduceBalance','public.BaseCascadeCombo','public.BaseMultiSelectTextareaField'
            ,'public.BaseHiddenField','public.column.ArrayColumn'
        ],
        localStorage : window.localStorage,
        launch:function(){
            var app = this;
            Ext.tip.QuickTipManager.init();
            Ext.Ajax.request({
                url: 'login/checkAdmin',
                success: function(response,opts){
                    var result = Util.Util.decodeJSON(response.responseText);
                    if (result.success) {
                        app.getController("UserController");
                        app.getController("OperationController");
                        app.getController("ViewOperationController");
                        app.getController("LayoutController");
                        Ext.create('admin.view.Viewport');
                        Util.SessionMonitor.start();
                    } else {
                        Ext.widget('login');
                    }
                },failure: function(response,opts){
                    Util.Util.showErrorMsg(response.responseText);
                }
            });
        },
        widget: function(tabPanel, controllerName, widgetName, record, cfg) {
            var findRes = this.findTab(tabPanel, record);
            if (findRes) {
                this.activateTab(tabPanel, findRes);
            } else {
                var ctrl = this.getController(controllerName),
                    tab = Ext.widget(widgetName, {record: record, closable: true});
                if (cfg) {
                    Ext.apply(tab, cfg);
                }
                tabPanel.setActiveTab(tabPanel.add(tab));
            }
        },
        findTab: function(tabPanel, record) {
            var ret,
                activeTab = tabPanel.getActiveTab();
            if (activeTab && activeTab.record === record) {
                return activeTab;
            }
            tabPanel.items.each(function(t, idx) {
                if (t && t.record === record) {
                    ret = t;
                    return false;
                }
            });
            return ret;
        },
        activateTab: function(tabPanel, targetTab) {
            if (targetTab) {
                tabPanel.setActiveTab(targetTab);
                return true;
            }
            return false;
        },
        storeSet : function(key, value) {
            this.getApplication().localStorage.setItem(key, value);
        },
        storeGet : function(key) {
            return this.getApplication().localStorage.getItem(key);
        }

    });
/*function checkUserSessionStatus(conn,response,options){
    if(response.getAllResponseHeaders().sessionstatus){     
        alert('您的登录已超时，系统即将关闭，请重新开启浏览器登录');  
        top.location='u.html'; 
    } 
}*/
//    Ext.state.Manager.setProvider(  new Ext.state.CookieProvider({expires: new Date(new Date().getTime()+(1000*60*60*24*365))}));
/*
    Ext.override(Ext.selection.Model, {
        getSelection: function() {
            var me = this,
                store = me.store,
                arr = this.selected.getRange();
            for(var i = 0; arr && i < arr.length; i++) {
                try {
                    var record = arr[i];
                    var newRecord = store.getById(record.get(record.idProperty));
                    if(newRecord) {
                        arr[i] = newRecord;
                    }
                } catch(e) {
                    alert('ext_override getSelection出现异常，详细信息：' + e);
                }
            }
            return arr;
        }
    });
*/

});
