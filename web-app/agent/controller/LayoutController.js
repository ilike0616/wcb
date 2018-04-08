Ext.define("agent.controller.LayoutController",{
    extend:'Ext.app.Controller',
    views:['Viewport','Header','Footer','menu.Tree'],
    stores:['TreeStore'],
    refs: [
        {
            ref: 'tabPanel',
            selector: '#tabPanel'
        }
    ],
    init:function(){
        this.control({
            'menuTree':{
                'itemclick':this.changePage
            },
            'pageHeader' : {
                afterrender :function (view) {
                    var el = view.getEl();
                    var agentName = this.getApplication().storeGet('agentName');
                    el.down("#agentName").setHTML(agentName);
                    //退出登录
                    el.on({
                        'click': {
                            delegate: "#logout",
                            fn: this.onButtonClickLogout,
                            scope: this,
                            delay: 100
                        }
                    });
                }
            }
        });
    },
    changePage:function(tree,rec,item,e,opti){
        var ctrl=rec.get("ctrl"),view=rec.get("view"),name=rec.get("text");
        if(!rec.isLeaf()){
            return;
        }
        this.application.widget(this.getTabPanel(), ctrl, view,ctrl+view,{title:name});
    },
    onButtonClickLogout: function(button, e, options) {
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
            url: 'logout/agent',
            params:{clientType:browser},
            success: function(conn, response, options, eOpts){
                var result = Util.Util.decodeJSON(conn.responseText);
                if (result.success) {
                    Ext.ComponentQuery.query('agentviewport')[0].destroy();
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