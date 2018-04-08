Ext.define('user.controller.LoginController', {
    extend: 'Ext.app.Controller',
    requires: [
        'Util.Util',
        'Util.SessionMonitor',
        'Util.Employee'
    ],
    views: [
        'Login'
    ],
    refs: [
        {
            ref: 'loginForm',
            selector: 'login form'
        },
        {
            ref: 'loginTypeAccountBtn',
            selector: 'login form button#accountBtn'
        }, {
            ref: 'loginTypeMobileBtn',
            selector: 'login form button#mobileBtn'
        }, {
            ref: 'loginTypeEmailBtn',
            selector: 'login form button#emailBtn'
        }],
    init: function (application) {
        this.control({
            "login form button#Accountsubmit": {
                click: this.onButtonClickSubmitAccount
            },
            "login form button#Emailsubmit": {
                click: this.onButtonClickSubmitEmailOrMobile
            },
            "login form button#cancel": {
                click: this.onButtonClickCancel
            },
            "login form button#register": {
                click: this.onButtonClickRegister
            },
            "login form textfield": {
                specialkey: this.onTextfielSpecialKey
            },
            'login form': {
                afterrender: function (form) {
                    var userId = this.application.storeGet('userId');
                    var email = this.application.storeGet('email');
                    var password = this.application.storeGet('password');
                    var browser = this.getBrowserType();
                    //if(form.itemId == 'emailForm'){
                    //    form.down('textfield[name=email]').focus(10000);
                    //}else{
                    //    form.down('textfield[name=userId]').focus(10000);
                    //}

                    //if (userId)form.down('textfield[name=userId]').setValue(userId);
                    //if (email)form.down('textfield[name=email]').setValue(email);
                    //if (password)form.down('textfield[name=password]').setValue(password);
                    form.down('hiddenfield[name=clientType]').setValue(browser);
                }
            }
            //'login tabpanel':{
            //    afterrender:function(tab){
            //        var active = tab.getActiveTab();
            //        console.info(active);
            //        if(active.itemId == 'emailForm'){
            //            active.down('textfield[name=email]').focus();
            //        }else{
            //            active.down('textfield[name=userId]').focus();
            //        }
            //        tab.on("tabchange", function(p, newTitle, oldTitle, eOpts){
            //            console.info('111111111111111111')
            //            if(p.itemId == 'emailForm'){
            //                p.down('textfield[name=email]').focus();
            //            }else{
            //                p.down('textfield[name=userId]').focus();
            //            }
            //        }, tab);
            //    }
            //}
        });
    },
    onTextfielSpecialKey: function (field, e, options) {
        if (e.getKey() == e.ENTER) {
            var submitBtn = field.up('form').down('button[itemId$=submit]');
            submitBtn.fireEvent('click', submitBtn, e, options);
        }
    },
    onButtonClickSubmitAccount: function (button, e, options) {
        var ctrl = this.application;
        var frm = button.up('form'), login = button.up('login');
        if (!frm.getForm().isValid()) return;
        var app = this.application;
        frm.submit({
            waitMsg: '登陆中.请稍候',
            waitTitle: '登陆中',
            url: 'login',
            method: 'POST',
            submitEmptyText: false,
            success: function (form, action) {
                ctrl.getController("WorkBenchController");
                Ext.example.msg('提示', '登陆成功');
                Util.SessionMonitor.start();
                var res = Ext.JSON.decode(action.response.responseText);
                Util.Employee.employee = res.data;
//                Ext.get('mainTitle').dom.innerText = res.data.name + " "+ res.data.name;
                app.storeSet('userId', frm.down('textfield[name=userId]').getValue());
                app.storeSet('userName', res.data.name);
                app.storeSet('account', frm.down('textfield[name=account]').getValue());
                app.storeSet('password', frm.down('textfield[name=password]').getValue());
                app.storeSet('skin', res.data.skin);
                login.close();
                Ext.create('user.view.Viewport');
            },
            failure: function (form, action) {
                var result = Util.Util.decodeJSON(action.response.responseText);
                Ext.example.msg('提示', result.msg);
            }
        });
    },
    onButtonClickSubmitEmailOrMobile: function (button, e, options) {
        var ctrl = this.application;
        var frm = button.up('form'), login = button.up('login');
        if (!frm.getForm().isValid()) return;
        var app = this.application;
        frm.submit({
            waitMsg: '登陆中.请稍候',
            waitTitle: '登陆中',
            url: 'login',
            method: 'POST',
            submitEmptyText: false,
            success: function (form, action) {
                ctrl.getController("WorkBenchController");
                Ext.example.msg('提示', '登陆成功');
                Util.SessionMonitor.start();
                var res = Ext.JSON.decode(action.response.responseText);
                Util.Employee.employee = res.data;
//                Ext.get('mainTitle').dom.innerText = res.data.name + " "+ res.data.name;
                app.storeSet('userName', res.data.name);
                app.storeSet('email', frm.down('textfield[name=email]').getValue());
                app.storeSet('password', frm.down('textfield[name=password]').getValue());
                app.storeSet('skin', res.data.skin);
                login.close();
                Ext.create('user.view.Viewport');
            },
            failure: function (form, action) {
                var result = Util.Util.decodeJSON(action.response.responseText);
                Ext.example.msg('提示', result.msg);
            }
        });
    },
    onButtonClickCancel: function (button, e, options) {
        button.up('form').getForm().reset();
    },
    onButtonClickRegister: function (btn, e, options) {
        Ext.widget('register');
    },
    getBrowserType:function(){
        var browser="";
        if (Ext.isIE) {
            browser = "IE";
        } else if (Ext.isChrome) {
            browser = "Chrome";
        } else if (Ext.isGecko) {
            browser = "Gecko";
        } else if (Ext.isOpera) {
            browser = "Opera";
        } else if (Ext.isSafari) {
            browser = "Safari";
        } else if (Ext.isWebKit) {
            browser = "WebKit";
        } else if (Ext.isFF10 || Ext.isFF3_0 || Ext.isFF3_5 || Ext.isFF3_6 || Ext.isFF4 || Ext.isFF5) {
            browser = "FireFox";
        }
        return browser;
    }
});
