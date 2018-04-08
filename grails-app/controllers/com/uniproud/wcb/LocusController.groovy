package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class LocusController {

	def modelService
	def baseService

	def list() {
		def extraCondition = {
		}
		render modelService.getDataJSON('locus',extraCondition)
	}

	def detailList(){
		if(!params.locus){
			render baseService.error(params)
			return
		}
        RequestUtil.pageParamsConvert(params)
//		def emps = modelService.getEmployeeChildrens(emp.id)
//		def fns = Field.where{
//			model{ modelClass == LocusDetail.name }
//		}.list()?.fieldName
//		def extraCondition = {}
//		def searchCondition = modelService.getSearchCondition(extraCondition,fns,params,emp,null,true)
//		def result = LocusDetail.createCriteria().list(params){
//			searchCondition.delegate = delegate
//			searchCondition()
//			eq('locus.id',params.locus as Long)
//			order("locusDate", "desc")
//		}
        def result = LocusDetail.createCriteria().list(params){
            if(params.locus){
                eq('locus.id',params.locus as Long)
            }
            order("locusDate", "desc")
        }
		def data = []
		result?.each{rs->
			data << [id:rs.id,employee:[id:rs.employee?.id,name:rs.employee?.name],location:rs.location,longtitude:rs.longtitude,
				latitude:rs.latitude,locusDate:rs.locusDate,mac:rs.mac,model:rs.model,dateCreated:rs.dateCreated]
		}
		def json = [success:true,data:data,total:result.totalCount] as JSON
		render json
	}

    /**
     * 插入移动轨迹
     * @param detail
     * @return
     */
    @NormalAuthAnnotation
	@Transactional
	def insert(LocusDetail detail){
		if(!params.token){
			render ([success:false,msg:'会话无效！'] as JSON)
			return
		}
		def employee = Employee.findByToken(params.token)
		def user = employee?.user
		if(employee==null){
			render ([success:false,msg:'令牌信息不正确！'] as JSON)
			return
		}
		detail.locusDate = new Date() - (detail.submitDate - detail.locusDate)
		//是否有当天的定位记录
        def dateMap = SysTool.generateStartAndEndDate(detail.locusDate)
		def locus = Locus.findByEmployeeAndStartDateBetween(employee, dateMap.get('startDate'),dateMap.get('endDate'))
		if(locus){
			locus.latestDate = detail.locusDate
			locus.location = detail.location
			locus.longtitude = detail.longtitude
			locus.latitude = detail.latitude
			locus.save flush: true
		}else{
			locus = new Locus(user:user,employee:employee,startDate:detail.locusDate,latestDate:detail.locusDate,location:detail.location,longtitude:detail.longtitude,latitude:detail.latitude).save()
		}
		detail.locus = locus
        detail.user = user
        detail.employee = employee
		//保存定位信息
		if (!detail.validate()) {
			log.info detail.errors
			def json = [success:false,errors: errorsToResponse(detail.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		detail.save flush: true
		if(employee.isAttached()){
			employee.attach()
		}
		employee.location = detail.location
		employee.longtitude = detail.longtitude
		employee.latitude = detail.latitude
		employee.locusDate = detail.locusDate 
		employee.save flush: true
		render baseService.success(params)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		Locus.executeUpdate("update Locus set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		LocusDetail.executeUpdate("update LocusDetail set deleteFlag=true where locus.id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}

	@Transactional
	def deleteDetail() {
		def ids = JSON.parse(params.ids) as List
		LocusDetail.executeUpdate("update LocusDetail set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}
}
