/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('user.view.productKind.List', {
    extend: 'public.BaseTreeGrid',
    alias: 'widget.productKindList',
    autoScroll: true,
    store:Ext.create('user.store.ProductKindStore'),
    title: '产品分类',
    forceFit:true,
    reserveScrollbar: true,
    id:'userViewProductKindBaseTreeGrid',
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop',
            displayField : 'name',
            containerScroll: true
        }
    },
    showRowNumber : false,
    renderLoad:true,
    viewId:'ProductKindList'
});