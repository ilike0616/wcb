package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class CustomerFollowController {

	def modelService
	def baseService

	def list(){
		def extraCondition = {
			if(params.customer){
				eq('customer.id',params.customer as Long)
			}
		}
		render modelService.getDataJSON('customer_follow',extraCondition)
	}

	@Transactional
	def insert(CustomerFollow customerFollow) {
		if (customerFollow == null) {
			render baseService.error(params)
			return
		}

		// 修改客户里面对应的信息
		if(customerFollow.customer){
			def customer = Customer.get(customerFollow.customer.id)
			if(customerFollow.afterKind || customerFollow.afterPhase){
				if(customerFollow.afterKind){
					customer.kind = customerFollow.afterKind
				}
				if(customerFollow.afterPhase){
					customer.phase = customerFollow.afterPhase
				}
			}
			if(customerFollow.afterPhase){
				customer.phase = customerFollow.afterPhase
			}
			customer.latestFollowDate = new Date()
			customer.save()
		}

		customerFollow.properties['user'] = session.employee?.user
		customerFollow.properties['employee']= session.employee
		params.moduleId = 'customer_follow'
		render baseService.save(params,customerFollow)
	}

	@Transactional
	def update(CustomerFollow customerFollow) {
		// 修改客户里面对应的信息
		if(customerFollow.customer){
			def customer = Customer.get(customerFollow.customer.id)
			if(customerFollow.afterKind || customerFollow.afterPhase){
				if(customerFollow.afterKind){
					customer.kind = customerFollow.afterKind
				}
				if(customerFollow.afterPhase){
					customer.phase = customerFollow.afterPhase
				}

				customer.save()
			}
			if(customerFollow.afterPhase){
				customer.phase = customerFollow.afterPhase
			}
		}
		render baseService.save(params,customerFollow)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		CustomerFollow.executeUpdate("update CustomerFollow set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
//		chain(controller:'base',action: 'success',params:params)
		render baseService.success(params)
	}

	/**
	 * 跟进中点签到
	 * @param id
	 * @return
	 */
	def getSignon(Long id){
		def follow = CustomerFollow.get(id)
		def sign = follow?.signon
		if(sign){
			def photos = []
			sign.photos?.each {
				def p = Doc.get(it.id)
				photos << [id:p.id,name:p.name]
			}
			def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,customerFollow:[id:follow.id,subject:follow.subject],photos:photos]] as JSON
			render json
		}else{
//			chain(controller:'base',action: 'error',params:params)
			render baseService.error(params)
		}
	}
	/**
	 * 跟进中点签退
	 * @param id
	 * @return
	 */
	def getSignout(long id){
		def follow = CustomerFollow.get(id)
		def sign = follow?.signout
		if(sign){
			def photos = []
			sign.photos?.each {
				def p = Doc.get(it.id)
				photos << [id:p.id,name:p.name]
			}
			def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,customerFollow:[id:follow.id,subject:follow.subject],photos:photos]] as JSON
			render json
		}else{
//			chain(controller:'base',action: 'error',params:params)
			render baseService.error(params)
		}
	}
	/**
	 * 跟进中点拍照
	 * @param id	跟进ID
	 * @return
	 */
	def getPhoto(long id){
		def follow = CustomerFollow.get(id)
		if(follow){
			def records = []
			follow.photos?.each{
				def record = OutsiteRecord.get(it.id)
				if(record&&record.actionKind==3){
					def photos = []
					record.photos?.each {
						def p = Doc.get(it.id)
						photos << [id:p.id,name:p.name]
					}
					records << [
						 id:record.id,remark:record.remark,longtitude:record.longtitude,latitude:record.latitude,location:record.location,photos:photos,dateCreated:record.dateCreated,
						 employee:[
							 employeeName:record.employee?.name,
							 photo:[
								 id:record.employee?.photo?.id,name:record.employee?.photo?.name
								 ]
						  ]
					]
				}
			}
			def json = [success:true,data:[customerFollow:[id:follow.id,subject:follow.subject],photos:records]] as JSON
			println json
			render json
		}else{
//			chain(controller:'base',action: 'error',params:params)
			render baseService.error(params)
		}
	}

	/**
	 * 签到
	 * @return
	 */
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
//			chain(controller:'base', action: 'validate',model:[json:json],params:params)
			render baseService.validate(params,json)
			return
		}
		//保存签到信息
		outsiteRecord.actionKind = 1
		outsiteRecord.save flush: true
		//更新跟进的相关签到信息
		CustomerFollow customerFollow = CustomerFollow.get(outsiteRecord.customerFollow.id)
		customerFollow.signon = outsiteRecord
		customerFollow.signonDate = new Date()
		customerFollow.save flush: true

//		chain(controller:'base',action: 'success',params:params)
		render baseService.success(params)
	}

	/**
	 * 签退
	 * @return
	 */
	@Transactional
	def signout(){
		println params
		User user = session.employee?.user
		Employee employee = session.employee
		OutsiteRecord outsiteRecord = new OutsiteRecord(params)
		outsiteRecord.user = user
		outsiteRecord.employee = employee
		//验证
		if (!outsiteRecord.validate()) {
			def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
//			chain(controller:'base', action: 'validate',model:[json:json],params:params)
			render baseService.validate(params,json)
			return
		}
		//保存签退信息
		outsiteRecord.actionKind = 2
		outsiteRecord.save flush: true
		//修改跟进的签退时间
		CustomerFollow customerFollow = CustomerFollow.get(outsiteRecord.customerFollow.id)
		customerFollow.signout = outsiteRecord
		customerFollow.signoffDate = new Date()
		customerFollow.save flush: true

//		chain(controller:'base',action: 'success',params:params)
		render baseService.success(params)
	}
	/**
	 * 拍照
	 * @return
	 */
	@Transactional
	def photo(){
		println params
		User user = session.employee?.user
		Employee employee = session.employee
		OutsiteRecord outsiteRecord = new OutsiteRecord(params)
		outsiteRecord.user = user
		outsiteRecord.employee = employee
		//验证
		if (!outsiteRecord.validate()) {
			def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
//			chain(controller:'base', action: 'validate',model:[json:json],params:params)
			render baseService.validate(params,json)
			return
		}
		//保存拍照信息
		outsiteRecord.actionKind = 3
		outsiteRecord.save flush: true

//		chain(controller:'base',action: 'success',params:params)
		render baseService.success(params)
	}


}
