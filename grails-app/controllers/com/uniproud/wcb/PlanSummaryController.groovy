package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

@UserAuthAnnotation
@Transactional(readOnly = true)
class PlanSummaryController {
	
	def modelService
	def baseService
	
    def list(){
        def extraCondition = {
//            if(params.searchValue){
//                ilike("subject","%$params.searchValue%")
//            }
            if(params.planSummaryType){
                eq("type",params.planSummaryType.toInteger())
            }
        }
		render modelService.getDataJSON('plan_summary',extraCondition)
    }

    @Transactional
    def insert(PlanSummary planSummary) {
		if (planSummary == null) {
			render baseService.error(params)
			return
		}
        def subject = planSummary.subject
        Calendar cal = Calendar.getInstance()
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        if(planSummary.type == 1){  // 日报
            if(!planSummary.startDate || !planSummary.endDate){
                planSummary.startDate = cal.getTime()
                planSummary.endDate = cal.getTime()
            }
            if(!planSummary.subject){
                subject = session.employee?.name+" "+SysTool.getPromptTime(planSummary.startDate)
            }
        }else if(planSummary.type == 2){    // 周报
            def dateMap = SysTool.getDayOfWeek('THIS_WEEK')
            if(!planSummary.startDate || !planSummary.endDate){
                planSummary.startDate = dateMap.get("startDate")
                planSummary.endDate = dateMap.get("endDate")
            }
            if(!planSummary.subject){
                subject = session.employee?.name+" " + sdf.format(planSummary.startDate) + "~" + sdf.format(planSummary.endDate)
            }
        }else if(planSummary.type == 3){    // 月报
            def dateMap = SysTool.getDayOfMonth('THIS_WEEK')
            if(!planSummary.startDate || !planSummary.endDate){
                planSummary.startDate = dateMap.get("startDate")
                planSummary.endDate = dateMap.get("endDate")
            }
            if(!planSummary.subject){
                subject = session.employee?.name+" " + sdf.format(planSummary.startDate).substring(0,7)
            }
        }else{  // 其他
            if(!planSummary.startDate || !planSummary.endDate){
                planSummary.startDate = cal.getTime()
                planSummary.endDate = cal.getTime()
            }
            if(!planSummary.subject){
                subject = session.employee?.name+" " + sdf.format(cal.getTime()) + "~" + sdf.format(cal.getTime())
            }
        }
        planSummary.subject = subject
        planSummary.properties["user"] = session.employee?.user
        planSummary.properties["employee"] = session.employee

        // 暂时先写死，将来从页面上传过来
        //planSummary.type = 2
        planSummary.state = 2

		render baseService.save(params,planSummary)
    }

    @Transactional
    def update(PlanSummary planSummary) {
		render baseService.save(params,planSummary)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        PlanSummary.executeUpdate("update PlanSummary set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }
    
}
