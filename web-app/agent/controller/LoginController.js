Ext.define('agent.controller.LoginController', {
    extend: 'Ext.app.Controller',
    requires: [
        'Util.Util',
        'Util.SessionMonitor'
    ],
    views: [
        'Login'
    ],
    init: function(application) {
        this.control({
            "login form button#submit": {
                click: this.onButtonClickSubmit
            },
            "login form button#cancel": {
                click: this.onButtonClickCancel
            },
            "login form textfield": {
                specialkey: this.onTextfielSpecialKey
            },
            'login form':{
                afterrender:function(form){
                    var agentId = this.application.storeGet('agentId');
                    var password = this.application.storeGet('password');
                    var browser = "";
                    if(Ext.isIE){
                        browser = "IE";
                    }else if(Ext.isChrome){
                        browser = "Chrome";
                    }else if(Ext.isGecko){
                        browser = "Gecko";
                    }else if(Ext.isOpera){
                        browser = "Opera";
                    }else if(Ext.isSafari){
                        browser = "Safari";
                    }else if(Ext.isWebKit){
                        browser = "WebKit";
                    }else if(Ext.isFF10 || Ext.isFF3_0 || Ext.isFF3_5 || Ext.isFF3_6 || Ext.isFF4 || Ext.isFF5 ){
                        browser = "FireFox";
                    }

                    if(agentId)form.down('textfield[name=agentId]').setValue(agentId);
                    if(password)form.down('textfield[name=password]').setValue(password);
                    form.down('hiddenfield[name=clientType]').setValue(browser);
                }
            }
        });
    },
    onTextfielSpecialKey: function(field, e, options) {
        if (e.getKey() == e.ENTER){
            var submitBtn = field.up('form').down('button#submit');
            submitBtn.fireEvent('click', submitBtn, e, options);
        }
    },
    onButtonClickSubmit: function(button, e, options) {
        var frm = button.up('form'),login = button.up('login');
        if (!frm.getForm().isValid()) return;
        var app = this.application;
        frm.submit({
            waitMsg: '登陆中.请稍候',
            waitTitle: '登陆中',
            url:'login/agent',
            method: 'POST',
            submitEmptyText : false,
            success: function(form, action) {
                Ext.example.msg('提示', '登陆成功');
                Util.SessionMonitor.start();
                var res = Ext.JSON.decode(action.response.responseText);
//                Ext.get('mainTitle').dom.innerText = res.data.name + " "+ res.data.name;
                app.storeSet('agentId',frm.down('textfield[name=agentId]').getValue());
                app.storeSet('agentName',res.data.name);
                app.storeSet('password',frm.down('textfield[name=password]').getValue());
                login.close();
                Ext.create('agent.view.Viewport');
            },
            failure:function(form,action){
                var result = Util.Util.decodeJSON(action.response.responseText);
                Ext.example.msg('提示', result.msg);
            }
        });
    },
    onButtonClickCancel: function(button, e, options) {
        button.up('form').getForm().reset();
    }
});
