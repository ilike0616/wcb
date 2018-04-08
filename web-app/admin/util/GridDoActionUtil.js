Ext.define("admin.util.GridDoActionUtil",{
    doDelete:function(grid,name,btn){
        var me = this;
        if(!grid){
            alert("参数传递不正确");
            return;
        }
        //得到数据集合
        var store = grid.getStore();
        var records = grid.getSelectionModel().getSelection();
        var data = [];
        var msg = "";
        Ext.Array.each(records,function(model){
            data.push(Ext.JSON.encode(model.get('id')));
            msg = msg +'\n'+model.get(name);
        });
        Ext.MessageBox.confirm('确定删除？', msg, function(button){
            if(button=='yes'){
                if(data.length > 0){
                    Ext.Ajax.request({
                        url:store.getProxy().api['remove'],
                        params:{ids:"["+data.join(",")+"]"},
                        method:'POST',
                        timeout:20000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            me.loadStore(grid);
                            btn.setDisabled(true);
                            if(d.success){
                                Ext.example.msg('提示', '删除成功');
                            }else{
                                Ext.example.msg('提示', '删除失败！');
                            }
                        },failure:function(response, opts){
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '删除失败！'+errorCode);
                        }
                    });
                }
            }
        });
    },
    doDeleteById:function(grid,id,name){
        var me = this;
        if(!grid){
            alert("参数传递不正确");
            return;
        }
        //得到数据集合
        var store = grid.getStore();
        Ext.MessageBox.confirm('确定删除？', name, function(button){
            if(button=='yes'){
                if(id > 0){
                    Ext.Ajax.request({
                        url:store.getProxy().api['remove'],
                        params:{ids:"["+Ext.JSON.encode(id)+"]"},
                        method:'POST',
                        timeout:20000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            me.loadStore(grid);
                            if(d.success){
                                store.sync();
                                Ext.example.msg('提示', '删除成功');
                            }else{
                                Ext.example.msg('提示', '删除失败！');
                            }
                        },failure:function(response, opts){
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '删除失败！'+errorCode);
                        }
                    });
                }
            }
        });
    },

    doSave:function(grid,btn,otherParams){
        doSave(grid,btn,otherParams,null);
    },
    /**
     * 列表的批量修改
     * @param {} grid
     */
    doSave:function(grid,btn,otherParams,paramUrl){
        var me = this;
        if(!grid){
            alert("参数传递不正确");
            return;
        }
        //得到数据集合
        var store = grid.getStore();
        //records 被你修改过的数据
        var records = store.getUpdatedRecords();
        var data = [];
        Ext.Array.each(records,function(model){
            data.push(Ext.JSON.encode(model.data));
        });

        if(data.length > 0){
            var otherParmas = Ext.JSON.encode(otherParams)
            var url = store.getProxy().api['save'];
            if(paramUrl != null){
                url = paramUrl;
            }
            Ext.Ajax.request({
                url:url,
                params:{data:"["+data.join(",")+"]",otherParams:otherParmas},
//                params:'data='+encodeURIComponent(Ext.encode(data)),
                method:'POST',
                timeout:20000,
                success:function(response,opts){
                    var d = Ext.JSON.decode(response.responseText);
                    me.loadStore(grid);
                    grid.getSelectionModel().deselectAll();
                    //btn.setDisabled(true);
                    if(d.success){
                        Ext.example.msg('提示', '保存成功');
                    }else{
                        Ext.example.msg('提示', '保存失败！');
                    }
                },
                failure:function(response,opts){
                    var errorCode = "";
                    if(response.status){
                        errorCode = 'error:'+response.status;
                    }
                    Ext.example.msg('提示', '保存失败！'+errorCode);
                }
            });
        }
    },
    doAjax:function(url,params,store){
        doAjax(url,params,store,true);
    },
    doAjax:function(url,params,store,isAsync){
        if(!url){
            alert("参数传递不正确");
            return;
        }
        var success = true;
        Ext.Ajax.request({
            url:url,
            params:params,
            method:'POST',
            timeout:90000,
            async:isAsync,
            success:function(response,opts){
                var d = Ext.JSON.decode(response.responseText);
                if(d.success){
                    if(d.msg) Ext.example.msg('提示', d.msg);
                    if(store != null && store != 'undefined'){
                        store.load();
                    }
                }else{
                    if(d.msg) Ext.example.msg('提示', d.msg);
                    success = false;
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
        return success;
    },
    doUpdate:function(grid,form,win,params,api){
        var me = this;
        if(params){
            params.moduleId=form.moduleId;
        }else{
            params = {moduleId:form.moduleId};
        }
        var store = grid.getStore();
        if (!form.getForm().isValid()) return;
        form.submit({
            waitMsg: '正在提交数据',
            waitTitle: '提示',
            url:store.getProxy().api[api]||store.getProxy().api['update'],
            method: 'POST',
            params: params,
            timeout:90000,
            submitEmptyText : false,
            success: function(response, opts) {
                if(grid.isHidden()==false){
                    me.loadStore(grid);
                }
                if(grid.getSelectionModel().getCount()>0) {
                    grid.getSelectionModel().deselectAll();
                }
                Ext.example.msg('提示', '保存成功');
                if(win && !win.submitNotClose){
                    win.close();
                }
            },
            failure:function(form,action){
                var result = Util.Util.decodeJSON(action.response.responseText);
                if(result.msg) {
                    Ext.example.msg('提示', result.msg);
                }
            }
        });
    },
    /**
     * 树形维护表格的插入操作
     * @param {} grid
     * @param {} modelObj
     * @param {} treeObj
     * @param {} api 指定提交地址 从store中取api的参数
     */
    doInsert:function(grid,form,win,params,api){
        var me = this;
        if(!grid){
            alert("参数传递不正确");
            return;
        }
        if(params){
            params.moduleId=form.moduleId;

        }else{
            params = {moduleId:form.moduleId};
        }
        //得到数据集合
        var store = grid.getStore();
        if (!form.getForm().isValid()) return;
        form.submit({
            waitMsg: '正在提交数据',
            waitTitle: '提示',
            url:store.getProxy().api[api]||store.getProxy().api['insert'],
            method: 'POST',
            params: params,
            timeout:90000,
            submitEmptyText : false,
            success: function(form, action) {
                if(grid.isHidden()==false){
                    me.loadStore(grid);
                }
                if(grid.getSelectionModel().getCount()>0){
                    grid.getSelectionModel().deselectAll();
                }
                Ext.example.msg('提示', '保存成功');
                if(win){
                    win.close();
                }
            },
            failure:function(form,action){
            	var result = Util.Util.decodeJSON(action.response.responseText);
                if(result.msg) {
                    Ext.example.msg('提示', result.msg);
                }
            }
        });
    },
    loadStore:function(grid){
        var store = grid.getStore();
        if(Ext.typeOf(store) != 'undefined') {
            store.load();
        }
    }
});

