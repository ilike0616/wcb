package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional;

@UserAuthAnnotation
@Transactional(readOnly = true)
class MarketActivityController {

	def modelService
	def qrCodeService
	def baseService

	def list() {
		def emp = session.employee
		def extraCondition = {
//			if(params.searchValue){
//				or {
//					ilike("name","%$params.searchValue%")
//					ilike("objective","%$params.searchValue%")
//				}
//			}
		}
		render modelService.getDataJSON('market_activity',extraCondition)
	}

    @Transactional
	def insert(MarketActivity marketActivity){
		if (marketActivity == null) {
			render baseService.error(params)
			return
		}
		marketActivity.properties["user"] = session.employee?.user
		marketActivity.properties["employee"] = session.employee
		render baseService.save(params,marketActivity)
	}
	
	@Transactional
	def update(MarketActivity marketActivity) {
		if(params.detail != null){
			def detail = JSON.parse(params.detail)
			println detail
			detail.each {
				println it
				MarketActivityCustomer mac = new MarketActivityCustomer(it)
				mac.properties['user'] = session.employee?.user
				mac.properties['employee']= session.employee
				mac.properties['dept']= session.employee.dept
				if(it['customer.id']){
					mac.customer = Customer.get(it['customer.id']?.toLong())
				}
				if(it['contact.id']){
					mac.contact = Contact.get(it['contact.id']?.toLong())
				}
				if(it['marketActivity.id']){
					marketActivity = MarketActivity.get(it['marketActivity.id']?.toLong())
					mac.marketActivity = marketActivity
				}
				println mac.contact
				baseService.save(params,mac,'market_activity_customer')
				println mac.contact
			}
		}
		render baseService.save(params,marketActivity)
	}
	
	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		MarketActivity.executeUpdate("update MarketActivity set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}

	@Transactional
	def weixin() {
		println 'marketActivity   weixin'
		println params
		def marketActivity = MarketActivity.get(params.marketActivity as Long)
		marketActivity.setWeixin(Weixin.get(params.weixin))
		render([success:true] as JSON)
	}

	@Transactional
	def QRCode() {
		println params
		def marketActivity = MarketActivity.get(params.marketActivity)//marketActivity.weixin
		def sendId = QRCode.countByWeixin(marketActivity.weixin)+1
		def result = qrCodeService.getTicket(marketActivity.weixin,sendId,'QR_SCENE')
		if(result){
			render baseService.success(params)
		}else{
			render ErrorUtil.successFalse
		}
	}
}
