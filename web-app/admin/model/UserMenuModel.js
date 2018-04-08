/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define('admin.model.UserMenuModel',{
    extend : 'Ext.data.TreeModel',
    fields : [
        {name : 'text',type : 'string'},
        {name : 'iconCls',type : 'string'},
        {name : 'idx',type : 'int'},
        {name : 'dateCreated',type : 'date'},
        {name : 'lastUpdated',type : 'date'},
        {name : 'parentUserMenu'},
        {name : 'parentUserMenuName',type : 'string'},
        {name : 'user'},
        {name : 'name',type : 'string'},
        {name : 'module'},
        {name : 'moduleId',type : 'string'},
        {name : 'moduleName',type : 'string'},
        {name : 'moduleCtrl',type : 'string'},
        {name : 'isLeaf',type : 'boolean'},
        {name : 'children'},
        {name : 'childrenNames',type : 'string'}
    ]
})