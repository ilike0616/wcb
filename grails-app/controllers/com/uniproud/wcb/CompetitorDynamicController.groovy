package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional;

@UserAuthAnnotation
@Transactional(readOnly = true)
class CompetitorDynamicController {

	def modelService
	def baseService

	def list() {
		def extraCondition = {
		}
		render modelService.getDataJSON('competitor_dynamic',extraCondition)
	}

	@Transactional
	def insert(CompetitorDynamic competitorDynamic){
		println params
		if (competitorDynamic == null) {
			render baseService.error(params)
			return
		}
		competitorDynamic.properties["user"] = session.employee?.user
		competitorDynamic.properties["employee"] = session.employee
		render baseService.save(params,competitorDynamic)
	}

	@Transactional
	def update(CompetitorDynamic competitorDynamic) {
		println params
		render baseService.save(params,competitorDynamic)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		CompetitorDynamic.executeUpdate("update CompetitorDynamic set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}
}
