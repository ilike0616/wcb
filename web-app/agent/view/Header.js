Ext.define('agent.view.Header', {
    extend: 'Ext.Toolbar',
    alias: 'widget.pageHeader',
    height: 60,
    border: false,
    padding: '0 0 0 0',
    html: '<div class="wcb-back-header">'
    + '<div class="wcb-back-header-banner"></div>'
    + '<div class="clear"></div>'
    + '<div class="wcb-back-header-tag">'
    + '<ul><li>'
        //+ '<li><label class="wcb-back-header-tag-work"></label><a href="#" id="workBenchSet" >工作台</a></li>'
    + '<label class="wcb-back-header-tag-user"></label><a href="#" id="agentName"></a></li>'
    + '<li><label class="wcb-back-header-tag-exit"></label><a href="#" id="logout">退出</a></li>'
    + '</ul></div>'
        //+ '<div class="wcb-back-header-skin">'
        //+ '<a href="#"><span class="skin-red"></span></a>'
        //+ '<a href="#"><span class="skin-blue"></span></a>'
        //+ '<a href="#"><span class="skin-green"></span></a>'
        //+ '</div>'
    + '</div>'
});
