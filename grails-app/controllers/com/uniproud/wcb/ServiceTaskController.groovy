package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class ServiceTaskController {
	
	def modelService
	def baseService
	
    def list(){
		def extraCondition = {
//			if(params.searchValue){
//				or {
//					customer{
//						ilike("name","%$params.searchValue%")
//					}
//					ilike("subject","%$params.searchValue%")
//				}
//			}
			if(params.customer){
				eq('customer.id',params.customer as Long)
			}
		}
		render modelService.getDataJSON('service_task',extraCondition)
    }

    @Transactional
    def insert(ServiceTask serviceTask) {
        if (serviceTask == null) {
			render baseService.error(params)
            return
        }

        serviceTask.properties['user'] = session.employee?.user
        serviceTask.properties['employee'] = session.employee
		render baseService.save(params,serviceTask)
    }

    @Transactional
    def update(ServiceTask serviceTask) {
		render baseService.save(params,serviceTask)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ServiceTask.executeUpdate("update ServiceTask set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

	def getSignon(Long id){
		def task = ServiceTask.get(id)
		def sign = task?.signon
		if(sign){
			def photos = []
			sign.photos?.each {
				def p = Doc.get(it.id)
				photos << [id:p.id,name:p.name]
			}
			def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,serviceTask:[id:task.id,subject:task.subject],photos:photos]] as JSON
			render json
		}else{
			render baseService.error(params)
		}
	}
	
	def getSignout(long id){
		def task = ServiceTask.get(id)
		def sign = task?.signout
		if(sign){
			def photos = []
			sign.photos?.each {
				def p = Doc.get(it.id)
				photos << [id:p.id,name:p.name]
			}
			def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,serviceTask:[id:task.id,subject:task.subject],photos:photos]] as JSON
			render json
		}else{
			render baseService.error(params)
		}
	}
	
	def getPhoto(long id){
		def task = ServiceTask.get(id)
		if(task){
			def records = []
			task.photos?.each{
				def record = OutsiteRecord.get(it.id)
				if(record){
					def photos = []
					record.photos?.each {
						def p = Doc.get(it.id)
						photos << [id:p.id,name:p.name]
					}
					records << [id:record.id,remark:record.remark,longtitude:record.longtitude,latitude:record.latitude,location:record.location,photos:photos]
				}
			}
			def json = [success:true,data:[serviceTask:[id:task.id,subject:task.subject],photos:records]] as JSON
			println json
			render json
		}else{
			render baseService.error(params)
		}
	}
	
    @Transactional
	def signon(){
		println params
		User user = session.employee?.user
		Employee employee = session.employee
		OutsiteRecord outsiteRecord = new OutsiteRecord(params)
		outsiteRecord.user = user
		outsiteRecord.employee = employee
		//验证
		if (!outsiteRecord.validate()) {
			println outsiteRecord.errors
			def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		//保存签到信息
		outsiteRecord.actionKind = 1
		outsiteRecord.save flush: true
		//更新跟进的相关签到信息
		ServiceTask serviceTask = ServiceTask.get(outsiteRecord.serviceTask.id)
		serviceTask.signon = outsiteRecord
		serviceTask.signonDate = new Date()
		serviceTask.save flush: true

		render baseService.success(params)
	}
	
	@Transactional
	def signout(){
		println params
		//处理照片
		User user = session.employee?.user
		Employee employee = session.employee
		OutsiteRecord outsiteRecord = new OutsiteRecord(params)
		outsiteRecord.user = user
		outsiteRecord.employee = employee
		//验证
		if (!outsiteRecord.validate()) {
			def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		//保存签退信息
		outsiteRecord.actionKind = 2
		outsiteRecord.save flush: true
		//修改跟进的签退时间
		ServiceTask serviceTask = ServiceTask.get(outsiteRecord.serviceTask.id)
        serviceTask.signout = outsiteRecord
        serviceTask.signoffDate = new Date()
        serviceTask.save flush: true

		render baseService.success(params)
	}
	
	@Transactional
	def photo(){
		println params
		//处理照片
		User user = session.employee?.user
		Employee employee = session.employee
		OutsiteRecord outsiteRecord = new OutsiteRecord(params)
		outsiteRecord.user = user
		outsiteRecord.employee = employee
		//验证
		if (!outsiteRecord.validate()) {
			def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		//保存拍照信息
		outsiteRecord.actionKind = 3
		outsiteRecord.save flush: true

		render baseService.success(params)
	}
	
}
