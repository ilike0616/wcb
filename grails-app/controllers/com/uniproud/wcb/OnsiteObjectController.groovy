package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class OnsiteObjectController {

    def modelService
	def baseService

    def list(){
        def extraCondition = {
//            if(params.searchValue){
//                ilike("name","%$params.searchValue%")
//            }
        }
        render modelService.getDataJSON('onsite_object',extraCondition)
    }

    @Transactional
    def insert(OnsiteObject onsiteObject) {
        log.info params
        if (onsiteObject == null) {
			render baseService.error(params)
            return
        }
        onsiteObject.properties["user"] = session.employee?.user
        onsiteObject.properties["employee"] = session.employee
		render baseService.save(params,onsiteObject)
    }

    @Transactional
    def update(OnsiteObject onsiteObject) {
        log.info params
        if (onsiteObject == null) {
			render baseService.error(params)
            return
        }
		render baseService.save(params,onsiteObject)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        OnsiteObject.executeUpdate("update OnsiteObject set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }
}
