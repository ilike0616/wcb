/**
 * Created by like on 2015-04-15.
 * 非自定义模块
 */
Ext.define('user.model.NotifyModelFilterDetailModel', {
    extend:'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'name', type: 'string'},
        {name: 'fieldName', type: 'string'},
        {name: 'fieldText', type: 'string'},
        {name: 'dbType', type: 'string'},
        {name: 'dbTypeName', type: 'string'},
        {name: 'isDict', type: 'boolean'},
        {name: 'expectType', type: 'int'},
        {name: 'expectValue', type: 'string'},
        {name: 'expectText', type: 'string'},
        {name: 'operator', type: 'String'},
        {name: 'dateCreated', type: 'date'},
        {name: 'lastUpdated', type: 'date'}
    ]
})