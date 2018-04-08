/**
 * Created by guozhen on 2014/7/31.
 */
Ext.define('user.controller.ScheduleController',{
    extend : 'Ext.app.Controller',
    views:['schedule.Main','schedule.PlanMain','schedule.SummaryMain','schedule.Add','schedule.CustomerAdd','schedule.SaleChanceAdd','schedule.Edit','schedule.CustomerEdit'
        ,'schedule.SaleChanceEdit','schedule.View','schedule.CustomerView','schedule.SaleChanceView','customerFollow.Add'
        ,'saleChanceFollow.Add'
        ],
    stores:[],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function() {
        this.control({
            'scheduleMain tabpanel':{
                tabchange:function(tabPanel, newCard, oldCard, eOpts ){
                    var ownerId = newCard.up('scheduleMain').down('baseSpecialTextfield').down('baseHiddenField').getValue();
                    var store = newCard.down('calendarpanel').eventStore;
                    Ext.apply(store.proxy.extraParams,{owner: ownerId});
                    store.load();
                }
            },
            'scheduleMain combo[name=owner.name]':{
                afterrender:function( o, eOpts ){
                    o.up('baseSpecialTextfield').down('baseHiddenField').setValue(Util.Employee.employee.id);
                    o.setValue(Util.Employee.employee.name);
                },
                change: function (o, newValue, oldValue, eOpts) {
                    o.fireEvent('blur',o);
                    var activeTab = o.up('scheduleMain').down('tabpanel').getActiveTab();
                    var ownerId = o.up('baseSpecialTextfield').down('baseHiddenField').getValue();
                    var store = activeTab.down('calendarpanel').eventStore;
                    Ext.apply(store.proxy.extraParams,{owner: ownerId});
                    store.load();
                }
            },
            // 日程安排
            'schedulePlanMain calendarpanel':{
                'eventclick': {
                    fn: function (vw, rec, el) {
                        var calendarPanel = vw.up('calendarpanel');
                        this.showEditOrDelOptMenu(calendarPanel,rec);
                    },
                    scope: this
                },
                'viewchange': {
                    fn: function (p, vw, dateInfo) {
                        if (dateInfo) {
                            var main = p.up('schedulePlanMain');
                            main.down('datepicker').setValue(dateInfo.activeDate);
                            this.updateTitle(dateInfo.viewStart, dateInfo.viewEnd,'schedulePlanMain-app-center');
                        }
                    },
                    scope: this
                },
                'dayclick': {
                    fn: function (vw, dt, ad, el) {
                        var allow = this.judgeModulePrivilege('schedule_add');
                        if(!allow) {
                            Ext.example.msg('提示', "对不起，您没有该操作权限！");
                            return;
                        }
                        var calendarPanel = vw.up('calendarpanel');
                        this.showAddOptMenu(calendarPanel,dt);
                    },
                    scope: this
                },
                'rangeselect': {
                    fn: function (win, dates, onComplete) {
                        // 判断有没有新增权限
                        var allow = this.judgeModulePrivilege('schedule_add');
                        if(!allow) {
                            Ext.example.msg('提示', "对不起，您没有该操作权限！");
                            return;
                        }
                        var calendarPanel = win.up('calendarpanel');
                        this.showAddOptMenu(calendarPanel,dates);
                    },
                    scope: this
                },
                'eventmove': {
                    fn: function (vw, rec) {
                        this.eventUpdate(rec);
                    },
                    scope: this
                },
                'eventresize': {
                    fn: function (vw, rec) {
                        this.eventUpdate(rec);
                    },
                    scope: this
                },
                'initdrag': {
                    fn: function (vw) {
                    },
                    scope: this
                }
            },
            // 日程总结
            'scheduleSummaryMain calendarpanel':{
                'eventclick': {
                    fn: function (vw, rec, el) {
                        var viewAlias = "";
                        var relatedType = rec.get('relatedType');
                        if(relatedType == 1){
                            viewAlias = 'scheduleCustomerView';
                        }else if(relatedType == 2){
                            viewAlias = 'scheduleSaleChanceView';
                        }
                        if(viewAlias != ''){
                            var win = Ext.widget(viewAlias);
                            var winForm = win.down('form');
                            win.show();
                            winForm.loadRecord(rec);
                        }
                    },
                    scope: this
                },
                'viewchange': {
                    fn: function (p, vw, dateInfo) {
                        if (dateInfo) {
                            var main = p.up('scheduleSummaryMain');
                            main.down('datepicker').setValue(dateInfo.activeDate);
                            this.updateTitle(dateInfo.viewStart, dateInfo.viewEnd,'scheduleSummaryMain-app-center');
                        }
                    },
                    scope: this
                },
                'dayclick': {
                    fn: function (vw, dt, ad, el) {
                    },
                    scope: this
                },
                'rangeselect': {
                    fn: function (win, dates, onComplete) {
                    },
                    scope: this
                },
                'eventmove': {
                    fn: function (vw, rec) {
                    },
                    scope: this
                },
                'eventresize': {
                    fn: function (vw, rec) {
                    },
                    scope: this
                },
                'initdrag': {
                    fn: function (vw) {
                    },
                    scope: this
                }
            },
            'button[itemId=scheduleCalendarInsert]':{
                click:function(btn){
                    this.saveForm(btn,'insert',{});
                }
            },
            'button[itemId=scheduleCalendarUpdate]':{
                click:function(btn){
                    this.saveForm(btn,'update',{});
                }
            },
            'button[btnItemId=executeScheduleFollowAdd]':{
                click:function(btn){
                    var params = {
                        scheduleId:btn.scheduleId,
                        moduleId:btn.moduleId,
                        moduleScheduleName:btn.moduleScheduleName
                    }
                    this.saveFollowForm(btn,params);
                }
            }
        });
    },
    updateTitle: function(startDt, endDt,mainAppCenter){
        var p = Ext.getCmp(mainAppCenter),
            fmt = Ext.Date.format;

        if(Ext.Date.clearTime(startDt).getTime() == Ext.Date.clearTime(endDt).getTime()){
            p.setTitle(fmt(startDt, 'Y-m-d'));
        }else{
            p.setTitle(fmt(startDt, 'Y-m-d') + ' ~ ' + fmt(endDt, 'Y-m-d'));
        }
    },
    showAddOptMenu:function(calendarPanel,dates){
        var me = this;
        var contextMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '客户跟进',
                iconCls:'customer_follow',
                relatedType: 1,
                handler:function(){
                    me.showAdd(calendarPanel,this.relatedType,dates);
                }
            },{
                text: '商机跟进',
                iconCls:'customer_follow',
                relatedType: 2,
                handler:function(){
                    me.showAdd(calendarPanel,this.relatedType,dates);
                }
            },{
                text: '待办任务',
                iconCls:'plan_summary',
                relatedType: 3,
                handler:function(){
                    me.showAdd(calendarPanel,this.relatedType,dates);
                }
            }]
        });
        contextMenu.showAt([window.event.pageX,window.event.pageY]);
    },
    showEditOrDelOptMenu:function(calendarPanel,rec){
        var me = this;
        var items = [{
            text: '查看',
            iconCls:'table_view',
            handler:function(){
                me.showEditOrView(calendarPanel,rec,'View');
            }
        }];
        // 是否有修改权限
        var allow = this.judgeModulePrivilege('schedule_update');
        if(allow == true && rec.get('taskState') != 2){
            items.push({
                text: '修改',
                iconCls:'table_save',
                handler:function(){
                    me.showEditOrView(calendarPanel,rec,'Edit');
                }
            });
        }
        // 是否有删除权限
        var allow = this.judgeModulePrivilege('schedule_delete');
        if(allow == true){
            items.push({
                text: '删除',
                iconCls:'table_remove',
                handler:function(){
                    me.deleteById(calendarPanel,rec);
                }
            });
        }
        var relatedType = rec.get('relatedType');
        var allow = false;
        if(relatedType == 1){
            allow = this.judgeModulePrivilege('customer_follow_add');
        }else if(relatedType == 2){
            allow = this.judgeModulePrivilege('sale_chance_follow_add');
        }
        if(allow == true && rec.get('taskState') != 2){
            items.push({
                text: '执行',
                iconCls:'table_save',
                handler:function(){
                    me.executeScheduleFollow(calendarPanel,rec);
                }
            });
        }
        var contextMenu = Ext.create('Ext.menu.Menu', {
            items: items
        });
        contextMenu.showAt([window.event.pageX,window.event.pageY]);
    },
    showAdd:function(calendarPanel,relatedType,dates){
        var startDate,endDate;
        if(Ext.typeOf(dates) == "object"){
            startDate = dates.StartDate;
            endDate = dates.EndDate;
        }else{
            startDate = dates;
            endDate = dates;
        }
        var viewAlias = this.getViewAliasByRelatedType(relatedType,'Add');
        var win = Ext.widget(viewAlias,{
            calendarPanel:calendarPanel
        });
        var winForm = win.down('form');
        // 判断如果没有某些字段，则必须强制加上
        var startComponent = winForm.down('datetimefield[name=planStartDate]');
        var endComponent = winForm.down('datetimefield[name=planEndDate]');
        if(startComponent == null){
            winForm.add({xtype:'hiddenfield',name:'planStartDate',value:startDate});
        }else{
            startComponent.setValue(startDate);
        }
        if(endComponent == null){
            winForm.add({xtype:'hiddenfield',name:'planEndDate',value:endDate});
        }else{
            endComponent.setValue(endDate);
        }
        var relatedTypeComponent = winForm.down('hiddenfield[name=relatedType]');
        if(relatedTypeComponent == null){
            winForm.add({xtype:'hiddenfield',name:'relatedType',value:relatedType});
        }else{
            relatedTypeComponent.setValue(relatedType);
        }
        win.show();
    },
    showEditOrView:function(calendarPanel,record,editOrView){
        var viewAlias = this.getViewAliasByRelatedType(record.get('relatedType'),editOrView);
        var win = Ext.widget(viewAlias,{
            calendarPanel:calendarPanel
        });
        var winForm = win.down('form');
        win.show();
        winForm.loadRecord(record);
    },
    saveFollowForm:function(btn,params){
      this.saveForm(btn,'insertFollow',params)
    },
    saveForm:function(btn,api,params){
        var me = this;
        var win = btn.up('window');
        var form = win.down('form');
        var calendarPanel = form.calendarPanel;
        var store = win.calendarPanel.eventStore;
        if (!form.getForm().isValid()) return;
        form.submit({
            waitMsg: '正在提交数据',
            waitTitle: '提示',
            url:store.getProxy().api[api],
            params:params,
            method: 'POST',
            timeout:4000,
            submitEmptyText : false,
            success: function(response, opts) {
                store.load();
                Ext.example.msg('提示', '保存成功');
                console.info(win);
                if(win){
                    win.close();
                }
            },
            failure:function(form,action){
                var result = Util.Util.decodeJSON(action.response.responseText);
                if(result.msg) {
                    Ext.example.msg('提示', result.msg);
                }
            }
        });
    },
    deleteById:function(calendarPanel,record){
        var me = this;
        //得到数据集合
        var store = calendarPanel.eventStore;
        var data = [];
        var msg = "";
        data.push(Ext.JSON.encode(record.get('id')));
        msg = msg +'\n'+record.get("subject");
        Ext.MessageBox.confirm('确定删除？', msg, function(button){
            if(button=='yes'){
                if(data.length > 0){
                    Ext.Ajax.request({
                        url:store.getProxy().api['remove'],
                        params:{ids:"["+data.join(",")+"]"},
                        method:'POST',
                        timeout:20000,
                        success:function(response,opts){
                            var d = Ext.JSON.decode(response.responseText);
                            if(d.success){
                                store.load();
                                Ext.example.msg('提示', '删除成功');
                            }else{
                                Ext.example.msg('提示', '删除失败！');
                            }
                        },failure:function(response, opts){
                            var errorCode = "";
                            if(response.status){
                                errorCode = 'error:'+response.status;
                            }
                            Ext.example.msg('提示', '删除失败！'+errorCode);
                        }
                    });
                }
            }
        });
    },
    getViewAliasByRelatedType:function(relatedType,addOrEdit){
        var viewAlias = 'schedule'+addOrEdit;
        if(relatedType == 1){
            viewAlias = 'scheduleCustomer'+addOrEdit;
        }else if(relatedType == 2){
            viewAlias = 'scheduleSaleChance'+addOrEdit;
        }else if(relatedType == 3){
            viewAlias = 'schedule'+addOrEdit;
        }
        return viewAlias;
    },
    judgeModulePrivilege:function(privilegeName){
        var success = false;
        Ext.Ajax.request({
            url:'schedule/judgeModulePrivilege',
            params:{privilege:privilegeName},
            method:'POST',
            timeout:4000,
            async:false,
            success:function(response,opts){
                var d = Ext.JSON.decode(response.responseText);
                if(d.success){
                    success = true;
                }else{
                    success = false;
                }
            },
            failure:function(response,opts){
                var errorCode = "";
                if(response.status){
                    errorCode = 'error:'+response.status;
                }
                Ext.example.msg('提示', '操作失败！'+errorCode);
                success = false;
            }
        });
        return success;
    },
    eventUpdate:function(rec){
        // 已完成不允许修改
        if(rec.get('taskState') == 2) return;
        var mappings = Ext.calendar.data.EventMappings,
            time = rec.data[mappings.IsAllDay.name] ? '' : ' \\a\\t g:i a';
        rec.set('planStartDate',rec.get(mappings.StartDate.name));
        rec.set('planEndDate',rec.get(mappings.EndDate.name));
        var params = {
            id:rec.get('id'),
            planStartDate:Ext.Date.format(rec.get('planStartDate'),'Y-m-d H:i:s'),
            planEndDate:Ext.Date.format(rec.get('planEndDate'),'Y-m-d H:i:s')
        }
        var success = this.GridDoActionUtil.doAjax('schedule/update',params,null,false);
        rec.commit();
    },
    executeScheduleFollow:function(calendarPanel,rec){
        var relatedType = rec.get('relatedType');
        var win,moduleId,moduleScheduleName;
        if(relatedType == 1){
            moduleId = "customer_follow";
            moduleScheduleName = "customerFollow";
            win = Ext.widget('customerFollowAdd',{
                calendarPanel:calendarPanel
            }).show();
            var customerCom = win.down('baseSpecialTextfield[name=customer.name]');
            if(customerCom != null){
                win.down('hiddenfield[name=customer]').setValue(rec.get('customer').id);
                customerCom.down('combo').setValue(rec.get('customer').name);
            }
        }else if(relatedType == 2){
            moduleId = "sale_chance_follow";
            moduleScheduleName = "saleChanceFollow";
            win = Ext.widget('saleChanceFollowAdd',{
                calendarPanel:calendarPanel
            }).show();
            var customerCom = win.down('baseSpecialTextfield[name=customer.name]');
            if(customerCom != null){
                win.down('hiddenfield[name=customer]').setValue(rec.get('customer').id);
                customerCom.down('combo').setValue(rec.get('customer').name);
            }
            var saleChanceCom = win.down('baseSpecialTextfield[name=saleChance.subject]');
            if(saleChanceCom != null){
                win.down('hiddenfield[name=saleChance]').setValue(rec.get('saleChance').id);
                saleChanceCom.down('combo').setValue(rec.get('saleChance').subject);
            }
        }
        if(win != null && win != ""){
            var btn = win.down('button#save');
            btn.btnItemId = "executeScheduleFollowAdd";
            btn.autoInsert = false;
            btn.target = 'schedulePlanMain';
            btn.scheduleId = rec.get('id');
            btn.moduleId = moduleId;
            btn.moduleScheduleName = moduleScheduleName;
        }
    },
    showMsg: function(msg){
        Ext.example.msg('提示', msg);
    }
})
