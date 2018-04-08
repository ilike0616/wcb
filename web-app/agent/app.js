Ext.override(Ext.Ajax, {
    defaultHeaders : {u:'agent'}
});
Ext.QuickTips.init();
Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', 'res/ux');
Ext.Loader.setPath('public', 'public');
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

Ext.onReady(function(){
    Ext.application({
        name: 'agent',
        appFolder: 'agent',
        autoCreateViewport: false,
        controllers: [
            'LoginController','LayoutController','BaseController'
        ],
        requires: [
            'public.AddBalance',,'public.ReduceBalance'
            /*'public.column.RowSelecter','public.form.field.DateTime','public.BaseSpecialTextfield','public.BaseFieldSet'
            ,'public.BaseImageField','public.BaseImageField2','public.BaseFieldSet','public.uploadPanel.BaseUploadField'*/
        ],
        localStorage : window.localStorage,
        launch:function(){
            Ext.tip.QuickTipManager.init();
            Ext.Ajax.request({
                url: 'login/checkAgent',
                success: function(response,opts){
                    var result = Util.Util.decodeJSON(response.responseText);
                    if (result.success) {
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
});
