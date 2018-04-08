/**
 * Created by guozhen on 2014/6/23.
 */

Ext.define('admin.controller.UserMenuController',{
    extend : 'Ext.app.Controller',
    views:['userMenu.Main','userMenu.List',/*'userMenu.Add','userMenu.Edit',*/'dept.DeptUserList'] ,
    stores:['UserMenuStore'],
    models:['UserMenuModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
        {
            ref:'deptUserList',
            selector:'userMenuMain deptUserList'
        },
        {
            ref : 'userMenuList',
            selector : 'userMenuMain userMenuList'
        }
    ],
    init:function() {
        this.control({
            'userMenuMain deptUserList': {
                beforerender:function(grid,eOpts){
                    var store = grid.getStore();
                    Ext.apply(store.proxy.extraParams,{limit:9999999});
                    store.load();
                },
                select : function(o, record, index, eOpts){
                    var userMenuStore = this.getUserMenuList().getStore();
                    Ext.apply(userMenuStore.proxy.extraParams,{userId:record.get('id')});
                    userMenuStore.load();
                }
            },
            'userMenuMain userMenuList': {
                select : function(o, record, index, eOpts){
                },
                deselect : function(o, record, index, eOpts){
                }
            },
            'userMenuMain userMenuList button#saveButton' : {
                click:function(btn){
                    var userList = this.getDeptUserList();
                    var record = userList.getSelectionModel().getSelection()[0];
                    if(record == null || Ext.typeOf(record) == 'undefined'){
                        alert("请选择用户！");
                        return;
                    }
                    this.GridDoActionUtil.doSave(this.getUserMenuList(),Object);
                }
            },
            'userMenuMain userMenuList > treeview': {
                drop: function (node, data, overModel, dropPosition, eOpts) {
                    var store = data.view.store.treeStore;
                    var items = data.view.store;
                    var record = data.records[0];
                    var data ={};
                    data["id"] = record.get("id");
                    if(dropPosition == 'append'){
                        data["parentUserMenu"] = overModel.get("id");
                    }else{
                        data["parentUserMenu"] = overModel.get("parentUserMenu");
                    }
                    var idxs = [];
                    items.each(function(record,index){
                        record.set('idx',index);
                        idxs.push(record.get('id')+"_"+record.get('idx'));
                    });
                    data["idxs"] = idxs;
                    Ext.Ajax.request({
                        url:'userMenu/dragSortIdx',
                        params:{data:Ext.JSON.encode(data)},
                        method:'POST',
                        timeout:4000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            store.load();
                            Ext.example.msg('提示', d.msg);
                        },
                        failure:function(response,opts){
                            store.load();
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '操作失败！'+errorCode);
                        }
                    });
                },
                nodedragover : function(targetNode, position, dragData, e, eOpts ){
                    // 1、如果目标节点是叶子节点并且（拖放位置为append或者拖动对象为非叶子节点），则禁止拖动
                    // 2、如果目标节点是非叶子节点且拖动位置为append且拖动对象为非叶子节点，则禁止拖动
                    // 3、如果目标节点是非叶子节点且拖动位置为before且拖动对象为叶子节点，则禁止拖动
                    if((targetNode.get('isLeaf') && (position == 'append' || dragData.records[0].get('isLeaf') == false))
                        || (targetNode.get('isLeaf') == false && dragData.records[0].get('isLeaf') == false && position == 'append')
                        || (targetNode.get('isLeaf') == false && dragData.records[0].get('isLeaf') && position == 'before')){
                        return false;
                    }
                    if(targetNode.get('isLeaf') == false){
                        targetNode.set('leaf',false);
                    }
                    return true;
                }
            }
        });
    }
})
