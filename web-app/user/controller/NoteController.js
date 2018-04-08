/**
 * Created by like on 2015/3/19.
 */
Ext.define('user.controller.NoteController', {
	extend : 'Ext.app.Controller',
	views : [ 'note.List', 'note.Add', 'note.Reply'],
	stores : [ 'NoteStore' ],
	models : [ 'NoteModel' ],
	GridDoActionUtil : Ext.create("admin.util.GridDoActionUtil"),
	refs : [ {
		ref : 'comment',
		selector : 'panel a[id=comment_btn]'
	} ],
	init : function() {
		this.control({
			'noteList dataview' : {
				viewready: "listInit",
				afterrender : function(dataview) {
					dataview.getStore().load();
				}
			},
			'button[operationId=note_add]' : {
				click : function(btn) {
					Ext.widget('noteAdd',{listDom:btn.up('noteList'),optType:'add'}).show();
				}
			},
			'noteAdd button[itemId=save]' : {
				click : function(btn) {
					// 得到数据集合
					var store = Ext.ComponentQuery.query(btn.target)[0].down('dataview').getStore();
					var frm = btn.up('form');
					var win = btn.up('window');
					if (!frm.getForm().isValid())
						return;
					frm.submit({
						waitMsg : '正在提交数据',
						waitTitle : '提示',
						url : store.getProxy().api['insert'],
						method : 'POST',
						params : '',
						submitEmptyText : false,
						success : function(form, action) {
							store.load();
							Ext.example.msg('提示', '保存成功');
							if (win) {
								win.close();
							}
						},
						failure : function(form, action) {
							var result = Util.Util.decodeJSON(action.response.responseText);
							Ext.example.msg('提示', result.msg);
						}
					});
				}
			},
			'noteReply button[itemId=reply]': {
				click : function(btn) {
					var store = Ext.ComponentQuery.query('noteList')[0].down('dataview').getStore();
					var frm = btn.up('form');
					var win = btn.up('window');
					if (!frm.getForm().isValid())
						return;
					frm.submit({
						waitMsg : '正在提交数据',
						waitTitle : '提示',
						url : store.getProxy().api['comment'],
						method : 'POST',
						params : '',
						submitEmptyText : false,
						success : function(form, action) {
							store.load();
							Ext.example.msg('提示', '保存成功');
							if (win) {
								win.close();
							}
						},
						failure : function(form, action) {
							var result = Util.Util.decodeJSON(action.response.responseText);
							Ext.example.msg('提示', result.msg);
						}
					});
				}
			}
		});
	},
	listInit:function (view) {
		var el = view.getEl();
		//评论
		el.on({
			'click' : {
				delegate: ".comment_btn",
		        fn: this.initComment,
		        scope: this,
		        delay: 100
		    }
		});
		//点赞
		el.on({
			'click' : {
				delegate: ".zan_btn",
		        fn: this.dianzan,
		        scope: this,
		        delay: 100
		    }
		});
	},
	initComment:function(e){
		var record = this.getNoteData(e);
		var win = Ext.widget('noteReply').show();
		win.down('form').form.setValues({note:record.get('id')});
	},
	dianzan:function(e){
		var record = this.getNoteData(e);
		var store = Ext.ComponentQuery.query('noteList')[0].down('dataview').getStore();
		var url = store.getProxy().api['zs'];
		var zsDom = Ext.get(e.getTarget()).up('.footer').down('#zsNum').dom;
		//.dom.outerText
		Ext.Ajax.request({
            url:url,
            params:{note:record.get('id')},
            method:'POST',
            timeout:4000,
            async:true,
            success:function(response,opts){
                var d = Ext.JSON.decode(response.responseText);
                if(d.type){
                	zsDom.innerText=Number(zsDom.innerText)-1;
                }else{
                	zsDom.innerText=Number(zsDom.innerText)+1;
                }
            },
            failure:function(response,opts){
                var errorCode = "";
                if(response.status){
                    errorCode = 'error:'+response.status;
                }
                Ext.example.msg('提示', '操作失败！'+errorCode);
                success = false;
            }
        });
	},
	//获取点击列表的信息
    getNoteData: function (e) {
        var id = Ext.get(e.getTarget()).up('.footer').down('.hidden_id').getValue(),
            store = Ext.ComponentQuery.query('noteList')[0].down('dataview').getStore(),
            record = store.getById(Number(id)),
            e = null;
        return record;
    }
})