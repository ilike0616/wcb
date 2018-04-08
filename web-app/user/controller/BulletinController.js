/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.BulletinController',{
    extend : 'Ext.app.Controller',
    views:['bulletin.List','bulletin.Edit','bulletin.View'] ,
    stores:['BulletinStore'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
            'bulletinList button[operationId=bulletin_update]' :{
                click:function(btn){
                    var record = btn.up('bulletinList').getSelectionModel().getSelection()[0];
                    var acceptorsId = [],acceptorsName = [];
                    var acceptDeptsId = [],acceptDeptsName = [];
                    Ext.each(record.get('acceptors'),function(item){
                        acceptorsId.push(item.id);
                        acceptorsName.push(item.name);
                    })
                    if(acceptorsId.length > 0){
                        acceptorsId.join(",");
                        acceptorsName.join(",");
                    }
                    Ext.each(record.get('acceptDepts'),function(item){
                        acceptDeptsId.push(item.id);
                        acceptDeptsName.push(item.name);
                    })
                    if(acceptDeptsId.length > 0){
                        acceptDeptsId.join(",");
                        acceptDeptsName.join(",");
                    }
                    var view = Ext.widget('bulletinEdit').show();
                    view.down('form').loadRecord(record);
                    view.down('hiddenfield[name=acceptors]').setValue(acceptorsId);
                    view.down('baseMultiSelectTextareaField textareafield[name=acceptors.name]').setValue(acceptorsName);
                    view.down('hiddenfield[name=acceptDepts]').setValue(acceptDeptsId);
                    view.down('baseMultiSelectTextareaField textareafield[name=acceptDepts.name]').setValue(acceptDeptsName);
                    var allCompany = record.get('allCompany')?1:0;
                    view.down('combo[name=allCompany]').setValue(allCompany);
                    if(allCompany == 0){
                        view.down('baseMultiSelectTextareaField[name=acceptors.name]').show();
                        view.down('baseMultiSelectTextareaField[name=acceptDepts.name]').show();
                    }else{
                        view.down('baseMultiSelectTextareaField[name=acceptors.name]').hide();
                        view.down('baseMultiSelectTextareaField[name=acceptDepts.name]').hide();
                    }
                }
            },
            'bulletinList button[operationId=bulletin_view]' :{
                click:function(btn){
                    var record = btn.up('bulletinList').getSelectionModel().getSelection()[0];
                    var acceptorsName = [];
                    var acceptDeptsName = [];
                    Ext.each(record.get('acceptors'),function(item){
                        acceptorsName.push(item.name);
                    })
                    if(acceptorsName.length > 0){
                        acceptorsName.join(",");
                    }
                    Ext.each(record.get('acceptDepts'),function(item){
                        acceptDeptsName.push(item.name);
                    })
                    if(acceptDeptsName.length > 0){
                        acceptDeptsName.join(",");
                    }
                    var view = Ext.widget('bulletinView').show();
                    view.down('form').loadRecord(record);
                    view.down('textareafield[name=acceptors.name]').setValue(acceptorsName);
                    view.down('textareafield[name=acceptDepts.name]').setValue(acceptDeptsName);
                    var allCompany = record.get('allCompany')?1:0;
                    view.down('combo[name=allCompany]').setValue(allCompany);
                    if(allCompany == 0){
                        view.down('field[name=acceptors.name]').show();
                        view.down('field[name=acceptDepts.name]').show();
                    }else{
                        view.down('field[name=acceptors.name]').hide();
                        view.down('field[name=acceptDepts.name]').hide();
                    }
                }
            },
            'baseWinForm[moduleId=bulletin][optType=add] combo[name=allCompany]':{
                change:function(o, newValue, oldValue, eOpts){
                    var view = o.up('baseWinForm');
                    if(newValue == 0){
                        view.down('baseMultiSelectTextareaField[name=acceptors.name]').show();
                        view.down('baseMultiSelectTextareaField[name=acceptDepts.name]').show();
                    }else{
                        view.down('baseMultiSelectTextareaField[name=acceptors.name]').hide();
                        view.down('baseMultiSelectTextareaField[name=acceptDepts.name]').hide();
                    }
                }
            },
            'bulletinEdit combo[name=allCompany]':{
                change:function(o, newValue, oldValue, eOpts){
                    var view = o.up('bulletinEdit');
                    if(newValue == 0){
                        view.down('baseMultiSelectTextareaField[name=acceptors.name]').show();
                        view.down('baseMultiSelectTextareaField[name=acceptDepts.name]').show();
                    }else{
                        view.down('baseMultiSelectTextareaField[name=acceptors.name]').hide();
                        view.down('baseMultiSelectTextareaField[name=acceptDepts.name]').hide();
                    }
                }
            }
        });
    }
})
