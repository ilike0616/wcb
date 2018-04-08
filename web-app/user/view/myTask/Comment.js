/**
 * Created by like on 2015/8/18.
 */
Ext.define('user.view.myTask.comment', {
    extend : 'public.BaseList',
    alias : 'widget.myTaskComment',
    autoScroll : true,
    store : Ext.create('Ext.data.Store', {
        model : 'user.model.CommentModel',
        proxy : {
            type : 'ajax',
            api : {
                read : 'myTask/getComments'
            },
            reader : {
                type : 'json',
                root : 'data',
                successProperty : 'success',
                totalProperty : 'total'
            },
            writer : {
                type : 'json'
            },
            simpleSortMode : true
        },
        autoLoad : false
    }),
    title : '回复',
    split : true,
    forceFit : true,
    renderLoad : false,
    operateBtn : false,
    enableSearchField : false,
    enableComplexQuery : false,
    enableBasePaging : false,
    columns : [ {
        xtype : 'rownumberer',
        width : 40,
        sortable : false
    }, {
        text : '用户名称',
        dataIndex : 'employee.name'
    }, {
        text : '内容',
        dataIndex : 'content'
    }, {
        text : '创建时间',
        dataIndex : 'dateCreated',
        xtype : 'datecolumn',
        format : 'Y-m-d H:i:s'
    } ],
    plugins: [{
        ptype: 'rowexpander',
        rowBodyTpl : new Ext.XTemplate(
            '<p><b>内容:</b> {content}</p>',
            '<p><b>附件:</b><tpl for="photos"><a href="{name}" target="_blank">{orgName}</a></tpl></p>',
            '<p><b>创建时间:</b> {dateCreated:this.formatDate}</p>',
            {
                formatDate:function(v){
                    return Ext.Date.format(v,'Y-m-d H:i:s');
                }
            }
        )
    }]
});