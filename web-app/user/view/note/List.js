Ext.define('user.view.note.List', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.noteList',
	autoScroll : true,
	split : true,
	forceFit : true,
	renderLoad : true,
	title : "随笔",
	title1 : "随笔",
	emptyText:"暂无数据",
    loadingText:"努力加载中...",
    initValues:[],
	tbar : [{
        xtype: "button",
        text: "添加",
        operationId: "note_add",
        iconCls: "table_add"
    }],
	initComponent: function() {
		var me = this;
		Ext.apply(this, {items:[{
				xtype: 'dataview',
				store : Ext.create('user.store.NoteStore'),
				tpl:me.noteTpl,
				listeners: {
					click : {
						fn: function(){ console.log('click el'); }
					}
				}
			}]
		});
		this.callParent(arguments);
	},
	noteTpl:Ext.create('Ext.XTemplate',
			'<tpl for=".">',
			'<div class="essay_container">',
			    '<div class="header"></div>',
			      '<div class="wcb_nav"><span class="tag_img"><tpl for="employee"><tpl if="photo"><img src="{fileUrl}/{photo}" width="55" height="55" /></tpl><tpl if="!photo"><img src="{fileUrl}/uploadFile/default.jpg" width="55" height="55" /></tpl></tpl></span>',
			      '<span class="tag"><ul><li><tpl for="employee">{name}</tpl></li><li>{dateCreated:this.timeFormat}</li></span></div>',
			    '<div class="main">{content}<tpl for="ats">@{name}</tpl></div>',
			    '<div class="footer"><input type="text" class="hidden_id" value="{id}"/><a class="comment_btn" href="javascript:(0)">回复</a>({commentsNum})&nbsp;&nbsp;<a class="zan_btn" href="javascript:(0)">赞</a>(<span id="zsNum">{zsNum}</span>)</div>',
			  	'<tpl for="comments">',
			  	'<div class="wcb_nav"><span class="tag_img"><tpl for="employee"><tpl if="photo"><img src="{fileUrl}/{photo}" width="55" height="55" /></tpl><tpl if="!photo"><img src="{fileUrl}/uploadFile/default.jpg" width="55" height="55" /></tpl></tpl></span>',
			  		'<span class="tag"><ul><li><tpl for="employee">{name}</tpl></li><li>{dateCreated:this.timeFormat}</li></span></div>',
			  		'<div class="main">{content}</div>',
			  		'<div class="line"></div>',
			    '</tpl>',
			'</div></tpl>',
			{timeFormat:function(time){
				if(Ext.typeOf(time) != 'date'){
					time = new Date(time);
				}
				var today = Ext.Date.format(new Date(), 'Y-m-d');
				var show = time;
				if(today == Ext.Date.format(time, 'Y-m-d')){
					show = Ext.Date.format(time, 'G:i');
				}else{
					show = Ext.Date.format(time, 'Y-m-d G:i');
				}
				return show;
			}}
	)
});