package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional;

@UserAuthAnnotation
@Transactional(readOnly = true)
class CompetitorProductController {

    def modelService
	def baseService

	def list() {
		def emp = session.employee
		def extraCondition = {
		}
		render modelService.getDataJSON('competitor_product',extraCondition)
	}

	@Transactional
	def insert(CompetitorProduct competitorProduct){
		println params
		if (competitorProduct == null) {
			render baseService.error(params)
			return
		}
		competitorProduct.properties["user"] = session.employee?.user
		competitorProduct.properties["employee"] = session.employee
		log.info competitorProduct.errors
		render baseService.save(params,competitorProduct)
	}

	@Transactional
	def update(CompetitorProduct competitorProduct) {
		println params
		render baseService.save(params,competitorProduct)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		CompetitorProduct.executeUpdate("update CompetitorProduct set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}
}
