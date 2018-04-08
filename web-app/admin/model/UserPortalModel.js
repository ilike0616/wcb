/**
 * Created by guozhen on 2014-6-16.
 */
Ext.define("admin.model.UserPortalModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id', type: 'int'},
        {name: 'title',type: 'string'},
        {name: 'height', type: 'int'},
        {name: 'idx', type: 'int'},
        {name: 'isShow', type: 'boolean'},
        {name: 'userPortalId', type: 'int'},
        {name: 'portal'},
        {name: 'portalName',type:'string'},
        {name: 'isEnable',type:'boolean'},
        {name: 'user'},
        {name: 'userName',type:'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});