/**
 * Created by guozhen on 2014/12/19.
 */
Ext.define('user.view.privilege.ModuleViewList', {
    extend: 'public.BaseList',
    alias: 'widget.moduleViewList',
    operateBtn:false,
    autoScroll: true,
    store:Ext.create('admin.store.ViewStore'),
    enableBasePaging : false,
    split:true,
    forceFit:true,
    enableToolbar:false,
    columns:[
        {
            text:'视图',
            dataIndex:'title',
            renderer:function(val,meta,rec){
            	 return val + "[" + rec.get('clientType') + "]";
            }
        }
    ]
});