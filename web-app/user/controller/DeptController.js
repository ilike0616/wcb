/**
 * Created by guozhen on 2014/6/23.
 */

Ext.define('user.controller.DeptController',{
    extend : 'Ext.app.Controller',
    views:['dept.Main','dept.List'] ,
    stores:['DeptStore','DeptStoreForEdit'],
    models:['DeptModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref : 'deptList',
            selector : 'deptList'
        }
    ],
    init:function() {
        this.control({
            'deptList > treeview': {
                drop: function (node, data, overModel, dropPosition, eOpts) {
                    var store = data.view.store.treeStore;
                    var url = store.proxy.api['save'];
                    var records = data.records[0];
                    var data ={};
                    data["id"] = records.get("id");
                    if(dropPosition == 'append'){
                        data["parentDept"] = overModel.get("id");
                    }else{
                        var parentDpet = overModel.get("id");
                        if(parentDpet == 0){
                            data["parentDept"] = null;
                        }else{
                            data["parentDept"] = parentDept;
                        }
                    }
                    Ext.Ajax.request({
                        url:url,
                        params:{data:Ext.JSON.encode(data)},
                        method:'POST',
                        timeout:4000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            store.load();
                            /*if(!(overModel.hasChildNodes())){
                                overModel.set('leaf','true')
                                overModel.set('parentDept',null);
                            }*/
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
                            Ext.example.msg('提示', '删除失败！'+errorCode);
                        }
                    });
                },
                nodedragover : function(targetNode, position, dragData, e, eOpts ){
                    if (targetNode.get('leaf')){
                        targetNode.set('leaf','false');
                    }
                    return true;
                }
            }

        });
    }
})
