package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class CloseCenterController {
	
	def modelService

    def list(){
        def extraCondition = {
            if(params.saleChance){
				eq('saleChance.id',params.saleChance as Long)
			}
        }
		render modelService.getDataJSON('close_center',extraCondition)
    }

}
