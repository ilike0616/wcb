package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.successTrue

@UserAuthAnnotation
class ScheduleController {

    def modelService
	def baseService

    def list(){
        def paramStartDate
        def paramEndDate
        if(params.start && params.end) {
            paramStartDate = DateUtil.toDate(params.start,"MM-dd-yyyy")
            paramEndDate = DateUtil.toDate(params.end,"MM-dd-yyyy")
        }
        def extraCondition = {
            if(paramStartDate && paramEndDate){
                or{
                    between('planStartDate',paramStartDate,paramEndDate)
                    between('planEndDate',paramStartDate,paramEndDate)
                }
            }
            if(params.owner){
                eq("owner.id",params.owner?.toLong())
            }
            if(params.taskState){ // 手机端
                def taskState = params.taskState.toInteger()
                if(taskState == 1 || taskState == 3) { // 未完成
                    and{
                        eq("taskState",1)
                        if(taskState == 1){ // 未完成，结束时间在当前时间之后
                            ge("planEndDate",new Date())
                        }else{ // 逾期，结束时间在当前时间之前
                            lt("planEndDate",new Date())
                        }
                    }
                }else { // 已完成
                    eq("taskState",2)
                }
            }
        }
        def isPaging = false
        if(params.isPaging && params.isPaging.toBoolean() == true){
            isPaging = true
        }
        def result = modelService.getData('schedule',extraCondition,isPaging,true)
        def data = []
        result.data.each {
            // itmap里面直接get得不到，只能先这样
            def planStartDate,planEndDate,relatedType,dateCreated
            it.each{key,value->
                if(key == "planStartDate"){
                    planStartDate = value
                }else if(key == "planEndDate"){
                    planEndDate = value
                }else if(key == 'relatedType'){
                    relatedType = value
                }else if(key == 'dateCreated'){
                    dateCreated = value
                }
            }
            if(!planStartDate && !planEndDate) {
                planStartDate = dateCreated
                planEndDate = dateCreated
            }
            if(planStartDate && !planEndDate) planEndDate = planStartDate
            if(!planStartDate && planEndDate) planStartDate = planEndDate
            def ad = false
            ad = judgeIsAllDay(planStartDate,planEndDate)
            it << [ad:ad,cid:relatedType==null?1:relatedType,planStartDate:planStartDate,planEndDate:planEndDate]
        }

        println(result)
        def json = [success: true, total:result.total,data: result.data] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    def summaryList(){
        def paramStartDate
        def paramEndDate
        if(params.start && params.end) {
            paramStartDate = DateUtil.toDate(params.start,"MM-dd-yyyy")
            paramEndDate = DateUtil.toDate(params.end,"MM-dd-yyyy")
        }
        def data = []

        // 客户跟进
        CustomerFollow.createCriteria().list(){
            if(paramStartDate && paramEndDate){
                or{
                    between('startDate',paramStartDate,paramEndDate)
                    between('endDate',paramStartDate,paramEndDate)
                }
            }
            if(params.owner){
                eq("employee.id",params.owner?.toLong())
            }
            eq('deleteFlag',false)
        }.each {cf->
            def ad = false
            def planStartDate = cf.startDate,planEndDate = cf.endDate
            if(!planStartDate && !planEndDate) {
                planStartDate = cf.dateCreated
                planEndDate = cf.dateCreated
            }
            if(planStartDate && !planEndDate) planEndDate = planStartDate
            if(!planStartDate && planEndDate) planStartDate = planEndDate
            ad = judgeIsAllDay(planStartDate,planEndDate)
            def relatedType = 1
            def schedule = [ad:ad,cid:relatedType,id:cf.id,subject:cf.subject,planStartDate:planStartDate,planEndDate:planEndDate,
                            employee:[id:cf.employee?.id,name:cf.employee?.name],owner:[id:cf.employee?.id,name:cf.employee?.name],
                            customer:[id:cf.customer?.id,name:cf.customer?.name],taskState:2,taskDesc:cf.followContent,
                            relatedType:relatedType,taskSummary:cf.followContent,deleteFlag:cf.deleteFlag,dateCreated:cf.dateCreated,
                            lastUpdated:cf.lastUpdated,files:[id:cf.files?.id,name: cf.files.name],files1:[id:cf.files1.id,name:cf.files1.name],
            ]
            (1..47).each {
                def extend = "extend${it}"
                schedule << ["$extend":cf."$extend"]
            }
            data << schedule
        }

        // 商机跟进
        SaleChanceFollow.createCriteria().list(){
            if(paramStartDate && paramEndDate){
                or{
                    between('followDate',paramStartDate,paramEndDate)
                    between('endDate',paramStartDate,paramEndDate)
                }
            }
            if(params.owner){
                eq("employee.id",params.owner?.toLong())
            }
            eq('deleteFlag',false)
        }.each {scf->
            def ad = false
            def planStartDate = scf.followDate,planEndDate = scf.endDate
            if(!planStartDate && !planEndDate) {
                planStartDate = scf.dateCreated
                planEndDate = scf.dateCreated
            }
            if(planStartDate && !planEndDate) planEndDate = planStartDate
            if(!planStartDate && planEndDate) planStartDate = planEndDate
            ad = judgeIsAllDay(planStartDate,planEndDate)
            def relatedType = 2
            def schedule = [ad:ad,cid:relatedType,id:scf.id,subject:scf.subject,planStartDate:planStartDate,planEndDate:planEndDate,
                            employee:[id:scf.employee?.id,name:scf.employee?.name],owner:[id:scf.employee?.id,name:scf.employee?.name],
                            customer:[id:scf.customer?.id,name:scf.customer?.name],taskState:2,taskDesc:scf.followContent,
                            relatedType:relatedType,taskSummary:scf.followContent,deleteFlag:scf.deleteFlag,dateCreated:scf.dateCreated,
                            lastUpdated:scf.lastUpdated,files:[id:scf.files?.id,name: scf.files.name],files1:[id:scf.files1.id,name:scf.files1.name,
                            saleChance:[id:scf.saleChance?.id,name:scf.saleChance?.subject]],
            ]
            (1..47).each {
                def extend = "extend${it}"
                schedule << ["$extend":scf."$extend"]
            }
            data << schedule
        }

        def json = [success: true, data: data] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    /**
     * 根据时间判断是否全天
     * @param startDate
     * @param endDate
     * @return
     */
    def judgeIsAllDay(startDate,endDate){
        def allDay = false
        def startDateStr = DateUtil.format(startDate,"yyyy-MM-dd")
        def startDateNoDate = DateUtil.format(startDate,"HH:mm")
        def endDateStr = DateUtil.format(endDate,"yyyy-MM-dd")
        def endDateNoDate = DateUtil.format(endDate,"HH:mm")
        if(startDateStr != endDateStr){
            allDay = true
        }else if(startDateNoDate == "00:00" && endDateNoDate == "00:00"){
            allDay = true
        }
        return allDay
    }

	@Transactional
	def insert(Schedule schedule) {
		if (schedule == null) {
			render baseService.error(params)
			return
		}
        schedule.properties["user"] = session.employee?.user
        schedule.properties["employee"] = session.employee

		render baseService.save(params,schedule)
	}

    @Transactional
    def insertFollow() {
        if (!params.moduleId  || !params.scheduleId) {
            render baseService.error(params)
            return
        }

        def module = Module.findByModuleId(params.moduleId)
        def modelClass = module.model?.modelClass
        def domain = grailsApplication.getClassForName(modelClass)
        def domainObj = domain.newInstance(params)
        domainObj.properties["user"] = session.employee?.user
        domainObj.properties["employee"] = session.employee
        baseService.save(params,domainObj)

        def schedule = Schedule.get(params.scheduleId.toLong())
        schedule.taskState = 2
        schedule."$params.moduleScheduleName" = domainObj
        schedule.save(flush: true)
        render successTrue
    }
	
    @Transactional
    def update(Schedule schedule) {
		render baseService.save(params,schedule)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Schedule.executeUpdate("update Schedule set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

}
