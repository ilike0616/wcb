package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class ObjectFollowController {
	
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
		}
		render modelService.getDataJSON('object_follow',extraCondition)
    }

    @Transactional
    def insert(ObjectFollow objectFollow) {
        if (objectFollow == null) {
			render baseService.error(params)
            return
        }

        objectFollow.properties['user'] = session.employee?.user
        objectFollow.properties['employee'] = session.employee
		render baseService.save(params,objectFollow)
    }

    @Transactional
    def update(ObjectFollow objectFollow) {
		render baseService.save(params,objectFollow)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ObjectFollow.executeUpdate("update ObjectFollow set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    /**
	 * 跟进中点签到
	 * @param id
	 * @return
	 */
	def getSignon(Long id){
		def follow = ObjectFollow.get(id)
		def sign = follow?.signon
		if(sign){
			def photos = []
			sign.photos?.each {
				def p = Doc.get(it.id)
				photos << [id:p.id,name:p.name]
			}
			def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,objectFollow:[id:follow.id,subject:follow.subject],photos:photos]] as JSON
			render json
		}else{
			render baseService.error(params)
		}
	}
	
	/**
	 * 跟进中点签退
	 * @param id
	 * @return
	 */
	def getSignout(long id){
		def follow = ObjectFollow.get(id)
		def sign = follow?.signout
		if(sign){
			def photos = []
			sign.photos?.each {
				def p = Doc.get(it.id)
				photos << [id:p.id,name:p.name]
			}
			def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,objectFollow:[id:follow.id,subject:follow.subject],photos:photos]] as JSON
			render json
		}else{
			render baseService.error(params)
		}
	}
	
	/**
	 * 跟进中点拍照
	 * @param id	跟进ID
	 * @return
	 */
	def getPhoto(long id){
		def follow = ObjectFollow.get(id)
		if(follow){
			def records = []
			follow.photos?.each{
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
			def json = [success:true,data:[objectFollow:[id:follow.id,subject:follow.subject],photos:records]] as JSON
			log.info json
			render json
		}else{
			render baseService.error(params)
		}
	}

    /**
	 * 签到
	 * @return
	 */
	@Transactional
	def signon(){
		//处理照片
		User user = session.employee?.user
		Employee employee = session.employee
		def outsiteRecord = new OutsiteRecord(params)
		outsiteRecord.user = user
		outsiteRecord.employee = employee
		//验证
		if (!outsiteRecord.validate()) {
			log.info outsiteRecord.errors
			def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		//保存签到信息
		outsiteRecord.actionKind = 1
		outsiteRecord.save flush: true
		//更新跟进的相关签到信息
		def objectFollow = ObjectFollow.get(outsiteRecord.objectFollow.id)
		objectFollow.signon = outsiteRecord
		objectFollow.signonDate = new Date()
		objectFollow.save flush: true

		render baseService.success(params)
	}

    /**
	 * 签退
	 * @return
	 */
	@Transactional
	def signout(){
		//处理照片
		User user = session.employee?.user
		Employee employee = session.employee
		def outsiteRecord = new OutsiteRecord(params)
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
		def objectFollow = ObjectFollow.get(outsiteRecord.objectFollow.id)
		objectFollow.signout = outsiteRecord
		objectFollow.signoffDate = new Date()
		objectFollow.save flush: true
		
		render baseService.success(params)
	}
	
	/**
	 * 拍照
	 * @return
	 */
	@Transactional
	def photo(){
		//处理照片
		User user = session.employee?.user
		Employee employee = session.employee
		def outsiteRecord = new OutsiteRecord(params)
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
