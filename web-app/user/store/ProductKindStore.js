/**
 * Created by guozhen on 2014/6/23.
 */
Ext.define("user.store.ProductKindStore",{
    extend : 'Ext.data.TreeStore',
    model : modelFactory.getModelByModuleId('product_kind'),
    requires:[
        'user.proxy.ProxyTemplate'
    ],
    proxy : {
        type : 'proxyTemplate',
        api:{
            read: 'productKind/list',
            remove: 'productKind/delete',
            update: "productKind/update",
            insert: "productKind/insert",
            save: 'productKind/save'
        },
        reader : {
            type : 'json',
            root: 'data',
            successProperty:'success',
            totalProperty:'total'
        }
    },
    autoLoad: true,
    folderSort : true,
    root: {
        expanded: true,
        name: "产品分类"
    }
})
