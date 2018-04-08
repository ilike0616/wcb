package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class SaleChanceFollowController {

	def modelService
	def baseService

	def list(){
		def extraCondition = {
			if(params.saleChance){
				eq('saleChance.id',params.saleChance as Long)
			}
		}
		render modelService.getDataJSON('sale_chance_follow',extraCondition)
	}

	@Transactional
	def insert(SaleChanceFollow saleChanceFollow) {
		if (saleChanceFollow == null) {
			render baseService.error(params)
			return
		}

		// 修改商机里面对应的信息
		if(saleChanceFollow.saleChance){
			def sc = SaleChance.get(saleChanceFollow.saleChance.id)
			sc.followPhase = saleChanceFollow.afterPhase
			sc.followDate = saleChanceFollow.followDate
			sc.followNum = sc.followNum?:0 + 1
			sc.followRemark = saleChanceFollow.followContent
			sc.nextFollowDate = saleChanceFollow.nextFollowDate
			sc.possibility = saleChanceFollow.possibility
			sc.foreseeDate = saleChanceFollow.foreseeDate
			sc.foreseeMoney = saleChanceFollow.foreseeMoney
			sc.save()
		}

		saleChanceFollow.properties['user'] = session.employee?.user
		saleChanceFollow.properties['employee']= session.employee
		render baseService.save(params,saleChanceFollow,'sale_chance_follow')
	}

	@Transactional
	def update(SaleChanceFollow saleChanceFollow) {
		// 修改商机里面对应的信息
		if(saleChanceFollow.saleChance){
			def sc = SaleChance.get(saleChanceFollow.saleChance.id)
			sc.followPhase = saleChanceFollow.afterPhase
			sc.followDate = saleChanceFollow.followDate
			sc.followRemark = saleChanceFollow.followContent
			sc.nextFollowDate = saleChanceFollow.nextFollowDate
			sc.possibility = saleChanceFollow.possibility
			sc.foreseeDate = saleChanceFollow.foreseeDate
			sc.foreseeMoney = saleChanceFollow.foreseeMoney
			sc.save()
		}
		render baseService.save(params,saleChanceFollow)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		SaleChanceFollow.executeUpdate("update SaleChanceFollow set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}
}
