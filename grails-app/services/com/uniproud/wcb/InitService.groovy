package com.uniproud.wcb

import grails.events.Listener
import grails.transaction.Transactional

@Transactional
class InitService {
    def modelService
    def moduleService
    def userService
    def dataDictService
    def annotationService
    def grailsApplication

    /**
     * 初始化，系统数据模型
     * @return
     */
    def init(){  //1307470
        modelService.insert('com.uniproud.wcb.Module','Module', '模块')
        modelService.insert('com.uniproud.wcb.Dept','Dept', '部门')
        modelService.insert('com.uniproud.wcb.Employee','Employee', '员工')
        modelService.insert('com.uniproud.wcb.Customer','Customer','我的客户')
        modelService.insert('com.uniproud.wcb.Contact','Contact','联系人')
        modelService.insert('com.uniproud.wcb.CustomerFollow','CustomerFollow', '客户跟进')
        modelService.insert('com.uniproud.wcb.SaleChance','SaleChance', '销售商机')
        modelService.insert('com.uniproud.wcb.SaleChanceDetail','SaleChanceDetail', '商机明细')
        modelService.insert('com.uniproud.wcb.SaleChanceFollow','SaleChanceFollow', '销售商机跟进')
        modelService.insert('com.uniproud.wcb.OnsiteObject','OnsiteObject', '对象管理')
        modelService.insert('com.uniproud.wcb.ObjectFollow','ObjectFollow', '对象拜访')
        modelService.insert('com.uniproud.wcb.OutsiteRecord','OutsiteRecord', '现场记录')
        modelService.insert('com.uniproud.wcb.ServiceTask','ServiceTask', '服务派单')
        modelService.insert('com.uniproud.wcb.Product','Product', '产品管理')
        modelService.insert('com.uniproud.wcb.ProductKind','ProductKind', '产品分类')
        modelService.insert('com.uniproud.wcb.PlanSummary','PlanSummary', '计划总结')
        modelService.insert('com.uniproud.wcb.Bulletin','Bulletin', '内部公告')
        modelService.insert('com.uniproud.wcb.User','User', '用户')
        modelService.insert('com.uniproud.wcb.UserOperation','UserOperation', '用户操作')
        //insert('com.uniproud.wcb.Role','Role', '角色管理')
        modelService.insert('com.uniproud.wcb.Privilege','Privilege', '权限管理')
        modelService.insert('com.uniproud.wcb.Doc','Doc', '文档管理')
        modelService.insert('com.uniproud.wcb.TaskAssigned','TaskAssigned', '任务交办')
        modelService.insert('com.uniproud.wcb.Audit','Audit', '审核任务')
        modelService.insert('com.uniproud.wcb.AuditOpinion','AuditOpinion', '审核意见')
        modelService.insert('com.uniproud.wcb.GoOutApply','GoOutApply', '外出申请')
        modelService.insert('com.uniproud.wcb.BusinessTripApply','BusinessTripApply', '出差申请')
        modelService.insert('com.uniproud.wcb.LeaveApply','LeaveApply', '请假申请')
        modelService.insert('com.uniproud.wcb.OvertimeApply','OvertimeApply', '加班申请')
        modelService.insert('com.uniproud.wcb.AttendanceData','AttendanceData', '考勤数据')
        modelService.insert('com.uniproud.wcb.AttendanceModel','AttendanceModel', '考勤模板')
        modelService.insert('com.uniproud.wcb.AttendanceCalendar','AttendanceCalendar', '考勤日历')
        modelService.insert('com.uniproud.wcb.AttendanceStatistics','AttendanceStatistics', '考勤统计')
        modelService.insert('com.uniproud.wcb.Comment','Comment', '评论回复')
        modelService.insert('com.uniproud.wcb.ContractOrder','ContractOrder', '订单管理')
        modelService.insert('com.uniproud.wcb.ContractOrderDetail','ContractOrderDetail', '订单明细')
        /*insert('com.uniproud.wcb.DataDict','DataDict', '数据字典')
        insert('com.uniproud.wcb.DataDictItem','DataDictItem', '数据字典明细')*/
        modelService.insert('com.uniproud.wcb.MarketActivity','MarketActivity', '市场活动')
        modelService.insert('com.uniproud.wcb.Competitor','Competitor', '竞争对手')
        modelService.insert('com.uniproud.wcb.CompetitorDynamic','CompetitorDynamic', '对手动态')
        modelService.insert('com.uniproud.wcb.CompetitorProduct','CompetitorProduct', '竞争产品')
        modelService.insert('com.uniproud.wcb.Note','Note', '随笔')
        modelService.insert('com.uniproud.wcb.Locus','Locus', '员工轨迹')
        modelService.insert('com.uniproud.wcb.LoginLog','LoginLog', '登录日志')
        modelService.insert('com.uniproud.wcb.NotifyModel','NotifyModel','消息设置')
        modelService.insert('com.uniproud.wcb.NotifyModelFilter','NotifyModelFilter','消息条件组')
        modelService.insert('com.uniproud.wcb.NotifyModelFilterDetail','NotifyModelFilterDetail','消息条件明细')
        modelService.insert('com.uniproud.wcb.EmployeeNotifyModel','EmployeeNotifyModel','我的提醒')
        modelService.insert('com.uniproud.wcb.Notify','Notify','消息中心')
        modelService.insert('com.uniproud.wcb.PaymentRecord','PaymentRecord','扣费记录')
        modelService.insert('com.uniproud.wcb.FareClaims','FareClaims','费用报销')
        modelService.insert('com.uniproud.wcb.FareClaimsDetail','FareClaimsDetail','费用报销明细')
        modelService.insert('com.uniproud.wcb.FinanceAccount','FinanceAccount','财务账户')
        modelService.insert('com.uniproud.wcb.FinanceIncomeExpense','FinanceIncomeExpense','出入账')
        modelService.insert('com.uniproud.wcb.Review','Review','领导评阅')
        modelService.insert('com.uniproud.wcb.Schedule','Schedule','日程安排')
        modelService.insert('com.uniproud.wcb.Sfa','Sfa','SFA方案')
        modelService.insert('com.uniproud.wcb.SfaEvent','SfaEvent','SFA事件')
        modelService.insert('com.uniproud.wcb.SfaExecute','SfaExecute','SFA方案记录')
        modelService.insert('com.uniproud.wcb.SfaEventExecute','SfaEventExecute','SFA事件记录')
        modelService.insert('com.uniproud.wcb.Share','Share','分享管理')
        modelService.insert('com.uniproud.wcb.CloseCenter','CloseCenter','关闭中心')
        modelService.insert('com.uniproud.wcb.Invoice','Invoice','开票记录')
        modelService.insert('com.uniproud.wcb.InvoiceDetail','InvoiceDetail','开票明细')
        modelService.insert('com.uniproud.wcb.SitePhoto','SitePhoto','现场拍照')
        modelService.insert('com.uniproud.wcb.InstallOrder','InstallOrder','安装订单')
        modelService.insert('com.uniproud.wcb.InstallOrderDetail','InstallOrderDetail','安装订单明细')
        modelService.insert('com.uniproud.wcb.InstallAllocateDetail','InstallAllocateDetail','预派单')
        modelService.insert('com.uniproud.wcb.CustomerRevisit', 'CustomerRevisit', '客户回访')
        modelService.insert('com.uniproud.wcb.CustomerCare', 'CustomerCare', '客户关怀')
        modelService.insert('com.uniproud.wcb.CustomerComplaints', 'CustomerComplaints', '客户关怀')
        modelService.insert('com.uniproud.wcb.MarketActivityCustomer','MarketActivityCustomer','市场活动客户')
        modelService.insert('com.uniproud.wcb.Weixin','Weixin','微信公众号')
        modelService.insert('com.uniproud.wcb.WeixinCustomer','WeixinCustomer','微信关注者')
        modelService.insert('com.uniproud.wcb.QRCode','QRCode','二维码')
        modelService.insert('com.uniproud.wcb.MarketActivityCustomer','MarketActivityCustomer','市场活动客户')
        modelService.insert('com.uniproud.wcb.Material','Material','素材')
        modelService.insert('com.uniproud.wcb.NewsMaterial','NewsMaterial','图文素材')
        modelService.insert('com.uniproud.wcb.WeixinMenu','WeixinMenu','微信菜单')
        modelService.insert('com.uniproud.wcb.WeixinSendMsg','WeixinSendMsg','群发消息')
        modelService.insert('com.uniproud.wcb.SendMsgWeixinCustomer','SendMsgWeixinCustomer','微信消息接受者接收者')

    }

    def initDev(){
        log.info "系统模块初始化......"
        def start = Calendar.instance.timeInMillis
        moduleService.init()
        def end  = Calendar.instance.timeInMillis
        def s = end - start
        log.info "系统模块初始化完成,耗时:${s/1000}秒"
        start = Calendar.instance.timeInMillis
        log.info "创建demo测试数据......."
        createDemo()
        userService.init()
        end  = Calendar.instance.timeInMillis
        s = end - start
        log.info "创建测试数据完成,耗时:${s/1000}秒"
    }
    def initVerDate(){
        def start = Calendar.instance.timeInMillis
        def users = User.findAll()
//        dataDictService.init(users)
//        if(grailsApplication.metadata['app.version']=='0.1.2'){
//            def opts = ['public_customer_add','public_customer_update','public_customer_view',
//                        'customer_add','customer_update','customer_view','customer_follow',
//                        'contact_add','contact_update','contact_view',
//                        'customer_follow_add','customer_follow_update','customer_follow_view',
//                        'sale_chance_add','sale_chance_follow',
//                        'sale_chance_follow_add','sale_chance_follow_update','sale_chance_follow_view',
//                        'onsite_object_add','onsite_object_update','onsite_object_view','onsite_object_follow',
//                        'object_follow_add','object_follow_update','object_follow_view',
//                        'service_task_add','service_task_update','service_task_view',
//                        'product_kind_update','product_kind_view','product_view',
//                        'bulletin_add',
//                        'fare_claims_add','fare_claims_update','fare_claims_view',
//                        'attendance_calendar_add','attendance_calendar_update','attendance_calendar_view',
//                        'attendance_data_view',
//                        'leave_apply_add','leave_apply_update','leave_apply_view',
//                        'business_trip_apply_add','business_trip_apply_update','business_trip_apply_view',
//                        'overtime_apply_add','overtime_apply_update','overtime_apply_view',
//                        'goout_apply_add','goout_apply_update','goout_apply_view',
//                        'contract_order_add','contract_order_update','contract_order_view','contract_order_detail_view',
//                        'market_activity_add','market_activity_update','market_activity_view',
//                        'finance_account_add','finance_account_update','finance_account_view',
//                        'finance_income_add','finance_income_update','finance_income_view',
//                        'finance_expense_add','finance_expense_update','finance_expense_view',
//                        'competitor_add','competitor_update','competitor_view',
//                        'competitor_product_add','competitor_product_update','competitor_product_view',
//                        'competitor_dynamic_add','competitor_dynamic_update','competitor_dynamic_view',
//                        'locus_view',
//                        'employee_add','employee_update','employee_view',
//                        'dept_add','dept_update','dept_view',
//                        'privilege_add','privilege_update','privilege_view',
//                        'base_dict_view',
//                        'login_log_view',
//                        'add_balance_view',
//                        'notify_model_add','notify_model_update','notify_model_view',
//                        'employee_notify_model_view','employee_notify_model_update',
//                        'notify_view',
//                        'payment_record_view',
//                        'sfa_add','sfa_update','sfa_view',
//                        'attendance_statistics_view',
//                        'review_view','review_update',
//                        'task_assigned_add']
//            Operation.executeUpdate("update Operation set isCustom=true where operationId in (:opts)",[opts:opts])

//            User.findAllByDeleteFlag(false)?.each { user ->
//                //客户回访
//                if (DataDict.findByDataIdAndUser(83, user) == null) {
//                    new DataDict(user: user, dataId: 83, text: '业务类型', issys: true)
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '产品', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '订单', seq: 2))
//                            .save()
//                }
//                //客户回访
//                if (DataDict.findByDataIdAndUser(84, user) == null) {
//                    new DataDict(user: user, dataId: 84, text: '回访方法')
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '电话回访', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '实地回访', seq: 2))
//                            .save()
//                }
//                //客户回访
//                if (DataDict.findByDataIdAndUser(85, user) == null) {
//                    new DataDict(user: user, dataId: 85, text: '客户满意度')
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '一般', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '满意', seq: 2))
//                            .addToItems(new DataDictItem(user: user, itemId: 3, text: '很满意', seq: 3))
//                            .addToItems(new DataDictItem(user: user, itemId: 4, text: '不满意', seq: 4))
//                            .save()
//                }
//                //客户回访
//                if (DataDict.findByDataIdAndUser(86, user) == null) {
//                    new DataDict(user: user, dataId: 86, text: '关怀方式')
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '电话关怀', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '实地关怀', seq: 2))
//                            .save()
//                }
//                //客户投诉
//                if (DataDict.findByDataIdAndUser(87, user) == null) {
//                    new DataDict(user: user, dataId: 87, text: '投诉种类')
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '电话投诉', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '上门投诉', seq: 2))
//                            .addToItems(new DataDictItem(user: user, itemId: 3, text: '邮件投诉', seq: 3))
//                            .save()
//                }
//                //客户投诉
//                if (DataDict.findByDataIdAndUser(88, user) == null) {
//                    new DataDict(user: user, dataId: 88, text: '处理结果')
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '进行中', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '已解决', seq: 2))
//                            .addToItems(new DataDictItem(user: user, itemId: 3, text: '未解决', seq: 3))
//                            .save()
//                }
//                if (DataDict.findByDataIdAndUser(89, user) == null) {
//                    new DataDict(user: user, dataId: 89, text: '投诉类型')
//                            .addToItems(new DataDictItem(user: user, itemId: 1, text: '产品', seq: 1))
//                            .addToItems(new DataDictItem(user: user, itemId: 2, text: '订单', seq: 2))
//                            .addToItems(new DataDictItem(user: user, itemId: 3, text: '安装单', seq: 3))
//                            .save()
//                }
//            }
            if(Module.findByModuleId('weixin_mgmt')==null){
                def weixin_mgmt = new Module(moduleId: 'weixin_mgmt',moduleName:'微信管理').save()
            }

            if(Module.findByModuleId('weixin')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/weixin.json").exists()) {
                def weixin = new Module(moduleId:'weixin',moduleName: '微信公众号',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.Weixin'),ctrl:'WeixinController', vw: 'weixinMain').save()
                new Operation(module: weixin,operationId:'weixin_add',text:'新增',vw:'weixinAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
                new Operation(module: weixin,operationId:'weixin_update',text:'修改',vw:'weixinEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
                new Operation(module: weixin,operationId:'weixin_view',text:'查看',vw:'weixinView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
                new Operation(module: weixin,operationId:'weixin_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
            }

            if(Module.findByModuleId('weixin_customer')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/weixin_customer.json").exists()) {
                def weixin_customer = new Module(moduleId:'weixin_customer',moduleName: '微信关注者',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.WeixinCustomer'),ctrl:'WeixinCustomerController', vw: 'weixinCustomerList').save()
                new Operation(module: weixin_customer,operationId:'weixin_customer_add',text:'新增',vw:'weixinCustomerAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
                new Operation(module: weixin_customer,operationId:'weixin_customer_update',text:'修改',vw:'weixinCustomerEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
                new Operation(module: weixin_customer,operationId:'weixin_customer_view',text:'查看',vw:'weixinCustomerView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
                new Operation(module: weixin_customer,operationId:'weixin_customer_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
            }

            if(Module.findByModuleId('weixin_menu')==null) {
                def weixin_menu = new Module(moduleId:'weixin_menu',moduleName: '微信菜单',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.WeixinMenu'),ctrl:'WeixinMenuController', vw: 'weixinMenuMain').save()
                new Operation(module: weixin_menu,operationId:'weixin_menu_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            }

            if(Module.findByModuleId('weixin_send_msg')==null) {
                def weixin_send_msg = new Module(moduleId:'weixin_send_msg',moduleName: '群发消息',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.WeixinSendMsg'),ctrl:'WeixinSendMsgController', vw: 'weixinSendMsgMain').save()
                new Operation(module: weixin_send_msg,operationId:'weixin_send_msg_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            }

            if(Module.findByModuleId('material')==null) {
                def material = new Module(moduleId:'material',moduleName: '素材管理',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.Material'),ctrl:'MaterialController', vw: 'materialMain').save()
                new Operation(module: material,operationId:'material_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            }

            if(Module.findByModuleId('send_msg_weixin_customer')==null  && new File("${grailsApplication.config.MODULE_FILE_PATH}/send_msg_weixin_customer.json").exists()) {
                def send_msg_weixin_customer = new Module(moduleId:'send_msg_weixin_customer',moduleName: '群发接收者列表',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.SendMsgWeixinCustomer'),ctrl:'SendMsgWeixinCustomerController', vw: 'SendMsgWeixinCustomerMain',isMenu:false).save()
                new Operation(module: send_msg_weixin_customer,operationId:'send_msg_weixin_customer_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            }
            def market_activity = Module.findByModuleId('market_activity')
            new Operation(module: market_activity,operationId: 'market_activity_invit',text:'邀客',vw:'marketActivityInvit',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true).save()

            if(Module.findByModuleId('market_activity_customer')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/market_activity_customer.json").exists()) {
                def market_activity_customer = new Module(moduleId:'market_activity_customer',moduleName: '市场活动客户',parentModule: Module.findByModuleId('market_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.MarketActivityCustomer'),ctrl:'MarketActivityCustomerController', vw: 'marketActivityCustomerList').save()
                new Operation(module: market_activity_customer,operationId:'market_activity_customer_add',text:'新增',vw:'marketActivityCustomerAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
                new Operation(module: market_activity_customer,operationId:'market_activity_customer_update',text:'修改',vw:'marketActivityCustomerEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
                new Operation(module: market_activity_customer,operationId:'market_activity_customer_view',text:'查看',vw:'marketActivityCustomerView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
                new Operation(module: market_activity_customer,operationId:'market_activity_customer_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
            }

            if(Module.findByModuleId('install_order_pending')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/install_order_pending.json").exists()) {
                def install_order_pending = new Module(moduleId: 'install_order_pending', moduleName: '待处理', parentModule: Module.findByModuleId('install_mgmt'),
                        model: Model.findByModelClass('com.uniproud.wcb.InstallOrder'), ctrl: 'InstallOrderPendingController', vw: 'installOrderPendingList').save()
                new Operation(module: install_order_pending, operationId: 'install_order_pending_update', text: '修改', vw: 'installOrderPendingEdit', auto: true, isCustom: true, clientType: 'all', type: 'update', showWin: true, autodisabled: true, optRecords: 'one').save()
                new Operation(module: install_order_pending, operationId: 'install_order_pending_view', text: '查看', vw: 'installOrderPendingView', auto: true, isCustom: true, clientType: 'all', type: 'view', showWin: true, autodisabled: true, optRecords: 'one').save()
            }
        if(Module.findByModuleId('install_allocate_detail')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/install_allocate_detail.json").exists()) {
            def install_allocate_detail = new Module(moduleId:'install_allocate_detail',moduleName:'预派单',parentModule:Module.findByModuleId('install_mgmt'),
                    model: Model.findByModelClass('com.uniproud.wcb.InstallAllocateDetail'),ctrl: 'InstallAllocateDetailController',vw:'installAllocateDetailList').save()
            new Operation(module: install_allocate_detail, operationId: 'install_allocate_detail_view', text: '查看', vw: 'installAllocateDetailView', auto: true, isCustom: true, clientType: 'all', type: 'view', showWin: true, autodisabled: true, optRecords: 'one').save()
            new Operation(module: install_allocate_detail, operationId: 'install_allocate_detail_grab', text: '抢单', auto: false, clientType: 'all', type: 'update', autodisabled: true, optRecords: 'one').save()
            new Operation(module: install_allocate_detail, operationId: 'install_allocate_detail_confirm', text: '定单', auto: false, clientType: 'all', type: 'update', autodisabled: true, optRecords: 'one').save()
        }
            if(Module.findByModuleId('customer_service_mgmt')==null) {
                new Module(moduleId: 'customer_service_mgmt',moduleName:'客服管理').save()
            }
        if(Module.findByModuleId('customer_revisit')==null  && new File("${grailsApplication.config.MODULE_FILE_PATH}/customer_revisit.json").exists()) {
            def customer_revisit = new Module(moduleId: 'customer_revisit', moduleName: '客户回访', parentModule: Module.findByModuleId('customer_service_mgmt'),
                    model: Model.findByModelClass('com.uniproud.wcb.CustomerRevisit'), ctrl:'CustomerRevisitController', vw: 'customerRevisitMain').save()
            new Operation(module: customer_revisit,operationId:'customer_revisit_add',text:'新增',vw:'customerRevisitAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
            new Operation(module: customer_revisit,operationId:'customer_revisit_update',text:'修改',vw:'customerRevisitEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
            new Operation(module: customer_revisit,operationId:'customer_revisit_view',text:'查看',vw:'customerRevisitView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            new Operation(module: customer_revisit,operationId:'customer_revisit_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        }
        if(Module.findByModuleId('customer_care')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/customer_care.json").exists()) {
            def customer_care = new Module(moduleId: 'customer_care', moduleName: '客户关怀', parentModule: Module.findByModuleId('customer_service_mgmt'),
                    model:Model.findByModelClass('com.uniproud.wcb.CustomerCare'), ctrl:'CustomerCareController', vw: 'customerCareMain').save()
            new Operation(module: customer_care,operationId:'customer_care_add',text:'新增',vw:'customerCareAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
            new Operation(module: customer_care,operationId:'customer_care_update',text:'修改',vw:'customerCareEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
            new Operation(module: customer_care,operationId:'customer_care_view',text:'查看',vw:'customerCareView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            new Operation(module: customer_care,operationId:'customer_care_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        }
        if(Module.findByModuleId('customer_complaints')==null && new File("${grailsApplication.config.MODULE_FILE_PATH}/customer_complaints.json").exists()) {
            def customer_complaints = new Module(moduleId: 'customer_complaints', moduleName: '客户投诉', parentModule: Module.findByModuleId('customer_service_mgmt'),
                    model:Model.findByModelClass('com.uniproud.wcb.CustomerComplaints'), ctrl:'CustomerComplaintsController', vw: 'customerComplaintsMain').save()
            new Operation(module: customer_complaints,operationId:'customer_complaints_add',text:'新增',vw:'customerComplaintsAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
            new Operation(module: customer_complaints,operationId:'customer_complaints_update',text:'修改',vw:'customerComplaintsEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
            new Operation(module: customer_complaints,operationId:'customer_complaints_view',text:'查看',vw:'customerComplaintsView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
            new Operation(module: customer_complaints,operationId:'customer_complaints_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        }
//        }
    }
    /**
     * 初始化所有企业模板的版本信息
     */
    def initAllUserModuleVer(){
        User.list().each {user ->
            initUserModuleVer(user)
        }
    }

    /**
     * 初始化企业模板
     * @param user
     * @return
     */
    @Listener(topic = 'initUmv')
    @Transactional
    def initUserModuleVer(User user){
        User.withNewSession {
            user = User.get(user.id)
            if (user) {
                user?.modules?.each { module ->
                    def umv = UserModuleVersion.findByUserAndModule(user, module)
                    if (umv) {
                        umv.ver = umv.ver + 1
                        umv.save()
                    } else {
                        try {
                            new UserModuleVersion(user: user, module: module, ver: 1).save()
                        } catch (ex) {
                        }
                    }
                }
            }
        }
    }

    @Transactional
    def createDemo(){
        if(User.findByUserId('uniproud')) return
        def agentCao = new Agent(name: '曹操',agentId: 'agent',password: 'agent',email: 'www.sanguo_cao@163.com',phone: '132xxxxxxxx',isAllowLowerAgent: true).save()
        def agentCao_1 = new Agent(parentAgent: agentCao, name: '典韦',agentId: 'wei_dian',password: 'wei_dian',email: 'www.sanguo_dian@163.com',phone: '1321xxxxxxx').save()
        def agentCao_2 = new Agent(parentAgent: agentCao,name: '徐晃',agentId: 'huang_xu',password: 'huang_xu',email: 'www.sanguo_xu@163.com',phone: '1322xxxxxxx').save()
        def agentSun = new Agent(name: '孙权',agentId: 'quan_sun',password: 'quan_sun',email: 'www.sanguo_sun@163.com',phone: '155xxxxxxxx').save()
        def agentSun_1 = new Agent(parentAgent: agentSun,name: '大乔',agentId: 'qiao_qiao',password: 'qiao_qiao',email: 'www.sanguo_qiao@163.com',phone: '152xxxxxxxx').save()

        def edition1 = new Edition(name:'销售通免费版',kind:1,ver:1,monthlyFee:0,remark:'免费推广').save()
        def edition2 = new Edition(name:'销售通标准版',kind:1,ver:2,monthlyFee:25,remark:'').save()
        def edition3 = new Edition(name:'销售通高级版',kind:1,ver:3,monthlyFee:35,remark:'').save()
        def edition4 = new Edition(name:'服务通免费版',kind:2,ver:1,monthlyFee:0,remark:'免费推广').save()
        def edition5 = new Edition(name:'服务通标准版',kind:2,ver:2,monthlyFee:20,remark:'').save()
        def edition6 = new Edition(name:'服务通高级版',kind:2,ver:3,monthlyFee:30,remark:'').save()
        def edition7 = new Edition(name:'服务通标准版',kind:3,ver:2,monthlyFee:35,remark:'').save()
        def edition8 = new Edition(name:'服务通高级版',kind:3,ver:3,monthlyFee:50,remark:'').save()

        def user1 = new User(userId:'uniproud',name:'销售通高级版',agent: agentCao,balance:100000,sumAddBalance:100000,sumAddRealBalance:80000,edition:edition3,isTest:false,allowedNum:50,monthlyFee:(edition3.monthlyFee)*50,dueDate:new Date(),enabled:true,isTemplate: true)
        user1.addToModules(Module.findByModuleId('customer_mgmt'))
        user1.addToModules(Module.findByModuleId('public_customer'))
        user1.addToModules(Module.findByModuleId('customer'))
        user1.addToModules(Module.findByModuleId('contact'))
        user1.addToModules(Module.findByModuleId('customer_follow'))
        user1.addToModules(Module.findByModuleId('sale_chance_mgmt'))
        user1.addToModules(Module.findByModuleId('sale_chance'))
        user1.addToModules(Module.findByModuleId('sale_chance_follow'))
        user1.addToModules(Module.findByModuleId('object_mgmt'))
        user1.addToModules(Module.findByModuleId('outsite_record'))
        user1.addToModules(Module.findByModuleId('onsite_object'))
        user1.addToModules(Module.findByModuleId('product_mgmt'))
        user1.addToModules(Module.findByModuleId('product_kind'))
        user1.addToModules(Module.findByModuleId('product'))
        user1.addToModules(Module.findByModuleId('plan_summary'))
        user1.addToModules(Module.findByModuleId('bulletin'))
        user1.addToModules(Module.findByModuleId('privilege'))
        user1.addToModules(Module.findByModuleId('object_follow'))
        user1.addToModules(Module.findByModuleId('work_mgmt'))
        user1.addToModules(Module.findByModuleId('service_mgmt'))
        user1.addToModules(Module.findByModuleId('service_task'))
        user1.addToModules(Module.findByModuleId('task_assigned'))
        user1.addToModules(Module.findByModuleId('base_mgmt'))
        user1.addToModules(Module.findByModuleId('employee'))
        user1.addToModules(Module.findByModuleId('dept'))
        user1.addToModules(Module.findByModuleId('work_audit'))
        user1.addToModules(Module.findByModuleId('apply_mgmt'))
        user1.addToModules(Module.findByModuleId('audit_opinion'))
        user1.addToModules(Module.findByModuleId('leave_apply'))
        user1.addToModules(Module.findByModuleId('business_trip_apply'))
        user1.addToModules(Module.findByModuleId('overtime_apply'))
        user1.addToModules(Module.findByModuleId('goout_apply'))
        user1.addToModules(Module.findByModuleId('attendance_data'))
        user1.addToModules(Module.findByModuleId('attendance_model'))
        user1.addToModules(Module.findByModuleId('attendance_statistics'))
        user1.addToModules(Module.findByModuleId('attendance_calendar'))
        user1.addToModules(Module.findByModuleId('work_day'))
        user1.addToModules(Module.findByModuleId('contract_order_mgmt'))//订单管理
        user1.addToModules(Module.findByModuleId('contract_order'))
        user1.addToModules(Module.findByModuleId('contract_order_detail'))
        user1.addToModules(Module.findByModuleId('market_mgmt'))
        user1.addToModules(Module.findByModuleId('market_activity'))
        user1.addToModules(Module.findByModuleId('competitor_mgmt'))
        user1.addToModules(Module.findByModuleId('competitor'))
        user1.addToModules(Module.findByModuleId('competitor_product'))
        user1.addToModules(Module.findByModuleId('competitor_dynamic'))
        user1.addToModules(Module.findByModuleId('locus'))
        user1.addToModules(Module.findByModuleId('base_dict'))
        user1.addToModules(Module.findByModuleId('employee_modify_pwd'))
        user1.addToModules(Module.findByModuleId('system_mgmt'))
        user1.addToModules(Module.findByModuleId('login_log'))
        user1.addToModules(Module.findByModuleId('add_balance'))
        user1.addToModules(Module.findByModuleId('employee_locus'))
        user1.addToModules(Module.findByModuleId('notify_model'))
        user1.addToModules(Module.findByModuleId('employee_notify_model'))
        user1.addToModules(Module.findByModuleId('notify'))
        user1.addToModules(Module.findByModuleId('payment_record'))
        user1.addToModules(Module.findByModuleId('employee_check'))
        user1.addToModules(Module.findByModuleId('user_mgmt'))

        user1.save()
        /*测试数据    begin*/
        def dept = new Dept(name:'上海傲融公司',dateCreated :new Date(),lastUpdated : new Date(),user: user1).save()
        def dept2 = new Dept(name:'上海公司',dateCreated :new Date(),lastUpdated : new Date(),user: user1,parentDept: dept).save()
        def dept3 = new Dept(name:'北京分公司',dateCreated :new Date(),lastUpdated : new Date(),user: user1,parentDept: dept).save()
        def dept4 = new Dept(name:'广州分公司',dateCreated :new Date(),lastUpdated : new Date(),user: user1).save()
        def dept5 = new Dept(name:'技术部',dateCreated :new Date(),lastUpdated : new Date(),user: user1,parentDept: dept2).save()
        def dept6 = new Dept(name:'销售部',dateCreated :new Date(),lastUpdated : new Date(),user: user1,parentDept: dept2).save()

        def privilege = new Privilege(user:user1,name:'系统管理员').save()
        def employee = new Employee(name:'总经理',account: 'admin',password:'admin',email:'admin@163.com',checkEmail: true,phone : '02151695988',mobile:'18401955862',checkMobile: true,fax : '11202212',enabled:'1',accountLocked:'0',user: user1)
        employee.addToPrivileges(privilege)
        employee.save()

        def attendanceModel = new AttendanceModel(name:'测试模板',user:user1,employee: employee,location: '上海傲融软件技术有限公司',longtitude: '121.538822',latitude: '31.220899',maxEarlyWork: 10,maxLateTime: 10,startTime1: '09:00',endTime1: '12:00',startTime2: '13:00',endTime2: '18:00',maxDistance:500.0,timeMode: 2,statDay:25,monday:true,tuesday: true,wednesday: true,thursday: true,friday: true).save()

        def qianjianguo = new Employee(parentEmployee:employee,name:'钱建国',account:'qianjianguo',password:'1',email:'jianguo_qian@uniproud.com',phone : '618',mobile:'13701709306',fax : '11202212',enabled:'1',accountLocked:'0',user: user1,dept:dept,location: '上海市徐汇区东泉路65号',longtitude: '121.45084',latitude: '31.161506',locusDate: new Date()).save()
        def qiaoxu = new Employee(parentEmployee:qianjianguo,name:'乔旭',account: 'shqv',password:'1',email:'xu_qiao@uniproud.com',phone : '622',mobile:'13816842991',fax : '11202212',enabled:'1',accountLocked:'0',user: user1,dept:dept5,location: '上海市徐汇区中山南二路1001号-1',longtitude: '121.452548',latitude: '31.18805',locusDate: new Date()).save()
        def like = new Employee(parentEmployee:qianjianguo,name:'李珂',account: 'like',password:'1',email:'ke_li@uniproud.com',phone : '626',mobile:'13916110549',fax : '11202212',enabled:'1',accountLocked:'0',user: user1,dept:dept5,location: '江苏省常州市钟楼区清潭路73号',longtitude: '119.950487',latitude: '31.77431',locusDate: new Date()).save()
        def guozhen = new Employee(parentEmployee:qianjianguo,name:'郭振',account: 'zhenmei',password:'1',email:'zhen_guo@uniproud.com',checkEmail: true,phone : '623',mobile:'18701955862',fax : '11202212',enabled:'1',accountLocked:'0',user: user1,dept:dept5,location: '上海市浦东新区峨山路91弄-101号',longtitude: '121.538011',latitude: '31.223554',locusDate: new Date()).save()
        def dongwei = new Employee(parentEmployee:qianjianguo,name:'董威',account: 'dongwei',password:'1',email:'wei_dong@uniproud.com',phone : '678',mobile:'18321482348',fax : '11202212',enabled:'1',accountLocked:'0',user: user1,dept:dept5,location: '上海市浦东新区祖冲之路',longtitude: '121.63474501557',latitude: '31.216452827392',locusDate: new Date()).save()
        qianjianguo.addToPrivileges(privilege)
        qiaoxu.addToPrivileges(privilege)
        like.addToPrivileges(privilege)
        guozhen.addToPrivileges(privilege)
        dongwei.addToPrivileges(privilege)

        def user2 = new User(userId:'uniproud1',name: '销售通标准版',agent: agentCao_1,balance:100000,sumAddBalance:100000,sumAddRealBalance:80000,isTest:false,allowedNum:10,monthlyFee:10*(edition1.monthlyFee),dueDate:new Date(),enabled:true,isTemplate: true)
        user2.addToModules(Module.findByModuleId('customer_mgmt'))
        user2.addToModules(Module.findByModuleId('customer'))
        user2.addToModules(Module.findByModuleId('customer_follow'))
        user2.addToModules(Module.findByModuleId('base_mgmt'))
        user2.addToModules(Module.findByModuleId('employee'))
        user2.save()

        def privilege2 = new Privilege(user2:user1,name:'系统管理员').save()
        def user3 = new User(userId:'uniproud2',name: '销售通免费版',agent: agentSun,balance:100000,sumAddBalance:100000,sumAddRealBalance:80000,isTest:false,allowedNum:100,monthlyFee:100*(edition5.monthlyFee),dueDate:new Date(),enabled:true,isTemplate: true)
        user3.addToModules(Module.findByModuleId('customer_mgmt'))
        user3.addToModules(Module.findByModuleId('customer'))
        user3.addToModules(Module.findByModuleId('contact'))
        user3.addToModules(Module.findByModuleId('customer_follow'))
        user3.addToModules(Module.findByModuleId('base_mgmt'))
        user3.addToModules(Module.findByModuleId('employee'))
        user3.save()
        def privilege3 = new Privilege(user2:user1,name:'系统管理员').save()


        def employee1 = new Employee(name:'销售经理',password:'xiaoshou',email:'xiaoshou@163.com',phone : '02151695988',mobile:'18601955862',fax : '11202212',enabled:'1',accountLocked:'0',user: user1).save()
        def employee2 = new Employee(name:'财务总监',password:'caiwu',email:'caiwu@163.com',phone : '02151695988',mobile:'18201955862',fax : '11202212',enabled:'1',accountLocked:'0',user: user1).save()

        def customerObj1 = new Customer(name:'上海电信',email:'dianxin@163.com',phone:'02151695988',user: user1,employee: qiaoxu,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        def customerObj2 = new Customer(name:'中国移动',email:'dianxin@163.com',phone:'02151695988',user: user1,employee: qiaoxu,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        def customerObj3 = new Customer(name:'上海傲融软件技术有限公司',email:'aorong@163.com',phone:'02151695988',user: user1,employee: like,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'测试',email:'sq@163.com',phone:'02151695988',user: user1,employee: like,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'中国电信',email:'aa@163.com',phone:'02151695988',user: user1,employee: like,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'铁通',email:'tt@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'网易',email:'wy@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'金凤科技',email:'tt@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'江南造船厂',email:'zaochuanchang@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'兴业银行',email:'yingye@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'壳牌统一',email:'qiaopaitongyi@163.com',phone:'02151695988',user: user1,employee: dongwei,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'莫泰168',email:'motai@163.com',phone:'02151695988',user: user1,employee: dongwei,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'YKK',email:'ykk@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'DELL',email:'dell@163.com',phone:'02151695988',user: user1,employee: dongwei,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'HP',email:'hp@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'春秋航空',email:'chunqiu@163.com',phone:'02151695988',user: user1,employee: dongwei,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'浦东新区建设（集团）有限公司',email:'pudongjianshe@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'江阴苏龙发电有限公司等',email:'sulongdianchang@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'广州南方李锦记',email:'lijinji@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'太平洋人寿保险公司上海分公司',email:'zhongguorenshou@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 1,level: 1,creditLevel: 1,customerType: 2,customerState:3,allocatee: qiaoxu,allocatedDate: new Date(),owner: qiaoxu).save()
        new Customer(name:'aaa',email:'aaaa@163.com',phone:'02151695988',user: user1,employee: guozhen,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'bbb',email:'bbb@163.com',phone:'02151695988',user: user1,employee: guozhen,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'ccc',email:'bbbb@163.com',phone:'02151695988',user: user1,employee: guozhen,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'ddd',email:'ddd@163.com',phone:'02151695988',user: user1,employee: guozhen,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'eee',email:'eeee@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'fff',email:'fff@163.com',phone:'02151695988',user: user1,employee: dongwei,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'ggg',email:'ggg@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'hhh',email:'hhhh@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'iii',email:'iii@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()
        new Customer(name:'jjj',email:'jjj@163.com',phone:'02151695988',user: user1,employee: employee,phase: 1,kind: 2,level: 1,creditLevel: 1,customerType: 1,customerState:1).save()

        def contactObj1 = new Contact(name: '钱经理',gender : 1,email1 : 'qian@163.com',kind: 1,phone1:'02151695988',customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '钱经理1',gender : 1,email1 : 'qianqq@163.com',kind: 1,phone1:'02151695988',customer: customerObj3,user: user1,employee: qiaoxu).save()
        new Contact(name: '钱经理2',gender : 1,email1 : 'qian22@163.com',kind: 1,phone1:'02151695988',customer: customerObj3,user: user1,employee: qiaoxu).save()
        new Contact(name: '联系人3',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人4',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人5',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人6',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人7',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人8',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人9',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人10',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人11',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人12',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人13',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人14',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人15',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人16',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人17',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人18',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人19',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人20',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人21',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人22',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人23',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人24',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人25',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()
        new Contact(name: '联系人26',gender : 1,email1 : 'qian22@163.com',kind: 1,customer: customerObj3,user: user1,employee: employee).save()

        def contactObj2 = new Contact(name: '李跃',gender : 0,email1 : '10086@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '张三',gender : 0,email1 : 'zhangsan@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '李四2',gender : 0,email1 : 'lisi@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '王五3',gender : 0,email1 : 'wangwu@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '张飞',gender : 0,email1 : 'zhangfei@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '关羽',gender : 0,email1 : 'guanyu@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '刘备',gender : 0,email1 : 'liubei@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '马超',gender : 0,email1 : 'machao@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '周瑜',gender : 0,email1 : 'zhouyu@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '黄盖',gender : 0,email1 : 'huanggai@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '曹操',gender : 0,email1 : 'caocao@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '李典',gender : 0,email1 : 'lidian@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: dongwei).save()
        new Contact(name: '张辽',gender : 0,email1 : 'zhangliao@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '武松',gender : 0,email1 : 'wusong@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '鲁智深',gender : 0,email1 : 'luzhishen@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '宋江',gender : 0,email1 : 'songjiang@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '吴用',gender : 0,email1 : 'wuyong@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '张青',gender : 0,email1 : 'zhangqing@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '扈三娘',gender : 0,email1 : 'husanniang@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '李元霸',gender : 0,email1 : 'liyuanba@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '李渊',gender : 0,email1 : 'liyuan@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '努尔哈赤',gender : 0,email1 : 'nuerhachi@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '曾国藩',gender : 0,email1 : 'zengguofan@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '夏侯惇',gender : 0,email1 : 'xiahoudun@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '夏侯渊',gender : 0,email1 : 'xiahouyuan@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '郭嘉',gender : 0,email1 : 'guojia@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '魏延',gender : 0,email1 : 'weiyan@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '诸葛亮',gender : 0,email1 : 'zhugeliang@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: dongwei).save()
        new Contact(name: '管仲',gender : 0,email1 : 'guanzhong@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '司马懿',gender : 0,email1 : 'simayi@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: employee).save()
        new Contact(name: '许褚',gender : 0,email1 : 'xuchu@163.com',kind: 1,phone1:'10086',customer: customerObj2,user: user1,employee: dongwei).save()

        def note1 = new Note(user:user1,employee:employee,customer:customerObj3,tag:1,content:'工作提前一天完成，请审阅 ！',location:'峨山路',longtitude:'35.33',latitude:'95.68').save()
        note1.addToAts(qianjianguo)
        note1.addToZs(new NoteZs(note:note1,employee:qiaoxu).save())
        def commentn1 = new Comment(employee:like,note:note1,content:'恭喜！').save()
        def commentn2 = new Comment(employee:guozhen,note:note1,content:'哎呀。。。。。。').save()
        note1.addToComments(commentn1)
        note1.addToComments(commentn2)
        note1.save()

        def cFObj1 = new CustomerFollow(subject: '客户跟进测试',followKind: 1,beforePhase: 1,afterPhase: 2,followResult: 1, remark: '客户想定制开发',user: user1,employee: employee,customer: customerObj2,contact: contactObj2).save()
        def cFObj2 = new CustomerFollow(subject: '拜访客户后跟进',followKind: 1,beforePhase: 2,afterPhase: 2,followResult: 1, remark: '客户要求一个月之内实施完成',user: user1,employee: employee,customer: customerObj3,contact: contactObj1).save()

        def outsiteRecord = new OutsiteRecord(user: user1,employee: employee,customerFollow: cFObj1,objectType: 1,actionKind: 1,remark: '测试',longtitude: '121.4',latitude: '31.2',location: '上海陆家嘴').save()

        def onsiteObject = new OnsiteObject(remark: '测试',longtitude: '121.4',latitude: '31.2',location: '上海陆家嘴',objectName: '测试啊',user: user1,employee: employee,customer: customerObj2,contact: contactObj2).save()

        def oFObj1 = new ObjectFollow(subject: '客户跟进测试',followKind: 1,beforePhase: 1,afterPhase: 2,followResult: 1, remark: '客户想定制开发',user: user1,employee: employee,onsiteObject: onsiteObject).save()
        def oFObj2 = new ObjectFollow(subject: '拜访客户后跟进',followKind: 1,beforePhase: 2,afterPhase: 2,followResult: 1, remark: '客户要求一个月之内实施完成',user: user1,employee: employee,onsiteObject: onsiteObject).save()

        def productKind = new ProductKind(user:user1,kindType: 1,name: 'A类产品').save()
        def productKind_1 = new ProductKind(user:user1,kindType: 1,name: 'A类产品_1',parentKind: productKind).save()

        def product1 = new Product(user: user1,employee: employee,name: '苹果6',productKind: productKind,productModel:'20*30',productNo:'CP00001',unit:1,salePrice:199,costPrice:99).save()
        def product2 = new Product(user: user1,employee: employee,name: 'IPhone5S',productKind: productKind,productModel:'30*30',productNo:'CP00002',unit:2,salePrice:3999,costPrice:2999).save()

        def serviceTask1 = new ServiceTask(subject:'服务单1',followKind: 1,beforePhase: 1,afterPhase: 2,followResult: 1, remark: '现场实施',user: user1,employee: employee,customer: customerObj2,contact: contactObj2).save()
        def serviceTask2 = new ServiceTask(subject:'服务单2',followKind: 1,beforePhase: 1,afterPhase: 2,followResult: 1, remark: '售后维修',user: user1,employee: employee,customer: customerObj2,contact: contactObj2).save()

        def planSummary1 = new PlanSummary(user: user1,employee: employee,subject:'日报-小布',summarys:'今日任务已完成',plans:'明日再和小婵大战三百回合',type:1,state:2).save()
        def planSummary2 = new PlanSummary(user: user1,employee: employee,subject:'日报-小婵',summarys:'小布好没用，不够嘛！',plans:'明日不会放过他的',type:1,state:2).save()

        def taskAssigned1 = new TaskAssigned(user:user1,employee: employee,customer:customerObj3 ,taskContent:'本周需完成任务交办服务端',state:1)
        //taskAssigned1.addToExecutor(employee)
        taskAssigned1.addToExecutor(guozhen)
        taskAssigned1.addToCc(employee1)
        def comment1 = new Comment(employee:employee,content:'很好').save()
        taskAssigned1.addToComments(comment1)
        taskAssigned1.save()
        def taskAssigned2 = new TaskAssigned(user:user1,employee: employee,customer:customerObj3,taskContent:'手机端日志页面完善',state:1)
        taskAssigned2.addToExecutor(employee1)
        taskAssigned2.addToCc(employee)
        def comment2 = new Comment(employee:employee,content:'很好的').save()
        taskAssigned2.addToComments(comment2)
        taskAssigned2.save()

        /******************审核********start*************/
        //employee申请，employee1待审核
        def audit1 = new Audit(user: user1,employee: employee,auditor:employee1,subject:'',type:1,qz:'goout',auditState:1).save()
        def goout1 = new GoOutApply(user: user1,employee: employee,audit:audit1,auditor:employee1,subject:'税务局',content:'到税务局学习新的财税政策',customer:customerObj1,contact:contactObj1)
        goout1.save()
        audit1.setSubject(goout1.subject)
        audit1.setGoout(goout1)
        def nowAuditOpinion1 = new AuditOpinion(user: user1,employee: employee,auditor:employee1,audit:audit1,state:1/*未审核*/,content:'')
        nowAuditOpinion1.save()
        audit1.setNowAuditOpinion(nowAuditOpinion1)
        audit1.save()
        //employee1申请，employee审核通过
        def audit2 = new Audit(user: user1,employee: employee1,auditor:employee,subject:'',type:1,qz:'goout',auditState:1).save()
        def goout2 = new GoOutApply(user: user1,employee: employee1,audit:audit2,auditor:employee,subject:'拜访客户',content:'售前调研',customer:customerObj2,contact:contactObj2).save()
        audit2.setSubject(goout2.subject)
        audit2.setGoout(goout2)
        def nowAuditOpinion2 = new AuditOpinion(user: user1,employee: employee1,auditor:employee,audit:audit2,state:1/*未审核*/,content:'')
        nowAuditOpinion2.save()
        audit2.setNowAuditOpinion(nowAuditOpinion2)
        audit2.save()
        nowAuditOpinion2.setState(2)//通过
        nowAuditOpinion2.setContent("同意！")
        nowAuditOpinion2.save()
        audit2.setAuditState(3)//审核通过
        audit2.addToAuditOpinions(nowAuditOpinion2)
        audit2.addToAuditors(employee)
        audit2.save()
        //employee申请，employee1审核通过
        def audit3 = new Audit(user: user1,employee: employee,auditor:employee1,subject:'',type:2,qz:'trip',auditState:1).save()
        def trip1 = new BusinessTripApply(user: user1,employee: employee,audit:audit3,auditor:employee1,subject:'北京分公司',content:'项目支持',customer:customerObj1,contact:contactObj1,aimAddress:'北京',fare:2000).save()
        audit3.setSubject(trip1.subject)
        audit3.setTrip(trip1)
        def nowAuditOpinion3 = new AuditOpinion(user: user1,employee: employee,auditor:employee1,audit:audit3,state:1/*未审核*/,content:'').save()
        audit3.setNowAuditOpinion(nowAuditOpinion3)
        audit3.save()
        nowAuditOpinion3.setState(2)//通过
        nowAuditOpinion3.setContent("同意！")
        nowAuditOpinion3.save()
        audit3.setAuditState(3)//审核通过
        audit3.addToAuditOpinions(nowAuditOpinion3)
        audit3.addToAuditors(employee1)
        audit3.save()
        //employee2申请，employee通过并继续流转，employee1待审核
        def audit4 = new Audit(user: user1,employee: employee2,auditor:employee,subject:'',type:2,qz:'trip',auditState:1).save()
        def trip2 = new BusinessTripApply(user: user1,employee: employee2,audit:audit4,auditor:employee,subject:'广州分公司',content:'项目支持',customer:customerObj1,contact:contactObj1,aimAddress:'广州',fare:1999.99).save()
        audit4.setSubject(trip2.subject)
        audit4.setTrip(trip2);
        def nowAuditOpinion4 = new AuditOpinion(user: user1,employee: employee2,auditor:employee,audit:audit4,state:1/*未审核*/,content:'').save()
        audit4.setNowAuditOpinion(nowAuditOpinion4)
        audit4.save()
        nowAuditOpinion4.setState(3)//通过，继续流转
        nowAuditOpinion4.setContent("同意！请employee1审核")
        nowAuditOpinion4.save()
        audit4.setAuditState(2)//审核中
        audit4.addToAuditOpinions(nowAuditOpinion4)
        audit4.addToAuditors(employee)
        def nowAuditOpinion9 = new AuditOpinion(user: user1,employee: employee,auditor:employee1,audit:audit4,state:1/*未审核*/,content:'').save()
        audit4.setNowAuditOpinion(nowAuditOpinion9)//当前审核意见
        audit4.setAuditor(employee1)//当前审核人
        audit4.save()
        //employee申请，employee1通过继续流转，employee2审核通过
        def audit5 = new Audit(user: user1,employee: employee,auditor:employee1,subject:'',type:3,qz:'leave',auditState:1).save()
        def leave1 = new LeaveApply(user: user1,employee: employee,audit:audit5,auditor:employee1,type:1/*事假*/,timeType:1/*天*/,subject:'事假一天',content:'双11防止老婆败家，特此请假一天').save()
        audit5.setSubject(leave1.subject)
        audit5.setLeave(leave1)
        def nowAuditOpinion5 = new AuditOpinion(user: user1,employee: employee,auditor:employee1,audit:audit5,state:1/*未审核*/,content:'').save()
        audit5.setNowAuditOpinion(nowAuditOpinion5)
        audit5.save()
        nowAuditOpinion5.setState(3)//通过，继续流转
        nowAuditOpinion5.setContent("同意！请employee2审核")
        nowAuditOpinion5.save()
        audit5.setAuditState(2)//审核中
        audit5.addToAuditOpinions(nowAuditOpinion5)
        audit5.addToAuditors(employee1)
        def nowAuditOpinion10 = new AuditOpinion(user: user1,employee: employee1,auditor:employee2,audit:audit5,state:1/*未审核*/,content:'').save()
        audit5.setNowAuditOpinion(nowAuditOpinion9)//当前审核意见
        audit5.setAuditor(employee1)//当前审核人
        audit5.save()
        nowAuditOpinion10.setState(2)
        nowAuditOpinion10.setContent("兄弟，记住登陆支付宝账号时连续输错3次密码啊！")
        nowAuditOpinion10.save()
        audit5.setAuditState(3)//审核通过
        audit5.addToAuditOpinions(nowAuditOpinion10)
        audit5.addToAuditors(employee2)
        audit5.save()
        //employee1申请，employee2通过并继续流转，employee待审核
        def audit6 = new Audit(user: user1,employee: employee1,auditor:employee2,subject:'',type:3,qz:'leave',auditState:1).save()
        def leave2 = new LeaveApply(user: user1,employee: employee1,audit:audit6,auditor:employee2,type:2/*婚假*/,timeType:1/*天*/,subject:'事假一周',content:'婚姻大事，特此请假').save()
        audit6.setSubject(leave2.subject)
        audit6.setLeave(leave2)
        def nowAuditOpinion6 = new AuditOpinion(user: user1,employee: employee1,auditor:employee2,audit:audit6,state:1/*未审核*/,content:'').save()
        audit6.setNowAuditOpinion(nowAuditOpinion6)
        audit6.save()
        nowAuditOpinion6.setState(3)//通过，继续流转
        nowAuditOpinion6.setContent("同意！请employee审核")
        nowAuditOpinion6.save()
        audit6.setAuditState(2)//审核中
        audit6.addToAuditOpinions(nowAuditOpinion6)
        audit6.addToAuditors(employee2)
        def nowAuditOpinion11 = new AuditOpinion(user: user1,employee: employee2,auditor:employee,audit:audit6,state:1/*未审核*/,content:'').save()
        audit6.setNowAuditOpinion(nowAuditOpinion11)//当前审核意见
        audit6.setAuditor(employee)//当前审核人
        audit6.save()

        def audit7 = new Audit(user: user1,employee: employee,auditor:employee1,subject:'',type:4,qz:'overtime',auditState:1).save()
        def overtime1 = new OvertimeApply(user: user1,employee: employee,audit:audit7,auditor:employee1,subject:'晚加班',type:'1',place:1,content:'赶项目进度').save()
        audit7.setSubject(overtime1.subject)
        audit7.setOvertime(overtime1)
        def nowAuditOpinion7 = new AuditOpinion(user: user1,employee: employee,auditor:employee1,audit:audit7,state:1/*未审核*/,content:'').save()
        audit7.setNowAuditOpinion(nowAuditOpinion7)
        audit7.save()

        def audit8 = new Audit(user: user1,employee: employee2,auditor:employee1,subject:'',type:4,qz:'overtime',auditState:1).save()
        def overtime2 = new OvertimeApply(user: user1,employee: employee2,audit:audit8,auditor:employee1,subject:'客户现场加班',type:'2',place:2,content:'周末到客户现象维护服务器').save()
        audit8.setSubject(overtime2.subject)
        audit8.setOvertime(overtime2)
        def nowAuditOpinion8 = new AuditOpinion(user: user1,employee: employee2,auditor:employee1,audit:audit8,state:1/*未审核*/,content:'').save()
        audit8.setNowAuditOpinion(nowAuditOpinion8)
        audit8.save()

        /******************审核********end*************/
/*
			def attendance = new Attendance(user: user1,employee: employee,location:'上海陆家嘴',longtitude:'121.4',latitude:'31.2',signDate:new Date().format("yyyy-MM-dd HH:mm:ss.S"),signType:1,remark:'上班').save()
			new Attendance(user: user1,employee: employee,location:'上海陆家嘴',longtitude:'121.4',latitude:'31.2',signDate:new Date().format("yyyy-MM-dd HH:mm:ss.S"),signType:2,remark:'下班').save()
*/

        def order1 = new ContractOrder(user: user1,employee: employee,subject:'测试订单',objectKind:1,effectiveDate:new Date().format("yyyy-MM-dd HH:mm:ss.S"),customer:customerObj1,remark:'测试').save()
        new ContractOrderDetail(user: user1,employee: employee,contractOrder:order1,product:product1,price:169,num:10).save()
        new ContractOrderDetail(user: user1,employee: employee,contractOrder:order1,product:product2,price:3899,num:1).save()
        def order2 = new ContractOrder(user: user1,employee: employee,subject:'销售单据',objectKind:1,effectiveDate:new Date().format("yyyy-MM-dd HH:mm:ss.S"),customer:customerObj2,remark:'测试').save()
        new ContractOrderDetail(user: user1,employee: employee,contractOrder:order2,product:product1,price:189,num:100).save()
        new ContractOrderDetail(user: user1,employee: employee,contractOrder:order2,product:product2,price:3799,num:2).save()

        def locus1 = new Locus(user: user1,employee: employee,startDate:new Date().format("yyyy-MM-dd HH:mm:ss.S"),latestDate:new Date().format("yyyy-MM-dd HH:mm:ss.S"),location:'上海市浦东新区峨山路91弄-101号',longtitude:'121.538011',latitude:'31.223554').save()
        new LocusDetail(user: user1,employee: employee,locus:locus1,location:'上海市浦东新区祖冲之路',longtitude:'121.63474501557',latitude:'31.216452827392',locusDate:new Date().format("yyyy-MM-dd HH:mm:ss.S")).save()
        new LocusDetail(user: user1,employee: employee,locus:locus1,location:'上海市浦东新区峨山路91弄-101号',longtitude:'121.538011',latitude:'31.223554',locusDate:new Date().format("yyyy-MM-dd HH:mm:ss.S")).save()

        def notifyModel = new NotifyModel(user: user1,employee: employee,name:'升级VIP客户提醒',isAuto:false,module:Module.findByModuleId('customer'),insertNotify:true,updateNotify: true,deleteFlag: false,notifyField:'owner',isNotifyMany:false,isDefaultRecv:true,isAllowForbid:true,subjectTemplate:'VIP客户提醒',contentTemplate:'您的客户省纪委VIP客户了！').save()
        def notifyModelFilter = new NotifyModelFilter(user: user1,employee: employee,notifyModel:notifyModel,module:Module.findByModuleId('customer'),name:'组一',childRelation:1).save()
        notifyModel.notifyModelFilter = notifyModelFilter
        notifyModel.save()
        def notifyModelFilterDetail = new NotifyModelFilterDetail(user: user1,employee: employee,notifyModelFilter:notifyModelFilter,name:'条件一',fieldName:'level',dbType:'java.lang.Integer',isDict:true,expectType:1,expectValue:2,operator:'==').save()

        def administrator = new Administrator(name: '刘备',adminId: 'admin',password: 'admin',email: 'www.sanguo_bei.com',phone: '187xxxxxxxx').save()

        def userFeedback = new UserFeedback(user: user1,employee: employee,kind: 2,content: '我要投诉，我们公司单身女同事太少了。。。').save()

        /******************************************工作台初始化***********************************************/
        def customerModule = Module.findByModuleId('customer')
        def cfModule = Module.findByModuleId('customer_follow')
        def doModule = Module.findByModuleId('contract_order')
        def stModule = Module.findByModuleId('service_task')
        def waModule = Module.findByModuleId('work_audit')
        def notifyModule = Module.findByModuleId('notify')
        def saleChanceModule = Module.findByModuleId('sale_chance')
        def aimPerformanceModule = Module.findByModuleId('aim_performance')
        def latestCustomerList = new Portal(module: customerModule,type: 1,title: '最新客户',height: 330,xtype: 'latestCustomerList').save()
        def latestCustomerFollowList = new Portal(module: cfModule,type: 1,title: '最新客户跟进',height: 330,xtype: 'latestCustomerFollowList').save()
        def latestSaleChanceList = new Portal(module: customerModule,type: 1,title: '最新商机',height: 330,xtype: 'latestSaleChanceList').save()
        def latestSaleChanceFollowList = new Portal(module: customerModule,type: 1,title: '最新商机跟进',height: 330,xtype: 'latestSaleChanceFollowList').save()
        def latestContractOrderList = new Portal(module: doModule,type: 1,title: '最新订单',height: 330,xtype: 'latestContractOrderList').save()
        def latestServiceTaskList = new Portal(module: stModule,type: 1,title: '最新服务派单',height: 330,xtype: 'latestServiceTaskList').save()
        def latestDealServiceTaskList = new Portal(module: stModule,type: 1,title: '最新服务处理',height: 330,xtype: 'latestDealServiceTaskList').save()
        def latestAuditList = new Portal(module: waModule,type: 1,title: '最新审核',height: 330,xtype: 'latestAuditList').save()
        def latestNotifyList = new Portal(module: notifyModule,type: 1,title: '最新消息',height: 330,xtype: 'latestNotifyList').save()
        def latestBulletinList = new Portal(module: customerModule,type: 1,title: '最新公告',height: 330,xtype: 'latestBulletinList').save()
        def customerFollowRankingList = new Portal(module: cfModule,type: 2,title: '客户跟进排行',height: 330,xtype: 'customerFollowRankingList').save()
        def customerAddRankingList = new Portal(module: customerModule,type: 2,title: '新增客户排行',height: 330,xtype: 'customerAddRankingList').save()
        def saleChanceRankingChart = new Portal(module: saleChanceModule,type: 2,title: '销售商机数量',height: 330,xtype: 'saleChanceRankingChart').save()
        def saleChanceMoneyRankingChart = new Portal(module: saleChanceModule,type: 2,title: '销售商机金额',height: 330,xtype: 'saleChanceMoneyRankingChart').save()
        def customerYearStatList = new Portal(module: customerModule,type: 1,title: '客户年度统计',height: 330,xtype: 'customerYearStatList').save()
        def contractOrderPaymentYearStatList = new Portal(module: doModule,type: 1,title: '订单回款年度统计',height: 330,xtype: 'contractOrderPaymentYearStatList').save()
        def contractOrderMoneyYearStatList = new Portal(module: doModule,type: 1,title: '订单销售额年度统计',height: 330,xtype: 'contractOrderMoneyYearStatList').save()
        def aimPerformanceYearStatChart = new Portal(module: doModule,type: 2,title: '目标业绩统计',height: 330,xtype: 'aimPerformanceYearStatChart').save()
        //def saleChanceFollowRankingList = new Portal(module: customerModule,type: 2,title: '商机跟进排行',height: 330,xtype: 'saleChanceFollowRankingList').save()
        //def contractOrderAmountRankingList = new Portal(module: doModule,type: 2,title: '销售额排行',height: 330,xtype: 'contractOrderAmountRankingList').save()

        def upLatestCustomerList = new UserPortal(user: user1,portal: latestCustomerList,title: '最新客户',height: 330,idx: 1).save()
        def upLatestCustomerFollowList = new UserPortal(user: user1,portal: latestCustomerFollowList,title: '最新客户跟进',height: 330,idx: 2).save()
        def upLatestSaleChanceList = new UserPortal(user: user1,portal: latestSaleChanceList,title: '最新商机',height: 330,idx: 3).save()
        def upLatestSaleChanceFollowList = new UserPortal(user: user1,portal: latestSaleChanceFollowList,title: '最新商机跟进',height: 330,idx: 4).save()
        def upLatestContractOrderList = new UserPortal(user: user1,portal: latestContractOrderList,title: '最新订单',height: 330,idx: 5).save()
        def upLatestServiceTaskList = new UserPortal(user: user1,portal: latestServiceTaskList,title: '最新服务派单',height: 330,idx: 6).save()
        def upLatestDealServiceTaskList = new UserPortal(user: user1,portal: latestDealServiceTaskList,title: '最新服务处理',height: 330,idx: 7).save()
        def upLatestAuditList = new UserPortal(user: user1,portal: latestAuditList,title: '最新审核',height: 330,idx: 8).save()
        def upLatestNotifyList = new UserPortal(user: user1,portal: latestNotifyList,title: '最新消息',height: 330,idx: 9).save()
        def upLatestBulletinList = new UserPortal(user: user1,portal: latestBulletinList,title: '最新公告',height: 330,idx: 10).save()
        def upCustomerFollowRankingList = new UserPortal(user: user1,portal: customerFollowRankingList,title: '客户跟进排行',height: 330,idx: 11).save()
        def upCustomerAddRankingList = new UserPortal(user: user1,portal: customerAddRankingList,title: '新增客户排行',height: 330,idx: 12).save()
        def upSaleChanceRankingChart = new UserPortal(user: user1,portal: saleChanceRankingChart,title: '销售商机数量',height: 330,idx: 13).save()
        def upSaleChanceMoneyRankingChart = new UserPortal(user: user1,portal: saleChanceMoneyRankingChart,title: '销售商机金额',height: 330,idx: 14).save()
        def upCustomerYearStatList = new UserPortal(user: user1,portal: customerYearStatList,title: '客户年度统计',height: 330,idx: 15).save()
        def upContractOrderPaymentYearStatList = new UserPortal(user: user1,portal: contractOrderPaymentYearStatList,title: '订单回款年度统计',height: 330,idx: 16).save()
        def upAimPerformanceYearStatChart = new UserPortal(user: user1,portal: aimPerformanceYearStatChart,title: '目标业绩年度统计',height: 330,idx: 17).save()
        def upContractOrderMoneyYearStatList = new UserPortal(user: user1,portal: contractOrderMoneyYearStatList,title: '订单销售额年度统计',height: 330,idx: 18).save()
        //def upSaleChanceFollowRankingList = new UserPortal(user: user1,portal: saleChanceFollowRankingList,title: '商机跟进排行',height: 330,idx: 13).save()
        //def upContractOrderAmountRankingList = new UserPortal(user: user1,portal: contractOrderAmountRankingList,title: '销售额排行',height: 330,idx: 14).save()
    }
}
