/**
 * Created by like on 2015/9/18.
 */
Ext.define("user.model.SfaEventExecuteModel",{
    extend:'Ext.data.Model',
    fields:[
        {name: 'id',type:'int'},
        {name: 'user.id',type: 'int'},
        {name: 'user.name',type: 'string'},
        {name: 'sfaExecute.id',type: 'int'},
        {name: 'sfaEvent.id',type: 'int'},
        {name:'state',type:'int'},
        {name:'receiverType',type:'int'},
        {name:'employees'},
        {name: 'customer'},
        {name:'executeDate',type:'date'},
        {name:'isNotify',type:'boolean'},
        {name:'notifyResult',type:'int'},
        {name: 'notifySubject',type: 'string'},
        {name: 'notifyContent',type: 'string'},
        {name:'isSms',type:'boolean'},
        {name:'smsResult',type:'int'},
        {name: 'smsSubject',type: 'string'},
        {name: 'smsContent',type: 'string'},
        {name:'isEmail',type:'boolean'},
        {name:'emailResult',type:'int'},
        {name: 'emailSubject',type: 'string'},
        {name: 'emailContent',type: 'string'},
        {name:'dateCreated',type:'date'},
        {name:'lastUpdated',type:'date'}
    ]
});