Ext.define('admin.view.menu.Tree', {
    extend: 'Ext.tree.Panel',
    alias:'widget.menuTree',
    xtype: 'treePanel',
    title: '菜单',
    rootVisible: false,
    cls: 'examples-list',
    lines: false,
    useArrows: true,
    collapsible:true,
    store: 'TreeStore' ,
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop',
            enableDrag: true,
            enableDrop: true
        }
    }
 });