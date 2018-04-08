Ext.override(Ext.Ajax, {
    defaultHeaders : {u:'user'}
});
Ext.Loader.setConfig({
    disableCaching:true,   // false:不追加_dc=时间戳,可以缓存；true:追加时间戳,阻止缓存
    enabled: true,
    paths: {
        'Ext.calendar': 'res/calendar/src',
        'Ext.calendar.view': 'res/calendar/src/view',
        'Ext.calendar.dd': 'res/calendar/src/dd',
        'Ext.ux': 'res/ux',
        'Ext.app': 'res/portal/classes',
        'public': 'public'
    }
});
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
                    url: 'logout',
                    success: function(conn, response, options, eOpts){
                        var result = Util.Util.decodeJSON(conn.responseText);
                        if (result.success) {
                            Ext.ComponentQuery.query('mainviewport')[0].destroy();
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
Ext.application({
    name: 'user',
    appFolder: 'user',
    autoCreateViewport: false,
    controllers: [
        'LoginController','LayoutController','BaseController','RegisterController'
    ],
    requires: [
        //'user.util.DoAjax','user.util.DoActionUtil',
        'public.column.RowSelecter','public.BaseCombo','public.column.Multiselecter','public.form.field.DateTime','public.BaseSearchField'
        ,'public.uploadPanel.BaseUploadField','public.BaseDetailTextField','public.BaseExport','public.BaseImport','public.BaseComboBoxTree'
        ,'public.BaseSpecialTextfield','public.BaseSpecialTextfieldTree','public.BaseImageField','public.BaseImageField2','public.BaseFieldSet'
        ,'public.BaseStatisticCharts','public.BaseMultiSelectTextareaField','public.BaseTemplateTextAreaField','public.BaseCascadeCombo'
        ,'public.column.ColumnCalculate','public.BaseSummary','public.BaseFunnelChart','public.BaseYearStatList','public.form.field.MonthField'
        ,'public.BaseHiddenField','public.TabCloseMenu','public.BaseStackedCharts','public.column.ArrayColumn','public.BaseWinForm'
        ,'public.uploadPanel.BaseUploadImageField'
    ],
    localStorage : window.localStorage,
    launch:function(){
        var me = this;
        var application = this;
        Ext.tip.QuickTipManager.init();
        Ext.Ajax.request({
            url: 'login/check',
            success: function(response,opts){
                var result = Util.Util.decodeJSON(response.responseText);
                if (result.success) {
                    application.getController('WorkBenchController');
                    Ext.create('user.view.Viewport');
                    Util.SessionMonitor.start();
                    Util.Employee.employee = result.data;
                    //me.moduleVersion();
                } else {
                    location.href = "login1.html";
                }
            },failure: function(response,opts){       
                Util.Util.showErrorMsg(response.responseText);
            }
        });
    },
    widget: function(tabPanel, controllerName, widgetName, viewId,record, cfg) {
        var findRes = this.findTab(tabPanel, record);
        if (findRes) {
            this.activateTab(tabPanel, findRes);
        } else {
        	if(controllerName) this.getController(controllerName);
            if(viewId){
            	var tab = Ext.widget('baseList', {record: record, closable: true,viewId:viewId});
            }else{
            	var tab = Ext.widget(widgetName, {record: record, closable: true});
            }
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
    /**
    getModuleVersion:function(moduleId){
        var versions = window.localStorage.getItem("moduleVersions");
        var version;
        if(versions) {
            var mv = Ext.JSON.decode(versions);
            mv.forEach(function (obj) {
                if (obj.moduleId == moduleId) {
                    version = obj.version;
                }
            });
        }
        return version;
    },
    moduleVersion:function(){
        var me = this;
        Ext.Ajax.request({
            url:"model/moduleVersion",
            method:'POST',
            async:false,
            timeout:20000,
            success:function(response,opts){
                var d = Ext.JSON.decode(response.responseText);
                if(d.success){
                    d.data.forEach(function(obj){
                        var ov = me.getModuleVersion(obj.moduleId),
                            nv = obj.version;
                        if(ov != nv){
                            me.refreshModule(obj.moduleId);
                        }
                    });
                    window.localStorage.setItem("moduleVersions",Ext.JSON.encode(d.data));
                }
            },failure:function(response, opts){
                var errorCode = "";
                if(response.status){
                    errorCode = 'error:'+response.status;
                }
                console.info("锟斤拷锟截版本锟斤拷息失锟斤拷");
            }
        });
    },
    refreshModule:function(moduleId){
        console.info("刷新："+moduleId);
        Ext.Ajax.request({
            url:"model/pc",
            params:{moduleId:moduleId},
            method:'POST',
            async:false,
            timeout:20000,
            success:function(response,opts){
                var d = Ext.JSON.decode(response.responseText);
                if(d.success && d.data){
                    var fields = d.data.fields,
                        views = d.data.views;
                    if(fields && fields.length > 0) {
                        window.localStorage.setItem("model-" + moduleId, Ext.JSON.encode(fields));
                    }
                    if(views) {
                        Ext.Object.each(views, function(viewId, value, myself) {
                            window.localStorage.setItem(viewId, Ext.JSON.encode(value));
                        });
                    }
                }
            },failure:function(response, opts){
                var errorCode = "";
                if(response.status){
                    errorCode = 'error:'+response.status;
                }
                console.info("锟斤拷锟截版本锟斤拷息失锟斤拷");
            }
        });
    }*/
});

Ext.onReady(function(){
	 Ext.state.Manager.setProvider(  new Ext.state.CookieProvider({expires: new Date(new Date().getTime()+(1000*60*60*24*365))}));
});
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
});*/
