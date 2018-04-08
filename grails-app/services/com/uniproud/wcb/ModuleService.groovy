package com.uniproud.wcb

import grails.converters.JSON
import groovy.json.JsonSlurper
import org.apache.ivy.core.module.id.ModuleId;

import grails.transaction.Transactional

@Transactional
class ModuleService {
    def grailsApplication
    def userService
    def init(){
        /**
         * 客户管理模块
         */
        def customer_mgmt = new Module(moduleId: 'customer_mgmt',moduleName:'客户管理').save()

        def public_customer = new Module(moduleId:'public_customer',moduleName:'公海客户',parentModule:customer_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Customer'),ctrl: 'PublicCustomerController',vw:'publicCustomerMain').save()
        new Operation(module: public_customer,operationId:'public_customer_add',text:'新增',auto:true,showWin:true,isCustom:true,iconCls:'table_add',clientType:'all',type:'add',vw:'publicCustomerAdd',optRecords: 'no').save()
        new Operation(module: public_customer,operationId:'public_customer_update',text:'修改',auto:true,showWin:true,isCustom:true,iconCls:'table_save',clientType:'all',type:'update',autodisabled:true,vw:'publicCustomerEdit',optRecords: 'one').save()
        new Operation(module: public_customer,operationId:'public_customer_view',text:'查看',auto:true,showWin:true,isCustom:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'publicCustomerView',optRecords: 'one').save()
        new Operation(module: public_customer,operationId:'public_customer_delete',text:'删除',auto:true,iconCls:'table_remove',clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: public_customer,operationId:'public_customer_allocation',text:'分配',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'view',autodisabled:true,vw:'publicCustomerAllocation',optRecords: 'many').save()
        new Operation(module: public_customer,operationId:'public_customer_recover',text:'回收',auto:false,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,optRecords: 'many').save()
        new Operation(module: public_customer,operationId:'public_customer_apply',text:'申请',auto:false,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,optRecords: 'many').save()
        new Operation(module: public_customer,operationId:'public_customer_apply_audit',text:'申请审核',auto:true,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'publicCustomerApplyAudit',optRecords: 'many').save()
        new Operation(module: public_customer,operationId:'public_customer_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: public_customer,operationId:'public_customer_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()

        def customer = new Module(moduleId:'customer',moduleName:'我的客户',parentModule:customer_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Customer'),ctrl: 'CustomerController',vw:'customerMain').save()
        new Operation(module: customer,operationId:'customer_add',text:'新增',auto:true,isCustom:true,showWin:true,iconCls:'table_add',clientType:'all',type:'add',vw:'customerAdd',optRecords: 'no').save()
        new Operation(module: customer,operationId:'customer_update',text:'修改',auto:true,isCustom:true,showWin:true,iconCls:'table_save',clientType:'all',type:'update',autodisabled:true,vw:'customerEdit',optRecords: 'one').save()
        new Operation(module: customer,operationId:'customer_view',text:'查看',auto:true,isCustom:true,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'customerView',optRecords: 'one').save()
        new Operation(module: customer,operationId:'customer_delete',text:'删除',auto:true,iconCls:'table_remove',clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: customer,operationId:'customer_follow',text:'跟进',clientType:'all',auto:true,isCustom:true,showWin:true,type:'add',autodisabled:true,vw:'customerFollowAdd',optRecords: 'one',targetEl: 'customerMain customerFollowList').save()
        new Operation(module: customer,operationId:'customer_transfer',text:'转移',auto:false,showWin:true,iconCls:'table_save',clientType:'all',type:'add',autodisabled:true,vw:'customerTransfer',optRecords: 'many').save()
        new Operation(module: customer,operationId:'customer_recover',text:'回收',auto:false,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,optRecords: 'many').save()
        new Operation(module: customer,operationId:'customer_location',text:'定位',type:'update',clientType:'mobile',optRecords: 'one').save()
        new Operation(module: customer,operationId:'customer_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: customer,operationId:'customer_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: customer,operationId: 'customer_fare_claims',text:'报销',vw:'fareClaimsAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'customerMain fareClaimsList',optRecords: 'one').save()
//		new Operation(module: customer,operationId:'customer_sign',text:'签到',clientType:'mobile').save()
//		new Operation(module: customer,operationId:'customer_signout',text:'签退',clientType:'mobile').save()

        def contact = new Module(moduleId:'contact',moduleName:'我的联系人',parentModule:customer_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Contact'),ctrl:'ContactController',vw:'contactMain').save()
        new Operation(module: contact,operationId:'contact_add',text:'新增',auto:true,isCustom:true,showWin:true,iconCls:'table_add',clientType:'all',type:'add',vw:'contactAdd',optRecords: 'no').save()
        new Operation(module: contact,operationId:'contact_update',text:'修改',auto:true,isCustom:true,showWin:true,iconCls:'table_save',clientType:'all',type:'update',autodisabled:true,vw:'contactEdit',optRecords: 'one').save()
        new Operation(module: contact,operationId:'contact_view',text:'查看',auto:true,isCustom:true,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'contactView',optRecords: 'one').save()
        new Operation(module: contact,operationId:'contact_delete',text:'删除',auto:true,iconCls:'table_remove',clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: contact,operationId:'contact_transfer',text:'转移',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'add',autodisabled:true,vw:'baseTransfer',optRecords: 'many').save()
        new Operation(module: contact,operationId:'contact_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: contact,operationId:'contact_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()

        def customer_follow = new Module(moduleId:'customer_follow',moduleName:'客户跟进',parentModule:customer_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.CustomerFollow'),ctrl:'CustomerFollowController',vw:'customerFollowMain').save()
        new Operation(module: customer_follow,operationId:'customer_follow_add',text:'新增',vw:'customerFollowAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
        new Operation(module: customer_follow,operationId:'customer_follow_update',text:'修改',vw:'customerFollowEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_follow,operationId:'customer_follow_view',text:'查看',vw:'customerFollowView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_follow,operationId:'customer_follow_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: customer_follow,operationId:'customer_follow_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: customer_follow,operationId:'customer_follow_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: customer_follow,operationId:'customer_follow_sign',text:'签到',clientType:'mobile').save()
        new Operation(module: customer_follow,operationId:'customer_follow_signout',text:'签退',clientType:'mobile').save()
        new Operation(module: customer_follow,operationId:'customer_follow_photo',text:'拍照',clientType:'mobile').save()

        def customer_stat = new Module(moduleId:'customer_stat',moduleName:'客户统计',parentModule:customer_mgmt,
                ctrl:'CustomerController',vw:'customerStatMain').save()
        new Operation(module: customer_stat,operationId: 'customer_stat_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()
        /**
         * 销售商机管理模块
         */
        def sale_chance_mgmt = new Module(moduleId: 'sale_chance_mgmt',moduleName:'商机管理').save()

        def sale_chance = new Module(moduleId:'sale_chance',moduleName:'销售商机',parentModule:sale_chance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.SaleChance'),ctrl: 'SaleChanceController',vw: 'saleChanceMain').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_add',text:'新增',vw:'saleChanceAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_update',text:'修改',vw:'saleChanceEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_view',text:'查看',vw:'saleChanceView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: sale_chance,operationId:'sale_chance_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_follow',text:'跟进',vw:'saleChanceFollowAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_order',text:'订单',vw:'contractOrderAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,optRecords: 'one',targetEl: 'saleChanceMain contractOrderList').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_close',text:'关闭',vw:'saleChanceClose',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_revertClose',text:'反关闭',vw:'saleChanceRevertClose',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance,operationId:'sale_chance_locus',text:'商机分布',auto:true,showWin:true,clientType:'all',type:'view',vw:'saleChanceLocusMap',optRecords: 'no').save()
        new Operation(module: sale_chance,operationId:'sale_chance_transfer',text:'转移',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'add',autodisabled:true,vw:'baseTransfer',optRecords: 'many').save()
        new Operation(module: sale_chance,operationId: 'sale_chance_fare_claims',text:'报销',vw:'fareClaimsAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'saleChanceMain fareClaimsList',optRecords: 'one').save()

        def sale_chance_detail = new Module(moduleId:'sale_chance_detail',moduleName:'商机明细',parentModule:sale_chance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.SaleChanceDetail'),ctrl:'SaleChanceController',vw:'saleChanceDetailList').save()
        new Operation(module: sale_chance_detail,operationId: 'sale_chance_detail_view',text:'查看',vw:'saleChanceDetailView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: sale_chance_detail,operationId:'sale_chance_detail_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()

        def sale_chance_follow = new Module(moduleId:'sale_chance_follow',moduleName:'商机跟进',parentModule:sale_chance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.SaleChanceFollow'),ctrl: 'SaleChanceFollowController',vw: 'saleChanceFollowMain').save()
        new Operation(module: sale_chance_follow,operationId: 'sale_chance_follow_add',text:'新增',vw:'saleChanceFollowAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: sale_chance_follow,operationId: 'sale_chance_follow_update',text:'修改',vw:'saleChanceFollowEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance_follow,operationId: 'sale_chance_follow_view',text:'查看',vw:'saleChanceFollowView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_chance_follow,operationId: 'sale_chance_follow_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: sale_chance_follow,operationId:'sale_chance_follow_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: sale_chance_follow,operationId:'sale_chance_follow_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()

        def sale_chance_stat = new Module(moduleId:'sale_chance_stat',moduleName:'商机统计',parentModule:sale_chance_mgmt,
                ctrl:'SaleChanceController',vw:'saleChanceStatMain').save()
        new Operation(module: sale_chance_stat,operationId: 'sale_chance_stat_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()

        /**
         * 业绩管理
         */
        def performance_mgmt = new Module(moduleId: 'performance_mgmt',moduleName:'业绩管理').save()
        def sale_aim = new Module(moduleId:'sale_aim',moduleName:'销售目标',parentModule:performance_mgmt,ctrl:'SaleAimController',vw:'saleAimList').save()
        new Operation(module: sale_aim,operationId: 'sale_aim_view',text:'查看',vw:'saleAimView',auto:true,clientType:'all',type:'view',showWin:false,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sale_aim,operationId: 'sale_aim_set',text:'设置',vw:'saleAimSet',auto:false,clientType:'pc',type:'update',showWin:true,optRecords: 'one').save()

        def aim_performance = new Module(moduleId:'aim_performance',moduleName:'目标业绩',parentModule:performance_mgmt,ctrl:'AimPerformanceController',vw:'aimPerformanceMain').save()
        new Operation(module: aim_performance,operationId: 'aim_performance_view',text:'查看',vw:'aimPerformanceView',auto:true,clientType:'all',type:'view',showWin:false,autodisabled:true,optRecords: 'one').save()
        new Operation(module: aim_performance,operationId: 'aim_performance_detail',text:'明细',vw:'aimPerformanceDetail',auto:false,clientType:'pc',type:'view',showWin:true,optRecords: 'one').save()

        /**
         * 对象管理模块
         */
        def object_mgmt = new Module(moduleId: 'object_mgmt',moduleName:'对象管理').save()

        def outsite_record = new Module(moduleId:'outsite_record',moduleName:'现场记录',parentModule:object_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.OutsiteRecord'),ctrl: 'OutsiteRecordController',vw: 'outsiteRecordList').save()
        new Operation(module: outsite_record,operationId: 'outsite_record_view',text:'查看',vw:'outsiteRecordView',auto:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: outsite_record,operationId: 'outsite_record_location',text:'查看位置',auto:true,clientType:'pc',type:'view',showWin:true,autodisabled:true,vw:'onsiteObjectMap').save()

        def onsite_object = new Module(moduleId:'onsite_object',moduleName:'现场对象',parentModule:object_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.OnsiteObject'),ctrl: 'OnsiteObjectController',vw: 'onsiteObjectList').save()
        new Operation(module: onsite_object,operationId: 'onsite_object_add',text:'新增',vw:'onsiteObjectAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: onsite_object,operationId: 'onsite_object_update',text:'修改',vw:'onsiteObjectEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: onsite_object,operationId: 'onsite_object_view',text:'查看',vw:'onsiteObjectView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: onsite_object,operationId: 'onsite_object_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: onsite_object,operationId:'onsite_object_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: onsite_object,operationId:'onsite_object_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: onsite_object,operationId: 'onsite_object_follow',text:'跟进',vw:'objectFollowAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true).save()
        new Operation(module: onsite_object,operationId:'onsite_object_location',text:'定位',type:'update',clientType:'mobile').save()

        def object_follow = new Module(moduleId:'object_follow',moduleName:'对象跟进',parentModule:object_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.ObjectFollow'),ctrl: 'ObjectFollowController',vw: 'objectFollowList').save()
        new Operation(module: object_follow,operationId: 'object_follow_add',text:'新增',vw:'objectFollowAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: object_follow,operationId: 'object_follow_update',text:'修改',vw:'objectFollowEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: object_follow,operationId: 'object_follow_view',text:'查看',vw:'objectFollowView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: object_follow,operationId: 'object_follow_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: object_follow,operationId:'object_follow_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: object_follow,operationId:'object_follow_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: object_follow,operationId:'object_follow_sign',text:'签到',clientType:'mobile').save()
        new Operation(module: object_follow,operationId:'object_follow_signout',text:'签退',clientType:'mobile').save()
        new Operation(module: object_follow,operationId:'object_follow_photo',text:'拍照',clientType:'mobile').save()
        /**
         * 服务派单
         */
        def service_mgmt = new Module(moduleId:'service_mgmt',moduleName:'服务派单').save()
        def service_task = new Module(moduleId:'service_task',moduleName:'服务派单',parentModule:service_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.ServiceTask'),ctrl:'ServiceTaskController',vw:'serviceTaskList').save()
        new Operation(module: service_task,operationId: 'service_task_add',text:'新增',vw:'serviceTaskAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: service_task,operationId: 'service_task_update',text:'修改',vw:'serviceTaskEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: service_task,operationId: 'service_task_view',text:'查看',vw:'serviceTaskView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: service_task,operationId: 'service_task_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: service_task,operationId:'service_task_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: service_task,operationId:'service_task_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: service_task,operationId: 'service_task_signon',text:'签到',clientType:'mobile',type:'add').save()
        new Operation(module: service_task,operationId: 'service_task_signout',text:'签退',clientType:'mobile',type:'add').save()
        new Operation(module: service_task,operationId: 'service_task_photo',text:'拍照',clientType:'mobile',type:'add').save()

        /**
         * 产品管理模块
         */
        def product_mgmt = new Module(moduleId: 'product_mgmt',moduleName:'产品管理').save()

        def product_kind = new Module(moduleId:'product_kind',moduleName:'产品分类',parentModule:product_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.ProductKind'),ctrl:'ProductKindController',vw:'productKindList').save()
        new Operation(module: product_kind,operationId: 'product_kind_add',text:'新增',vw:'productKindAdd',auto:false,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: product_kind,operationId: 'product_kind_update',text:'修改',vw:'productKindEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: product_kind,operationId: 'product_kind_view',text:'查看',vw:'productKindView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: product_kind,operationId: 'product_kind_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: product_kind,operationId:'product_kind_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: product_kind,operationId:'product_kind_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()

        def product = new Module(moduleId:'product',moduleName:'产品管理',parentModule:product_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Product'),ctrl:'ProductController',vw:'productMain').save()
        new Operation(module: product,operationId: 'product_add',text:'新增',vw:'productAdd',auto:false,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: product,operationId: 'product_update',text:'修改',vw:'productEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: product,operationId: 'product_view',text:'查看',vw:'productView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: product,operationId: 'product_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: product,operationId:'product_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: product,operationId:'product_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()

        /**
         * 工作管理
         */
        def work_mgmt = new Module(moduleId:'work_mgmt',moduleName:'工作管理').save()
        def plan_summary = new Module(moduleId:'plan_summary',moduleName:'计划总结',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.PlanSummary'),ctrl:'PlanSummaryController',vw:'planSummaryMain').save()
        new Operation(module: plan_summary,operationId: 'plan_summary_view',text:'查看',clientType:'all',type:'add').save()
        new Operation(module: plan_summary,operationId: 'plan_summary_day_add',text:'新增',vw:'planSummaryDayAdd',clientType:'all',type:'add').save()
        new Operation(module: plan_summary,operationId: 'plan_summary_day_update',text:'修改',vw:'planSummaryDayEdit',clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_day_view',text:'查看',vw:'planSummaryDayView',clientType:'all',type:'view',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_day_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_week_add',text:'新增',vw:'planSummaryWeekAdd',clientType:'all',type:'add').save()
        new Operation(module: plan_summary,operationId: 'plan_summary_week_update',text:'修改',vw:'planSummaryWeekEdit',clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_week_view',text:'查看',vw:'planSummaryWeekView',clientType:'all',type:'view',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_week_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_month_add',text:'新增',vw:'planSummaryMonthAdd',clientType:'all',type:'add').save()
        new Operation(module: plan_summary,operationId: 'plan_summary_month_update',text:'修改',vw:'planSummaryMonthEdit',clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_month_view',text:'查看',vw:'planSummaryMonthView',clientType:'all',type:'view',autodisabled:true).save()
        new Operation(module: plan_summary,operationId: 'plan_summary_month_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        def task_assigned = new Module(moduleId:'task_assigned',moduleName:'任务交办',parentModule:work_mgmt,
				model: Model.findByModelClass('com.uniproud.wcb.TaskAssigned'),ctrl:'TaskAssignedController',vw:'taskAssignedMain').save()
        new Operation(module: task_assigned,operationId: 'task_assigned_add',text:'新增',vw:'taskAssignedAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: task_assigned,operationId: 'task_assigned_update',text:'修改',vw:'taskAssignedEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: task_assigned,operationId: 'task_assigned_view',text:'查看',vw:'taskAssignedView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: task_assigned,operationId: 'task_assigned_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: task_assigned,operationId:'task_assigned_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'taskAssignedImport').save()
        new Operation(module: task_assigned,operationId:'task_assigned_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'taskAssignedExport').save()
        def my_task = new Module(moduleId:'my_task',moduleName:'我的任务',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.TaskAssigned'),ctrl:'MyTaskController',vw:'myTaskMain').save()
        new Operation(module: my_task,operationId: 'my_task_reply',text:'回复',auto:false,clientType:'all',type:'other',autodisabled:true,optRecords: 'no').save()
        new Operation(module: my_task,operationId: 'my_task_report',text:'汇报',vw:'myTaskReport',auto:true,clientType:'all',type:'other',autodisabled:true,optRecords: 'no').save()
        new Operation(module: my_task,operationId: 'my_task_view',text:'查看',vw:'myTaskView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'no').save()

        def bulletin = new Module(moduleId:'bulletin',moduleName:'内部公告',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Bulletin'),ctrl:'BulletinController',vw:'bulletinList').save()
        new Operation(module: bulletin,operationId: 'bulletin_add',text:'新增',vw:'bulletinAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: bulletin,operationId: 'bulletin_update',text:'修改',vw:'bulletinEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: bulletin,operationId: 'bulletin_view',text:'查看',vw:'bulletinView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: bulletin,operationId: 'bulletin_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: bulletin,operationId:'bulletin_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        def work_audit = new Module(moduleId:'work_audit',moduleName:'审批申请',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Audit'),ctrl:'AuditController',vw:'auditMain').save()
        new Operation(module: work_audit,operationId: 'work_audit_audit',text:'审核',clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: work_audit,operationId: 'work_audit_view',text:'审核意见',clientType:'pc',type:'view',autodisabled:true).save()
        new Operation(module: work_audit,operationId: 'work_audit_view_apply',text:'查看申请单',auto:false,clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        def audit_opinion = new Module(moduleId:'audit_opinion',moduleName:'审核意见',parentModule:work_mgmt,model: Model.findByModelClass('com.uniproud.wcb.AuditOpinion')).save()
//        def work_auditCenter = new Module(moduleId:'work_auditCenter',moduleName:'审核中心',parentModule:work_mgmt).save()
//        def work_schedule = new Module(moduleId:'work_schedule',moduleName:'日程安排',parentModule:work_mgmt).save()
        /**
         * 费用报销
         */
        def fare_claims = new Module(moduleId:'fare_claims',moduleName:'费用报销',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.FareClaims'),ctrl:'FareClaimsController',vw:'fareClaimsMain').save()
        new Operation(module: fare_claims,operationId: 'fare_claims_add',text:'新增',vw:'fareClaimsAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: fare_claims,operationId: 'fare_claims_update',text:'修改',vw:'fareClaimsEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: fare_claims,operationId: 'fare_claims_view',text:'查看',vw:'fareClaimsView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: fare_claims,operationId: 'fare_claims_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: fare_claims,operationId:'fare_claims_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: fare_claims,operationId:'fare_claims_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: fare_claims,operationId: 'fare_claims_finance_expense',text:'出账',vw:'financeExpenseAdd',auto:false,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'fareClaimsMain financeExpenseList').save()
        new Operation(module: fare_claims,operationId: 'fare_claims_audit_again',text:'重新送审',clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: fare_claims,operationId: 'fare_claims_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        def fare_claims_detail = new Module(moduleId:'fare_claims_detail',moduleName:'费用报销明细',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.FareClaimsDetail'),ctrl:'FareClaimsController',vw:'fareClaimsDetailList').save()
        /**
         * 我的评阅
         */
        def review = new Module(moduleId:'review',moduleName:'我的评阅',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Review'),ctrl:'ReviewController',vw:'reviewList').save()
        new Operation(module: review,operationId: 'review_view',text:'查看',vw:'reviewView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: review, operationId: 'review_link_view',text:'查看关联对象',vw:'reviewLinkView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled: true,optRecords: 'one').save()
        new Operation(module: review, operationId: 'review_update', text: '修改', vw: 'reviewEdit', auto: true,isCustom:true, clientType: 'all', type: 'update', showWin: true, autodisabled: true,optRecords: 'one').save()
        new Operation(module: review, operationId: 'review_delete', text: '删除', auto: true, clientType: 'all', type: 'del', autodisabled: true,optRecords: 'one').save()
        /**
         * 领导评阅
         */
        def leader_review = new Module(moduleId: 'leader_review', moduleName: '领导评阅', parentModule: work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Review'), ctrl: 'LeaderReviewController', vw: 'leaderReviewList').save()
        new Operation(module: leader_review, operationId: 'leader_review_view', text: '查看', vw: 'leaderReviewView', auto: true, clientType: 'all', type: 'view', showWin: true, autodisabled: true,optRecords: 'one').save()
        new Operation(module: leader_review, operationId: 'leader_review_reply',text:'回复',vw:'leaderReviewReply',auto:false,clientType:'all',type:'add',showWin:true,autodisabled: true,optRecords: 'one').save()
        new Operation(module: leader_review, operationId: 'leader_review_link_view',text:'查看关联对象',vw:'leaderReviewLinkView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled: true,optRecords: 'one').save()
        new Operation(module: leader_review, operationId: 'leader_review_read',text:'置为已读',auto:false,clientType:'all',type:'update',autodisabled:true,optRecords: 'many').save()

        def schedule = new Module(moduleId:'schedule',moduleName:'日程安排',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Schedule'),ctrl:'ScheduleController',vw:'scheduleMain').save()
        new Operation(module: schedule,operationId: 'schedule_add',text:'新增',vw:'scheduleAdd',auto:false,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: schedule,operationId: 'schedule_update',text:'修改',vw:'scheduleEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: schedule,operationId: 'schedule_view',text:'查看',vw:'scheduleView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: schedule,operationId: 'schedule_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()

        def share = new Module(moduleId:'share',moduleName:'分享管理',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Share'),ctrl:'ShareController',vw:'shareMain').save()
        new Operation(module: share,operationId: 'share_add',text:'新增',vw:'shareAdd',auto:false,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: share,operationId: 'share_update',text:'修改',vw:'shareEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: share,operationId: 'share_view',text:'查看',vw:'shareView',auto:false,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: share,operationId: 'share_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: share,operationId: 'share_share',text:'分享',auto:false,clientType:'all',type:'add',autodisabled:true,optRecords: 'one').save()

        //现场拍照
        def site_photo = new Module(moduleId:'site_photo',moduleName:'现场拍照',parentModule:work_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.SitePhoto'),ctrl:'SitePhotoController',vw:'sitePhotoList').save()
        new Operation(module: site_photo,operationId: 'site_photo_add',text:'新增',vw:'sitePhotoAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: site_photo,operationId: 'site_photo_update',text:'修改',vw:'sitePhotoEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: site_photo,operationId: 'site_photo_view',text:'查看',vw:'sitePhotoView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: site_photo,operationId: 'site_photo_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()


		def attendance_mgmt = new Module(moduleId:'apply_mgmt',moduleName:'考勤管理').save()
        def attendance_model = new Module(moduleId:'attendance_model',moduleName:'考勤模板',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.AttendanceModel'),ctrl:'AttendanceModelController',vw:'attendanceModelMain').save()
        new Operation(module: attendance_model,operationId: 'attendance_model_add',text:'新增',vw:'attendanceModelAdd',auto:false,clientType:'mobile',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: attendance_model,operationId: 'attendance_model_update',text:'修改',vw:'attendanceModelEdit',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_model,operationId: 'attendance_model_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: attendance_model,operationId: 'attendance_model_view',text:'查看',vw:'attendanceModelView',auto:false,clientType:'pc',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_model,operationId:'attendance_model_bind_employee',text:'绑定员工',vw:'attendanceModelBindEmployee',auto:true,showWin:true,clientType:'pc',type:'update',autodisabled:true,optRecords: 'one').save()

        def attendance_calendar = new Module(moduleId: 'attendance_calendar', moduleName: '考勤日历', parentModule: attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.AttendanceCalendar'),ctrl: 'AttendanceCalendarController', vw: 'attendanceCalendarMain').save()
        new Operation(module: attendance_calendar,operationId: 'attendance_calendar_add',text:'新增',vw:'attendanceCalendarAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: attendance_calendar,operationId: 'attendance_calendar_update',text:'修改',vw:'attendanceCalendarEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_calendar,operationId: 'attendance_calendar_view',text:'查看',vw:'attendanceCalendarView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_calendar,operationId: 'attendance_calendar_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()

        def work_day = new Module(moduleId:'work_day',moduleName:'考勤工作日',parentModule:attendance_mgmt,
                ctrl:'WorkDayController',vw:'workDayMonth').save()
        new Operation(module: work_day,operationId: 'work_day_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()

        def attendance_data = new Module(moduleId:'attendance_data',moduleName:'考勤数据',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.AttendanceData'),ctrl:'AttendanceDataController',vw:'attendanceDataList').save()
        new Operation(module: attendance_data,operationId: 'attendance_data_view',text:'查看',vw:'attendanceDataView',auto:true,isCustom:true,clientType:'pc',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_data,operationId: 'attendance_data_location',text:'签到签退位置',auto:true,vw:'attendanceDataMap',clientType:'pc',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_data,operationId:'attendance_data_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: attendance_data,operationId:'attendance_data_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: attendance_data,operationId:'attendance_data_sign',text:'签到',clientType:'mobile').save()
        new Operation(module: attendance_data,operationId:'attendance_data_signout',text:'签退',clientType:'mobile').save()

        def leave_apply = new Module(moduleId:'leave_apply',moduleName:'请假申请',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.LeaveApply'),ctrl:'LeaveApplyController',vw:'leaveApplyList').save()
        new Operation(module: leave_apply,operationId: 'leave_apply_add',text:'新增',vw:'leaveApplyAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: leave_apply,operationId: 'leave_apply_update',text:'修改',vw:'leaveApplyEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: leave_apply,operationId: 'leave_apply_view',text:'查看',vw:'leaveApplyView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: leave_apply,operationId: 'leave_apply_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: leave_apply,operationId:'leave_apply_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: leave_apply,operationId:'leave_apply_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: leave_apply,operationId: 'leave_apply_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: leave_apply,operationId: 'leave_apply_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()

        def business_trip_apply = new Module(moduleId:'business_trip_apply',moduleName:'出差申请',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.BusinessTripApply'),ctrl:'BusinessTripApplyController',vw:'businessTripApplyList').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_add',text:'新增',vw:'businessTripApplyAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_update',text:'修改',vw:'businessTripApplyEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_view',text:'查看',vw:'businessTripApplyView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: business_trip_apply,operationId:'business_trip_apply_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: business_trip_apply,operationId:'business_trip_apply_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: business_trip_apply,operationId: 'business_trip_apply_fare_claims',text:'报销',vw:'fareClaimsAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'businessTripApplyMain fareClaimsList',optRecords: 'one').save()

        def overtime_apply = new Module(moduleId:'overtime_apply',moduleName:'加班申请',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.OvertimeApply'),ctrl:'OvertimeApplyController',vw:'overtimeApplyList').save()
        new Operation(module: overtime_apply,operationId: 'overtime_apply_add',text:'新增',vw:'overtimeApplyAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: overtime_apply,operationId: 'overtime_apply_update',text:'修改',vw:'overtimeApplyEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'no').save()
        new Operation(module: overtime_apply,operationId: 'overtime_apply_view',text:'查看',vw:'overtimeApplyView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: overtime_apply,operationId: 'overtime_apply_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: overtime_apply,operationId:'overtime_apply_apply_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: overtime_apply,operationId:'overtime_apply_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: overtime_apply,operationId: 'overtime_apply_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: overtime_apply,operationId: 'overtime_apply_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()

        def goout_apply = new Module(moduleId:'goout_apply',moduleName:'外出申请',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.GoOutApply'),ctrl:'GoOutApplyController',vw:'goOutApplyList').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_add',text:'新增',vw:'goOutApplyAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_update',text:'修改',vw:'goOutApplyEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_view',text:'查看',vw:'goOutApplyView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: goout_apply,operationId:'goout_apply_apply_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport',optRecords: 'no').save()
        new Operation(module: goout_apply,operationId:'goout_apply_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: goout_apply,operationId: 'goout_apply_fare_claims',text:'报销',vw:'fareClaimsAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'goOutApplyMain fareClaimsList',optRecords: 'one').save()

        def attendance_statistics = new Module(moduleId:'attendance_statistics',moduleName:'考勤统计',parentModule:attendance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.AttendanceStatistics'),ctrl:'AttendanceStatisticsController',vw:'attendanceStatisticsList').save()
        new Operation(module: attendance_statistics,operationId: 'attendance_statistics_view',text:'查看',vw:'attendanceStatisticsView',auto:true,isCustom:true,clientType:'pc',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: attendance_statistics,operationId:'attendance_statistics_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()


        /**
         * 订单管理
         */
        def contract_order_mgmt = new Module(moduleId:'contract_order_mgmt',moduleName:'订单管理').save()
        def contract_order = new Module(moduleId:'contract_order',moduleName:'订单管理',parentModule:contract_order_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.ContractOrder'),ctrl:'ContractOrderController',vw:'contractOrderMain').save()
        new Operation(module: contract_order,operationId: 'contract_order_add',text:'新增',vw:'contractOrderAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: contract_order,operationId: 'contract_order_update',text:'修改',vw:'contractOrderEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: contract_order,operationId: 'contract_order_view',text:'查看',vw:'contractOrderView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: contract_order,operationId: 'contract_order_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: contract_order,operationId:'contract_order_transfer',text:'转移',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'add',autodisabled:true,vw:'baseTransfer',optRecords: 'many').save()
        new Operation(module: contract_order,operationId:'contract_order_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: contract_order,operationId:'contract_order_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: contract_order,operationId: 'contract_order_finance_income',text:'收款',vw:'contractOrderIncomeAdd',auto:false,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'contractOrderMain financeIncomeList',optRecords: 'one').save()
        new Operation(module: contract_order,operationId: 'contract_order_invoice',text:'开票',vw:'invoiceAdd',auto:false,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'contractOrderMain invoiceList',optRecords: 'one').save()

        def contract_order_detail = new Module(moduleId:'contract_order_detail',moduleName:'订单明细',parentModule:contract_order_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.ContractOrderDetail'),ctrl:'ContractOrderController',vw:'contractOrderDetailList').save()
        new Operation(module: contract_order_detail,operationId: 'contract_order_detail_view',text:'查看',vw:'contractOrderDetailView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: contract_order_detail,operationId:'contract_order_detail_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()

        def contract_order_income = new Module(moduleId:'contract_order_income',moduleName:'收款记录',parentModule:contract_order_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.FinanceIncomeExpense'),ctrl:'ContractOrderIncomeController',vw:'contractOrderIncomeList').save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_add',text:'新增',vw:'contractOrderIncomeAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_update',text:'修改',vw:'contractOrderIncomeEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_view',text:'查看',vw:'contractOrderIncomeView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: contract_order_income,operationId: 'contract_order_income_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()

        def contract_order_stat = new Module(moduleId:'contract_order_stat',moduleName:'订单统计',parentModule:contract_order_mgmt,
                ctrl:'ContractOrderController',vw:'contractOrderStatMain').save()
        new Operation(module: contract_order_stat,operationId: 'contract_order_stat_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()


        def invoice = new Module(moduleId:'invoice',moduleName:'开票记录',parentModule:contract_order_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Invoice'),ctrl:'InvoiceController',vw:'invoiceList').save()
        new Operation(module: invoice,operationId: 'invoice_add',text:'新增',vw:'invoiceAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: invoice,operationId: 'invoice_update',text:'修改',vw:'invoiceEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: invoice,operationId: 'invoice_view',text:'查看',vw:'invoiceView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: invoice,operationId: 'invoice_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: invoice,operationId: 'invoice_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: invoice,operationId: 'invoice_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        def invoice_detail = new Module(moduleId:'invoice_detail',moduleName:'开票明细',parentModule:contract_order_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.InvoiceDetail'),ctrl:'InvoiceController',vw:'invoiceDetailList').save()
        new Operation(module: invoice_detail,operationId: 'invoice_detail_view',text:'查看',vw:'invoiceDetailView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: invoice_detail,operationId:'invoice_detail_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()

        /**
		 * 市场活动
		 */
		def market_mgmt = new Module(moduleId:'market_mgmt',moduleName:'市场管理').save()
		def market_activity = new Module(moduleId:'market_activity',moduleName:'市场活动',parentModule:market_mgmt,
				model: Model.findByModelClass('com.uniproud.wcb.MarketActivity'),ctrl:'MarketActivityController',vw:'marketActivityList').save()
		new Operation(module: market_activity,operationId: 'market_activity_add',text:'新增',vw:'marketActivityAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
		new Operation(module: market_activity,operationId: 'market_activity_update',text:'修改',vw:'marketActivityEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
		new Operation(module: market_activity,operationId: 'market_activity_view',text:'查看',vw:'marketActivityView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
		new Operation(module: market_activity,operationId: 'market_activity_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: market_activity,operationId:'market_activity_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: market_activity,operationId:'market_activity_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: market_activity,operationId: 'market_activity_QRCode',text:'获取二维码',auto:true,clientType:'all',type:'qrcode',autodisabled:true).save()
        new Operation(module: market_activity,operationId: 'market_activity_fare_claims',text:'报销',vw:'fareClaimsAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true,targetEl: 'marketActivityMain fareClaimsList',optRecords: 'one').save()

        /**
         * 财务管理
         */
        def finance_mgmt = new Module(moduleId:'finance_mgmt',moduleName:'财务管理').save()
        def finance_account = new Module(moduleId:'finance_account',moduleName:'财务账户',parentModule:finance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.FinanceAccount'),ctrl:'FinanceAccountController',vw:'financeAccountList').save()
        new Operation(module: finance_account,operationId: 'finance_account_add',text:'新增',vw:'financeAccountAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: finance_account,operationId: 'finance_account_update',text:'修改',vw:'financeAccountEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_account,operationId: 'finance_account_view',text:'查看',vw:'financeAccountView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: finance_account,operationId: 'finance_account_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: finance_account,operationId: 'finance_account_enabled',text:'启用',auto:false,clientType:'all',type:'update',showWin:false,autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_account,operationId: 'finance_account_disable',text:'禁用',auto:false,clientType:'all',type:'update',showWin:false,autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_account,operationId:'finance_account_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: finance_account,operationId:'finance_account_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        def finance_income = new Module(moduleId:'finance_income',moduleName:'入账管理',parentModule:finance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.FinanceIncomeExpense'),ctrl:'FinanceIncomeController',vw:'financeIncomeList').save()
        new Operation(module: finance_income,operationId: 'finance_income_add',text:'新增',vw:'financeIncomeAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: finance_income,operationId: 'finance_income_update',text:'修改',vw:'financeIncomeEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_income,operationId: 'finance_income_view',text:'查看',vw:'financeIncomeView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: finance_income,operationId: 'finance_income_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: finance_income,operationId: 'finance_income_charge',text:'记账',vw:'financeIncomeCharge',auto:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_income,operationId: 'finance_income_wrong',text:'红字更正',vw:'',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_income,operationId: 'finance_income_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: finance_income,operationId: 'finance_income_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_income,operationId: 'finance_income_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        def finance_expense = new Module(moduleId:'finance_expense',moduleName:'出账管理',parentModule:finance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.FinanceIncomeExpense'),ctrl:'FinanceExpenseController',vw:'financeExpenseList').save()
        new Operation(module: finance_expense,operationId: 'finance_expense_add',text:'新增',vw:'financeExpenseAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: finance_expense,operationId: 'finance_expense_update',text:'修改',vw:'financeExpenseEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_expense,operationId: 'finance_expense_view',text:'查看',vw:'financeExpenseView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: finance_expense,operationId: 'finance_expense_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: finance_expense,operationId: 'finance_expense_charge',text:'记账',vw:'financeExpenseCharge',auto:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_expense,operationId: 'finance_expense_wrong',text:'红字更正',vw:'',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: finance_expense,operationId: 'finance_expense_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: finance_expense,operationId: 'finance_expense_audit_again',text:'重新送审',clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_expense,operationId: 'finance_expense_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        def finance_invoice = new Module(moduleId:'finance_invoice',moduleName:'财务开票',parentModule:finance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Invoice'),ctrl:'FinanceInvoiceController',vw:'financeInvoiceList').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_add',text:'新增',vw:'financeInvoiceAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_update',text:'修改',vw:'financeInvoiceEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_view',text:'查看',vw:'financeInvoiceView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_opinion',text:'审核意见',clientType:'pc',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: finance_invoice,operationId: 'finance_invoice_confirm',text:'开票',vw:'financeInvoiceConfirm',auto:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        def finance_invoice_detail = new Module(moduleId:'finance_invoice_detail',moduleName:'财务开票明细',parentModule:finance_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.InvoiceDetail'),ctrl:'FinanceInvoiceController',vw:'financeInvoiceDetailList').save()
        new Operation(module: finance_invoice_detail,operationId: 'finance_invoice_detail_view',text:'查看',vw:'financeInvoiceDetailView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: finance_invoice_detail,operationId:'finance_invoice_detail_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: finance_income,operationId: 'finance_invoice_wrong',text:'红字更正',vw:'',auto:false,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        /**
		 * 竞争对手
		 */
		def competitor_mgmt = new Module(moduleId:'competitor_mgmt',moduleName:'竞争对手').save()
		def competitor = new Module(moduleId:'competitor',moduleName:'竞争对手',parentModule:competitor_mgmt,
				model: Model.findByModelClass('com.uniproud.wcb.Competitor'),ctrl:'CompetitorController',vw:'competitorList').save()
		new Operation(module: competitor,operationId: 'competitor_add',text:'新增',vw:'competitorAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
		new Operation(module: competitor,operationId: 'competitor_update',text:'修改',vw:'competitorEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
		new Operation(module: competitor,operationId: 'competitor_view',text:'查看',vw:'competitorView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
		new Operation(module: competitor,operationId: 'competitor_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
       new Operation(module: competitor,operationId:'competitor_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: competitor,operationId:'competitor_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()

        def competitor_product = new Module(moduleId:'competitor_product',moduleName:'竞争产品',parentModule:competitor_mgmt,
			model: Model.findByModelClass('com.uniproud.wcb.CompetitorProduct'),ctrl:'CompetitorProductController',vw:'competitorProductList').save()
		new Operation(module: competitor_product,operationId: 'competitor_product_add',text:'新增',vw:'competitorProductAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
		new Operation(module: competitor_product,operationId: 'competitor_product_update',text:'修改',vw:'competitorProductEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
		new Operation(module: competitor_product,operationId: 'competitor_product_view',text:'查看',vw:'competitorProductView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
		new Operation(module: competitor_product,operationId: 'competitor_product_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: competitor_product,operationId:'competitor_product_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: competitor_product,operationId:'competitor_product_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        def competitor_dynamic = new Module(moduleId:'competitor_dynamic',moduleName:'对手动态',parentModule:competitor_mgmt,
			model: Model.findByModelClass('com.uniproud.wcb.CompetitorDynamic'),ctrl:'CompetitorDynamicController',vw:'competitorDynamicList').save()
		new Operation(module: competitor_dynamic,operationId: 'competitor_dynamic_add',text:'新增',vw:'competitorDynamicAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
		new Operation(module: competitor_dynamic,operationId: 'competitor_dynamic_update',text:'修改',vw:'competitorDynamicEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
		new Operation(module: competitor_dynamic,operationId: 'competitor_dynamic_view',text:'查看',vw:'competitorDynamicView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
		new Operation(module: competitor_dynamic,operationId: 'competitor_dynamic_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: competitor_dynamic,operationId:'competitor_dynamic_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: competitor_dynamic,operationId:'competitor_dynamic_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()

        def user_mgmt = new Module(moduleId:'user_mgmt',moduleName:'用户管理').save()
        def employee_locus = new Module(moduleId:'employee_locus',moduleName:'员工分布',parentModule:user_mgmt,
                ctrl:'EmployeeLocusController',vw:'employeeLocusLMap').save()
        new Operation(module: employee_locus,operationId: 'employee_locus_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()

        def locus = new Module(moduleId:'locus',moduleName:'移动轨迹',parentModule:user_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Locus'),ctrl:'LocusController',vw:'locusMain').save()
        new Operation(module: locus,operationId: 'locus_view',text:'查看',vw:'locusView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: locus,operationId: 'locus_location',text:'定位',vw:'locusMap',auto:true,clientType:'pc',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: locus,operationId: 'locus_path',text:'轨迹',vw:'locusPathMap',auto:true,clientType:'pc',type:'view',showWin:true,autodisabled:true).save()

        def base_mgmt = new Module(moduleId:'base_mgmt',moduleName:'基础数据').save()
        def base_employee = new Module(moduleId:'employee',moduleName:'员工管理',parentModule:base_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.Employee'),ctrl: 'EmployeeController',vw:'employeeMain').save()
        new Operation(module: base_employee,operationId: 'employee_add',text:'新增',vw:'employeeAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: base_employee,operationId: 'employee_update',text:'修改',vw:'employeeEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: base_employee,operationId: 'employee_view',text:'查看',vw:'employeeView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: base_employee,operationId: 'employee_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: base_employee,operationId:'employee_import',text:'导入',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseImport').save()
        new Operation(module: base_employee,operationId:'employee_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport').save()
        new Operation(module: base_employee,operationId:'bind_employee_privilege',text:'绑定权限',vw:'bindEmpPrivilege',auto:true,showWin:true,clientType:'pc',type:'update',autodisabled:true).save()
        new Operation(module: base_employee,operationId:'employee_structure',text:'员工组织架构',vw:'employeeStructure',auto:true,showWin:true,clientType:'all',type:'view',autodisabled:false).save()
        new Operation(module: base_employee,operationId: 'employee_password_init', text: '密码初始化', auto: true, clientType: 'all', type: 'del', autodisabled: true,optRecords: 'many').save()
        new Operation(module: base_employee,operationId: 'employee_limit', text: '限定', auto: false, clientType: 'pc', type: 'update', autodisabled: true,optRecords: 'one').save()


		def base_dept = new Module(moduleId:'dept',moduleName:'部门管理',parentModule:base_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.Dept'),ctrl: 'DeptController',vw:'deptMain').save()
        new Operation(module: base_dept,operationId: 'dept_add',text:'新增',vw:'deptAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: base_dept,operationId: 'dept_update',text:'修改',vw:'deptEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: base_dept,operationId: 'dept_view',text:'查看',vw:'deptView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: base_dept,operationId: 'dept_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()

        def user_privilege = new Module(moduleId:'privilege',moduleName:'用户权限',parentModule:base_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.Privilege'),ctrl: 'PrivilegeController',vw:'privilegeMain').save()
        new Operation(module: user_privilege,operationId: 'privilege_add',text:'新增',vw:'privilegeAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: user_privilege,operationId: 'privilege_update',text:'修改',vw:'privilegeEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: user_privilege,operationId: 'privilege_view',text:'查看',vw:'privilegeView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: user_privilege,operationId: 'privilege_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()


        def base_dict = new Module(moduleId:'base_dict',moduleName:'数据字典',parentModule:base_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.DataDict'),ctrl: 'DataDictController',vw:'dictMain').save()
        new Operation(module: base_dict,operationId: 'base_dict_view',text:'查看',vw:'dictView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()

        def base_dict_item = new Module(moduleId:'dict_item',moduleName:'数据字典明细',parentModule:base_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.DataDictItem')).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_add',text:'新增',vw:'dictItemAdd',auto:true,clientType:'all',type:'add',showWin:true).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_update',text:'修改',vw:'dictItemEdit',auto:true,clientType:'all',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_view',text:'查看',vw:'dictItemView',auto:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_up',text:'上移',auto:true,clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_down',text:'下移',auto:true,clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: base_dict_item,operationId: 'dict_item_save',text:'保存',auto:true,clientType:'all',type:'update',autodisabled:true).save()

        def employee_modify_pwd = new Module(moduleId:'employee_modify_pwd',moduleName:'更改密码',parentModule:base_mgmt,
                ctrl:'EmployeeController',vw:'employeeModifyPwd').save()
        new Operation(module: employee_modify_pwd,operationId: 'employee_modify_pwd_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()
        def employee_check  = new Module(moduleId:'employee_check',moduleName:'企业信息',parentModule:base_mgmt,
                ctrl:'EmployeeCheckController',vw:'employeeCheckMain').save()
        new Operation(module: employee_check,operationId: 'employee_check_view',text:'查看',vw:'',auto:false,clientType:'pc',type:'view',showWin:false,autodisabled:true).save()
        /**
         * 系统管理模块
         */
        def system_mgmt = new Module(moduleId: 'system_mgmt',moduleName:'系统管理').save()
        def login_log = new Module(moduleId:'login_log',moduleName:'登录日志',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.LoginLog'),ctrl: 'LoginLogController',vw:'loginLogList').save()
        new Operation(module: login_log,operationId:'login_log_view',text:'查看',auto:true,isCustom:true,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'loginLogView',optRecords: 'one').save()
        new Operation(module: login_log,operationId:'login_log_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()

        def add_balance = new Module(moduleId:'add_balance',moduleName:'充值记录',parentModule:system_mgmt,ctrl: 'AddBalanceController',vw:'addBalanceList').save()
        new Operation(module: add_balance,operationId:'add_balance_view',text:'查看',auto:true,isCustom:true,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'addBalanceView',optRecords: 'one').save()

        def notify_model = new Module(moduleId:'notify_model',moduleName:'消息模型',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.NotifyModel'),ctrl: 'NotifyModelController',vw:'notifyModelMain').save()
        new Operation(module: notify_model,operationId: 'notify_model_add',text:'新增',vw:'notifyModelAdd',auto:true,isCustom:true,clientType:'pc',type:'add',showWin:true).save()
        new Operation(module: notify_model,operationId: 'notify_model_update',text:'修改',vw:'notifyModelEdit',auto:true,isCustom:true,clientType:'pc',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: notify_model,operationId: 'notify_model_view',text:'查看',vw:'notifyModelView',auto:true,isCustom:true,clientType:'pc',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: notify_model,operationId: 'notify_model_delete',text:'删除',auto:true,clientType:'pc',type:'del',autodisabled:true).save()
        new Operation(module: notify_model,operationId: 'notify_model_filter',text:'设置',vw:'notifyModelFilterMain',false:true,clientType:'pc',type:'update',showWin:true,autodisabled:true).save()
        def employee_notify_model = new Module(moduleId: 'employee_notify_model',moduleName:'消息设置',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.EmployeeNotifyModel'),ctrl: 'EmployeeNotifyModelController',vw:'employeeNotifyModelMain').save()
        new Operation(module: employee_notify_model,operationId: 'employee_notify_model_view',text:'查看',vw:'employeeNotifyModelView',auto:true,isCustom:true,clientType:'pc',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: employee_notify_model,operationId: 'employee_notify_model_update',text:'修改',vw:'employeeNotifyModelEdit',auto:true,isCustom:true,clientType:'pc',type:'update',showWin:true,autodisabled:true).save()
        new Operation(module: employee_notify_model,operationId: 'employee_notify_model_default',text:'恢复默认',auto:false,clientType:'pc',type:'del',autodisabled:true).save()
        //提醒消息
        def notify = new Module(moduleId: 'notify',moduleName:'提醒消息',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Notify'),ctrl: 'NotifyController',vw:'notifyList').save()
        new Operation(module: notify,operationId: 'notify_view',text:'查看',vw:'notifyView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true).save()
        new Operation(module: notify,operationId: 'notify_read',text:'置为已读',auto:false,clientType:'all',type:'update',autodisabled:true).save()
        new Operation(module: notify,operationId: 'notify_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true).save()
        //扣费记录
        def payment_record = new Module(moduleId: 'payment_record',moduleName:'扣费记录',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.PaymentRecord'),ctrl: 'PaymentRecordController',vw:'paymentRecordList').save()
        new Operation(module: payment_record,operationId: 'payment_record_view',text:'查看',vw:'paymentRecordView',auto:true,isCustom:true,clientType:'pc',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: payment_record,operationId:'payment_record_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()
        //Sfa
        def sfa = new Module(moduleId: 'sfa',moduleName:'SFA方案',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.Sfa'),ctrl: 'SfaController',vw:'sfaList').save()
        new Operation(module: sfa,operationId: 'sfa_add',text:'新增',vw:'sfaAdd',auto:true,isCustom:true,clientType:'pc',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: sfa,operationId: 'sfa_update',text:'修改',vw:'sfaEdit',auto:true,isCustom:true,clientType:'pc',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sfa,operationId: 'sfa_view',text:'查看',vw:'sfaView',auto:true,isCustom:true,clientType:'pc',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sfa,operationId: 'sfa_delete',text:'删除',auto:true,clientType:'pc',type:'del',autodisabled:true,optRecords: 'one').save()
        new Operation(module: sfa,operationId: 'sfa_set',text:'设置',vw:'sfaEventList',false:true,clientType:'pc',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: sfa,operationId: 'sfa_enabled',text:'启用',auto:false,clientType:'pc',type:'update',showWin:false,autodisabled:true,optRecords: 'many').save()
        new Operation(module: sfa,operationId: 'sfa_disable',text:'停用',auto:false,clientType:'pc',type:'update',showWin:false,autodisabled:true,optRecords: 'many').save()

        def close_center = new Module(moduleId:'close_center',moduleName:'关闭中心',parentModule:system_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.CloseCenter'),ctrl: 'CloseCenterController',vw:'closeCenterList').save()
        new Operation(module: close_center,operationId:'close_center_view',text:'查看',auto:true,isCustom:true,showWin:true,iconCls:'table_view',clientType:'all',type:'view',autodisabled:true,vw:'closeCenterView',optRecords: 'one').save()
        /**
         * 安装管理
         */
        def install_mgmt = new Module(moduleId: 'install_mgmt',moduleName:'安装管理').save()
        def install_order = new Module(moduleId: 'install_order',moduleName:'安装订单',parentModule:install_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.InstallOrder'),ctrl: 'InstallOrderController',vw:'installOrderMain').save()
        new Operation(module: install_order,operationId: 'install_order_add',text:'新增',vw:'installOrderAdd',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,optRecords: 'no').save()
        new Operation(module: install_order,operationId: 'install_order_update',text:'修改',vw:'installOrderEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: install_order,operationId: 'install_order_view',text:'查看',vw:'installOrderView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: install_order,operationId: 'install_order_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'one').save()
        new Operation(module: install_order,operationId: 'install_order_allocate',text:'派单',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'view',autodisabled:true,vw:'installOrderAllocate',optRecords: 'one').save()
        new Operation(module: install_order,operationId: 'install_order_transfer',text:'转单',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'view',autodisabled:true,vw:'installOrderTransfer',optRecords: 'one').save()

        def install_order_detail = new Module(moduleId:'install_order_detail',moduleName:'安装单明细',parentModule:install_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.InstallOrderDetail'),ctrl:'InstallOrderController',vw:'installOrderDetailList').save()
        new Operation(module: install_order_detail,operationId: 'install_order_detail_view',text:'查看',vw:'installOrderDetailView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: install_order_detail,operationId:'install_order_detail_export',text:'导出',auto:true,showWin:true,clientType:'all',type:'view',vw:'baseExport',optRecords: 'no').save()

        def install_order_pending = new Module(moduleId: 'install_order_pending',moduleName:'待处理',parentModule:install_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.InstallOrder'),ctrl: 'InstallOrderPendingController',vw:'installOrderPendingList').save()
        new Operation(module: install_order_pending,operationId: 'install_order_pending_update',text:'修改',vw:'installOrderPendingEdit',auto:true,isCustom:true,clientType:'all',type:'update',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: install_order_pending,operationId: 'install_order_pending_view',text:'查看',vw:'installOrderPendingView',auto:true,isCustom:true,clientType:'all',type:'view',showWin:true,autodisabled:true,optRecords: 'one').save()
        new Operation(module: install_order_pending,operationId: 'install_order_pending_allocate',text:'派单',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'view',autodisabled:true,vw:'installOrderPendingAllocate',optRecords: 'one').save()
        new Operation(module: install_order_pending,operationId: 'install_order_pending_transfer',text:'转单',auto:true,showWin:true,iconCls:'table_save',clientType:'all',type:'view',autodisabled:true,vw:'installOrderPendingTransfer',optRecords: 'one').save()
        new Operation(module: install_order_pending,operationId: 'install_order_pending_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'one').save()

        def install_allocate_detail = new Module(moduleId:'install_allocate_detail',moduleName:'预派单',parentModule:install_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.InstallAllocateDetail'),ctrl: 'InstallAllocateDetailController',vw:'installAllocateDetailList').save()
        new Operation(module: install_allocate_detail, operationId: 'install_allocate_detail_view', text: '查看', vw: 'installAllocateDetailView', auto: true, isCustom: true, clientType: 'all', type: 'view', showWin: true, autodisabled: true, optRecords: 'one').save()
        new Operation(module: install_allocate_detail, operationId: 'install_allocate_detail_grab', text: '抢单', auto: false, clientType: 'all', type: 'update', autodisabled: true, optRecords: 'one').save()
        new Operation(module: install_allocate_detail, operationId: 'install_allocate_detail_confirm', text: '定单', auto: false, clientType: 'all', type: 'update', autodisabled: true, optRecords: 'one').save()

        /**
         * 客服管理模块
         */
        def customer_service_mgmt = new Module(moduleId: 'customer_service_mgmt',moduleName:'客服管理').save()
        def customer_revisit = new Module(moduleId: 'customer_revisit', moduleName: '客户回访', parentModule: customer_service_mgmt,
                model: Model.findByModelClass('com.uniproud.wcb.CustomerRevisit'), ctrl:'CustomerRevisitController', vw: 'customerRevisitMain').save()
        new Operation(module: customer_revisit,operationId:'customer_revisit_add',text:'新增',vw:'customerRevisitAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
        new Operation(module: customer_revisit,operationId:'customer_revisit_update',text:'修改',vw:'customerRevisitEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_revisit,operationId:'customer_revisit_view',text:'查看',vw:'customerRevisitView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_revisit,operationId:'customer_revisit_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()

        def customer_care = new Module(moduleId: 'customer_care', moduleName: '客户关怀', parentModule: customer_service_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.CustomerCare'), ctrl:'CustomerCareController', vw: 'customerCareMain').save()
        new Operation(module: customer_care,operationId:'customer_care_add',text:'新增',vw:'customerCareAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
        new Operation(module: customer_care,operationId:'customer_care_update',text:'修改',vw:'customerCareEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_care,operationId:'customer_care_view',text:'查看',vw:'customerCareView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_care,operationId:'customer_care_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()

        def customer_complaints = new Module(moduleId: 'customer_complaints', moduleName: '客户投诉', parentModule: customer_service_mgmt,
                model:Model.findByModelClass('com.uniproud.wcb.CustomerComplaints'), ctrl:'CustomerComplaintsController', vw: 'customerComplaintsMain').save()
        new Operation(module: customer_complaints,operationId:'customer_complaints_add',text:'新增',vw:'customerComplaintsAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
        new Operation(module: customer_complaints,operationId:'customer_complaints_update',text:'修改',vw:'customerComplaintsEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_complaints,operationId:'customer_complaints_view',text:'查看',vw:'customerComplaintsView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
        new Operation(module: customer_complaints,operationId:'customer_complaints_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()

    }
/*
    def market_activity = Module.findByModuleId('market_activity')
    new Operation(module: market_activity,operationId: 'market_activity_invit',text:'邀客',vw:'marketActivityInvit',auto:true,isCustom:true,clientType:'all',type:'add',showWin:true,autodisabled:true).save()

    def market_activity_customer = new Module(moduleId:'market_activity_customer',moduleName: '市场活动客户',parentModule: Module.findByModuleId('market_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.MarketActivityCustomer'),ctrl:'MarketActivityCustomerController', vw: 'marketActivityCustomerList').save()
    new Operation(module: market_activity_customer,operationId:'market_activity_customer_add',text:'新增',vw:'marketActivityCustomerAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
    new Operation(module: market_activity_customer,operationId:'market_activity_customer_update',text:'修改',vw:'marketActivityCustomerEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
    new Operation(module: market_activity_customer,operationId:'market_activity_customer_view',text:'查看',vw:'marketActivityCustomerView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
    new Operation(module: market_activity_customer,operationId:'market_activity_customer_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()

    微信管理模块
    def weixin_mgmt = new Module(moduleId: 'weixin_mgmt',moduleName:'微信管理').save()
    def weixin = new Module(moduleId:'weixin',moduleName: '微信公众号',parentModule: weixin_mgmt,model:Model.findByModelClass('com.uniproud.wcb.Weixin'),ctrl:'WeixinController', vw: 'weixinMain').save()
    new Operation(module: weixin,operationId:'weixin_add',text:'新增',vw:'weixinAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
    new Operation(module: weixin,operationId:'weixin_update',text:'修改',vw:'weixinEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
    new Operation(module: weixin,operationId:'weixin_view',text:'查看',vw:'weixinView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
    new Operation(module: weixin,operationId:'weixin_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
    def weixin_customer = new Module(moduleId:'weixin_customer',moduleName: '微信关注者',parentModule: weixin_mgmt,model:Model.findByModelClass('com.uniproud.wcb.WeixinCustomer'),ctrl:'WeixinCustomerController', vw: 'weixinCustomerList').save()
    new Operation(module: weixin_customer,operationId:'weixin_customer_add',text:'新增',vw:'weixinCustomerAdd',auto:true,isCustom:true,showWin:true,clientType:'all',type:'add',optRecords: 'no').save()
    new Operation(module: weixin_customer,operationId:'weixin_customer_update',text:'修改',vw:'weixinCustomerEdit',auto:true,isCustom:true,showWin:true,clientType:'all',type:'update',autodisabled:true,optRecords: 'one').save()
    new Operation(module: weixin_customer,operationId:'weixin_customer_view',text:'查看',vw:'weixinCustomerView',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
    new Operation(module: weixin_customer,operationId:'weixin_customer_delete',text:'删除',auto:true,clientType:'all',type:'del',autodisabled:true,optRecords: 'many').save()
    def weixin_menu = new Module(moduleId:'weixin_menu',moduleName: '微信菜单',parentModule: weixin_mgmt,model:Model.findByModelClass('com.uniproud.wcb.WeixinMenu'),ctrl:'WeixinMenuController', vw: 'weixinMenuMain').save()
    new Operation(module: weixin_menu,operationId:'weixin_menu_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
    def weixin_send_msg = new Module(moduleId:'weixin_send_msg',moduleName: '群发消息',parentModule: weixin_mgmt,model:Model.findByModelClass('com.uniproud.wcb.WeixinSendMsg'),ctrl:'WeixinSendMsgController', vw: 'weixinSendMsgMain').save()
    new Operation(module: weixin_send_msg,operationId:'weixin_send_msg_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
    def material = new Module(moduleId:'material',moduleName: '素材管理',parentModule: weixin_mgmt,model:Model.findByModelClass('com.uniproud.wcb.Material'),ctrl:'MaterialController', vw: 'materialMain').save()
    new Operation(module: material,operationId:'material_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
    def send_msg_weixin_customer = new Module(moduleId:'send_msg_weixin_customer',moduleName: '群发接收者列表',parentModule: Module.findByModuleId('weixin_mgmt'),model:Model.findByModelClass('com.uniproud.wcb.SendMsgWeixinCustomer'),ctrl:'SendMsgWeixinCustomerController', vw: 'SendMsgWeixinCustomerMain',isMenu:false).save()
    new Operation(module: send_msg_weixin_customer,operationId:'send_msg_weixin_customer_view',text:'查看',auto:true,isCustom:true,showWin:true,clientType:'all',type:'view',autodisabled:true,optRecords: 'one').save()
 */

    def createModulesFiles(user){
        def modules = Module.findAllByModelIsNotNull()
        modules.each {module->
            def F = [:]
            UserField.findAllByUserAndModule(user,module,[sort: "fieldName", order: "asc"])?.each {uf->
                if((!uf.relation || uf.fieldName == 'files') && uf.fieldName!='id' && !uf.fieldName.endsWith('.id')){
                    def prop = [:]
                    prop << ["text":uf.text]
                    if(uf.must)
                        prop << ["must":uf.must]
                    if(uf.bitian)
                        prop << ["bitian":uf.bitian]
                    if(uf.weiyi)
                        prop << ["weiyi":uf.weiyi]
                    if(uf.mohu)
                        prop << ["mohu":uf.mohu]
                    if(uf.heji)
                        prop << ["heji":uf.heji]
                    if(uf.defValue)
                        prop << ["defValue":uf.defValue]
                    if(uf.dict)
                        prop << ["dict":uf.dict.dataId]
                    if(uf.pageType && uf.pageType!=userService.getPageType(uf.dict,uf.dbType,uf.fieldName,Field.findByModelAndFieldName(module.model,uf.fieldName))){
                        prop << ["pageType":uf.pageType]
                    }
                    if(uf.max)
                        prop << ["max":uf.max]
                    if(uf.min)
                        prop << ["min":uf.min]
                    if(uf.scale)
                        prop << ["scale":uf.scale]
                    if(uf.maxSize)
                        prop << ["maxSize":uf.maxSize]
                    if(uf.note)
                        prop << ["not":uf.note]
                    def ve = [:]
                    ViewDetail.findAllByUserAndUserField(user,uf)?.each {vd->
                        def view = vd.view
                        if(view.module == module){
                            def vedProp = []
                            ViewDetailExtend.findAllByViewDetail(vd).each {vde->
                                def vdeJson = [:]
                                vdeJson << ["paramName":vde.paramName,"paramValue":vde.paramValue]
                                if(!vde.isBelongToEditor){
                                    vdeJson << ["isBelongToEditor":vde.isBelongToEditor]
                                }
                                if(vde.paramType){
                                    vdeJson << ["paramType":vde.paramType]
                                }
                                if(vde.paramDesc){
                                    vdeJson << ["paramDesc":vde.paramDesc]
                                }
                                vedProp << vdeJson
                            }
                            if(vedProp){
                                if(ve."${view.clientType}"){
                                    if(!ve."${view.clientType}"."${view.viewType}"){
                                        ve."${view.clientType}" << ["${view.viewType}": vedProp]
                                    }
                                }else{
                                    ve << ["${view.clientType}":["${view.viewType}": vedProp]]
                                }
                            }
                        }
                    }
                    if(ve)
                        prop << ["VE":ve]
                    F << ["${uf.fieldName}":prop]
                }
            }
            def V = [:]
            View.findAllByUserAndModule(user,module,[sort: "viewId", order: "asc"]).each {view->
                def vp  = [:]
                vp << ["title": view.title,"clientType":view.clientType,"viewType":view.viewType]
                if(view.editable || view.viewId?.endsWith('Add') || view.viewId?.endsWith('Edit'))
                    vp << ["editable":true]
                if(view.forceFit)
                    vp << ["forceFit":true]
                if(view.columns != 2)
                    vp << ["columns":view.columns]
                if(view.isSearchView)
                    vp << ["isSearchView":true]
                if(view.remark)
                    vp << ["remark":view.remark]
                def viewDetail = [:]     //视图明细
                ViewDetail.findAllByView(view,[sort: "orderIndex", order: "asc"])?.each {detail->
                    if(detail.userField?.fieldName){
                        def viewField = [:]
                        viewField << ["orderIndex":detail.orderIndex,"pageType":detail.pageType]
                        if(detail.width != 100){
                            viewField << ["width":detail.width]
                        }
                        if(detail.defValue){
                            viewField << ["defValue":detail.defValue]
                        }
                        if(detail.locked){
                            viewField << ["locked":detail.locked]
                        }
//                        if(detail.sortable){
//                            viewField << ["sortable":detail.sortable]
//                            if(detail.sortName){
//                                viewField << ["sortName":detail.sortName]
//                            }
//                        }
                        if(detail.openFormula){
                            viewField << ["openFormula":detail.openFormula]
                        }
                        if(detail.remark){
                            viewField << ["remark":detail.remark]
                        }
                        if(detail.listView){
                            viewField << ["listView":["viewId":detail.listView?.viewId,"moduleId":detail.listView?.module?.moduleId]]
                        }
                        def vedProp = []
                        ViewDetailExtend.findAllByViewDetail(detail).each {vde->
                            def vdeJson = [:]
                            vdeJson << ["paramName":vde.paramName,"paramValue":vde.paramValue]
                            if(!vde.isBelongToEditor){
                                vdeJson << ["isBelongToEditor":vde.isBelongToEditor]
                            }
                            if(vde.paramType){
                                vdeJson << ["paramType":vde.paramType]
                            }
                            if(vde.paramDesc){
                                vdeJson << ["paramDesc":vde.paramDesc]
                            }
                            vedProp << vdeJson
                        }
                        if(vedProp){
                            viewField << ["ved":vedProp]
                        }
                        viewDetail << ["${detail.userField?.fieldName}":viewField]
                    }
                }
                def ext = [:]
                ViewExtend.findAllByView(view)?.each {viewEtend->
                    ext << ["${viewEtend.paramName}":viewEtend.paramValue]
                }
                def opt = []
                ViewOperation.findAllByView(view,[sort: "orderIndex"]).each{vt->
                    opt << vt.userOperation?.operation?.operationId
                }
                def viewProp = ["view":vp,"viewDetail":viewDetail]
                if(ext)
                    viewProp << ["ext":ext]
                if(opt)
                    viewProp << ["opt":opt]
                V << ["${view.viewId}":viewProp]
            }
            def json = ["F":F,"V":V] as JSON
            def ant = new AntBuilder()
            ant.mkdir(dir:"${grailsApplication.config.MODULE_FILE_PATH}")
            def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${module.moduleId}.json")
            if(file.exists()){
                file.delete()
            }
            file.withPrintWriter {printWriter ->
                printWriter.print(JsonUtil.format(json.toString()))
            }
        }
    }


}
