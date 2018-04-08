/**
 * Created by guozhen on 2014-6-23.
 */
Ext.define('user.view.product.List', {
    extend: 'public.BaseList',
    alias: 'widget.productList',
    autoScroll: true,
    store:Ext.create('user.store.ProductStore'),
    title: '产品管理',
    split:true,
    forceFit:true,
    renderLoad:true,
    viewId:'ProductList'
});