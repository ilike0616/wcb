Ext.define("user.controller.LayoutController",{
    extend:'Ext.app.Controller',
    views:['Viewport','Header','Footer','menu.Tree','workBench.WorkBenchSet','employee.ViewMyInfo'],
    stores:['UserTreeStore'],
    refs: [
        {
            ref: 'tabPanel',
            selector: '#tabPanel'
        },{
            ref: 'userMenu',
            selector: 'mainviewport menuTree'
        }
    ],
    init:function(){
        this.control({
            'mainviewport':{
                afterrender:function(){
                    w.destroy();
                }
            },
            'menuTree':{
                itemclick:this.changePage,
                afterrender:this.menuAfterRender
            },
            'pageHeader' : {
                beforerender:function(view){
                    this.loadSession();
                    //登陆时渲染上次选择的皮肤
                    var skin = this.application.storeGet('skin');
                    switch(skin){
                        case 'blue':
                            //默认皮肤
                            break;
                        case 'red':
                            this.changeSkinRed();
                            break;
                        case 'green':
                            this.changeSkinGreen();
                            break;
                    }
                },
                afterrender :function (view) {
                    var el = view.getEl();
                    var userName = this.getApplication().storeGet('userName');
                    el.down("#userName").setHTML(userName);
                    //退出登录
                    el.on({
                        'click' : {
                            delegate: "#logout",
                            fn: this.onButtonClickLogout,
                            scope: this,
                            delay: 100
                        }
                    });
                    el.on({
                        'click' : {
                            delegate: ".skin-red",
                            fn: this.changeSkinRed,
                            scope: this,
                            delay: 100
                        }
                    });
                    el.on({
                        'click' : {
                            delegate: ".skin-blue",
                            fn: this.changeSkinBlue,
                            scope: this,
                            delay: 100
                        }
                    });
                    el.on({
                        'click' : {
                            delegate: ".skin-green",
                            fn: this.changeSkinGreen,
                            scope: this,
                            delay: 100
                        }
                    });
                    // 工作台设置
                    el.on({
                        'click' : {
                            delegate: "#workBenchSet",
                            fn: this.onWorkBenchSet,
                            scope: this,
                            delay: 100
                        }
                    });
                    //查看我的信息
                    el.on({
                        'click' : {
                            delegate: "#userName",
                            fn: this.onViewMyInfo,
                            scope: this,
                            delay: 100
                        }
                    });
                }
            },
            "mainviewport tabpanel":{
            	
            }
        });
    },
    loadSession:function(){
        var app = this.application;
        Ext.Ajax.request({
            url: 'login/check',
            method:'GET',
            async:false,
            success: function(response,opts){
                var result = Util.Util.decodeJSON(response.responseText);
                if (result.success) {
                    var data = result.data
                    app.storeSet('userId', data.userId);
                    app.storeSet('userName', data.name);
                    app.storeSet('account', data.account);
                    app.storeSet('email', data.email);
                    app.storeSet('skin', data.skin);
                }
            },failure: function(response,opts){
                Util.Util.showErrorMsg(response.responseText);
            }
        });
    },
    changeSkinRed:function(me){
        Ext.util.CSS.swapStyleSheet('link_skin','http://static.xiaoshouwuyou.com/ext/resources/wcb-theme-red/css/style.css');
        Ext.util.CSS.swapStyleSheet('theme','http://static.xiaoshouwuyou.com/ext/resources/wcb-theme-red/wcb-theme-red-all.css');
        if(me && this.application.storeGet('skin') != 'red')
            this.saveSkin('red');
    },
    changeSkinBlue:function(me){
        Ext.util.CSS.swapStyleSheet('link_skin','http://static.xiaoshouwuyou.com/ext/resources/wcb-theme-blue/css/style.css');
        Ext.util.CSS.swapStyleSheet('theme','http://static.xiaoshouwuyou.com/ext/resources/wcb-theme-blue/wcb-theme-blue-all.css');
        if(me && this.application.storeGet('skin') != 'blue')
            this.saveSkin('blue');
    },
    changeSkinGreen:function(me){
        Ext.util.CSS.swapStyleSheet('link_skin','http://static.xiaoshouwuyou.com/ext/resources/wcb-theme-green/css/style.css');
        Ext.util.CSS.swapStyleSheet('theme','http://static.xiaoshouwuyou.com/ext/resources/wcb-theme-green/wcb-theme-green-all.css');
        if(me && this.application.storeGet('skin') != 'green')
            this.saveSkin('green');
    },
    saveSkin:function(skin){
        var app = this.application;
        Ext.Ajax.request({
            url:'employee/saveSkin',
            params:{skin:skin},
            method:'POST',
            timeout:20000,
            success:function(response,opts){
                var d = Ext.JSON.decode(response.responseText);
                if(d.success){
                    app.storeSet('skin',skin);
                }else{
                    //console.info('保存皮肤设置失败！');
                }

            },failure:function(response, opts){
            }
        });
    },
    changePage:function(tree,rec,item,e,opti){
        if(!rec.isLeaf()){
            return;
        }
        var me = this;
        var tabPanel = this.getTabPanel()
        tabPanel.setLoading('页面加载中，请稍后...');
        Ext.defer(function(){
            me.dealUnReadInfoNum(rec);
            var ctrl=rec.get("ctrl"),view=rec.get("view"),name=rec.get("text"),viewId=rec.get("viewId");
            me.application.widget(tabPanel, ctrl, view,viewId,ctrl+view+viewId,{title:name,iconCls:rec.raw.iconCls,viewAlias:view});
            tabPanel.setLoading(false);
        },10);
    },
    menuAfterRender:function(o,eOpts){
        var me = this;
        // 页面加载完之后停多少分钟启动定时器
        Ext.defer(function(){
            me.timeTask = {
                run: me.statUnReadInfoNum,
                interval: 120*1000, // 毫秒
                scope: me
            };
            Ext.TaskManager.start(me.timeTask);
        },120*1000);
       //this.application.widget(this.getTabPanel(), 'CustomerController', 'customerList','CustomerControllercustomerList',{title:'客户管理'});
    },
    onButtonClickLogout: function(button, e, options) {
        var me = this;
        Ext.MessageBox.confirm('提醒', '确定退出系统？', function(button) {
            me.statUnReadInfoNumTaskClose();
            if (button == 'yes') {
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
                Ext.Ajax.request({
                    url: 'logout',
                    params:{clientType:browser},
                    success: function(conn, response, options, eOpts){
                        var result = Util.Util.decodeJSON(conn.responseText);
                        if (result.success) {
                            Ext.ComponentQuery.query('mainviewport')[0].destroy();
                            location.href = "login1.html";
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
    },
    onWorkBenchSet : function(button, e, options) {
        Ext.widget('workBenchSet').show();
    },
    onViewMyInfo:function(button, e, options){
        var view = Ext.widget('employeeViewMyInfo').show();
        Ext.Ajax.request({
            url: 'employee/me',
            method: 'POST',
            timeout: 4000,
            success: function (response, opts) {
                var d = Ext.JSON.decode(response.responseText);
                if (d.success) {
                    var emp = d.data;
                    view.down('form').form.setValues(emp);
                } else {
                    Ext.example.msg('提示', d.msg);
                }
            },
            failure: function (response, opts) {
            }
        });
    },
    statUnReadInfoNum:function(){
        var me = this;
        Ext.Ajax.request({
            url:'base/statUnReadInfoNum',
            method:'POST',
            async:true,
            success:function(response){
                var result = Ext.JSON.decode(response.responseText);
                if(result.success){
                    var root = me.getUserMenu().getRootNode();
                    Ext.Array.each(result.data,function(module){
                        root.cascadeBy(function(node){
                            if(node.get('moduleId') == module.moduleId){
                                var text = node.get('text');
                                var divIndex = text.indexOf('<div');
                                if(divIndex > -1){
                                    text = text.substring(0,divIndex);
                                }
                                var num = module.num;
                                if(parseInt(num,10) > 99) num = "99+";
                                var num = "<div class='qipao'>"+num+"</div>";
                                node.set('text',text + num);
                            }
                        })
                    })
                }
            },
            failure:function(response,opts){
                me.statUnReadInfoNumTaskClose();
                console.info("获取未读数据数量失败！");
            }
        });
    },
    statUnReadInfoNumTaskClose:function(){
        if(this.timeTask){
            Ext.TaskManager.stop(this.timeTask);
        }
    },
    dealUnReadInfoNum : function(node){
        Ext.Ajax.request({
            url:'base/updateModuleLastQueryDate',
            method:'POST',
            params:{moduleId:node.get('moduleId')},
            async:true,
            success:function(response){
                var result = Ext.JSON.decode(response.responseText);
                if(result.success) {
                    var text = node.get('text');
                    var divIndex = text.indexOf('<div');
                    if(divIndex > -1){
                        text = text.substring(0,divIndex);
                    }
                    node.set('text',text);
                }
            },
            failure:function(response,opts){
                console.info("更新模块上次查询时间失败！");
            }
        });
    }
});