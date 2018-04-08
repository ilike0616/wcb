/**
 * Created by like on 2015-04-15.
 * 非自定义模块
 */
Ext.define('user.model.NotifyModelFilterModel', {
    extend: 'Ext.data.TreeModel',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'name', type: 'string'},
        {name: 'moduleId', type: 'string'},
        {name: 'notifyModel', type:'int'},
        {name: 'childRelation', type: 'int'},
        {name: 'parentNotifyModelFilter.id', type: 'int'},
        {name: 'parentNotifyModelFilter.name', type: 'string'},
        {name: 'dateCreated', type: 'date'},
        {name: 'lastUpdated', type: 'date'}
    ]
})