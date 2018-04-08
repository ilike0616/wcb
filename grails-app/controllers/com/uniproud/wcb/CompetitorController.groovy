package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional;

@UserAuthAnnotation
@Transactional(readOnly = true)
class CompetitorController {

	def modelService
	def baseService

	def list() {
		def emp = session.employee
		def extraCondition = {
//			if(params.searchValue){
//				or {
//					ilike("name","%$params.searchValue%")
//					ilike("strength","%$params.searchValue%")
//					ilike("weakneness","%$params.searchValue%")
//					ilike("opportunity","%$params.searchValue%")
//					ilike("threat","%$params.searchValue%")
//					ilike("policy","%$params.searchValue%")
//					ilike("salesAnalysis","%$params.searchValue%")
//					ilike("marketAnalysis","%$params.searchValue%")
//					ilike("remark","%$params.searchValue%")
//				}
//			}
		}
		render modelService.getDataJSON('competitor',extraCondition)
	}

    @Transactional
	def insert(Competitor competitor){
		if (competitor == null) {
			render baseService.error(params)
			return
		}
		competitor.properties["user"] = session.employee?.user
		competitor.properties["employee"] = session.employee
		render baseService.save(params,competitor)
	}

	@Transactional
	def update(Competitor competitor) {
		println params
		render baseService.save(params,competitor)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		Competitor.executeUpdate("update Competitor set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}
}
