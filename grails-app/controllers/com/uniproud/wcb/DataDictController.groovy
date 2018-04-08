package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.successFalse
import static com.uniproud.wcb.ErrorUtil.successTrue

@AdminAuthAnnotation
@UserAuthAnnotation
class DataDictController {
	
	def baseService
	
    def list() {
        if(session.employee){
            params.user = session.employee.user?.id
        }
        RequestUtil.pageParamsConvert(params)
        def query = DataDict.where{
            if(params.user){
                user{
                    id  == params.user
                }
            }
            if(params.searchValue){
                ilike("text", "%$params.searchValue%")
            }
        }
        def dicts = []
        def queryList = []
        if(!params.isPaging){
            queryList = query.list(params)
        }else{
            queryList = query.list()
        }
        queryList.each{
            dicts << [
                    id:it.id,dataId:it.dataId,text:it.text,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,user:it.user?.id,issys:it.issys,items:it.items?.id/*,fields:it.fields?.id*/
            ]
        }
        def json = [success:true,data:dicts, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(DataDict dataDict) {
        if (dataDict == null) {
            render(successFalse)
            return
        }
        if(session.employee){
            dataDict.user = session.employee.user
        }

        def maxDataId = DataDict.executeQuery("select max (dataId) from DataDict where user=:puser",[puser:dataDict.user]).first()
        log.info(maxDataId)
        def newDataId = 1
        if(maxDataId){
            newDataId = maxDataId + 1
        }
        dataDict.dataId = newDataId

        if (!dataDict.validate()) {
            render([success:false,errors: errorsToResponse(dataDict.errors)] as JSON)
            return
        }

        dataDict.save flush: true
        render(successTrue)
    }

    @Transactional
    def update(DataDict dataDict) {
        if (dataDict == null) {
            render(successFalse)
            return
        }
        if (!dataDict.validate()) {
            log.info(dataDict.errors)
            render([success:false,errors: errorsToResponse(dataDict.errors)] as JSON)
            return
        }
        dataDict.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        DataDictItem.executeUpdate("delete DataDictItem where dict.id in (:ids)",[ids:ids*.toLong()])
        DataDict.executeUpdate("delete DataDict where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }
}
