package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue
import static com.uniproud.wcb.ErrorUtil.successFalse
import static com.uniproud.wcb.ErrorUtil.successTrue
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@AdminAuthAnnotation
@UserAuthAnnotation
@Transactional(readOnly = true)
class DataDictItemController {
	
	def baseService
	
    def list(){
        def query = DataDictItem.where{
            if(params.dict){
                dict{
                    id  == params.dict
                }
            }
            if(params.user){
                user{
                    id == params.user
                }
            }
        }
        def items = []
        query.list(params).each{
            items << [
                    id:it.id,itemId:it.itemId,text:it.text,seq:it.seq,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,dict:it.dict?.id,user:it.user?.id
            ]
        }
        def json = [success:true,data:items, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(DataDictItem dataDictItem) {
        if (dataDictItem == null) {
            render(successFalse)
            return
        }
        if(session.employee){
            dataDictItem.user = session.employee.user
        }

        def paramMap = [:]
        paramMap.put("puser",dataDictItem.user)
        paramMap.put("pdict",dataDictItem.dict)

        def maxItemId = DataDictItem.executeQuery("select max (itemId) from DataDictItem where user = :puser and dict= :pdict",paramMap).first()
        def maxSeq = DataDictItem.executeQuery("select max (seq) from DataDictItem ddt where user = :puser and dict= :pdict",paramMap).first()

        def newItemId = 1
        if(maxItemId){
            newItemId = maxItemId + 1
        }
        def newSeq = 1
        if(maxSeq){
            newSeq = maxSeq + 1
        }
        dataDictItem.itemId = newItemId
        dataDictItem.seq = newSeq

        if (!dataDictItem.validate()) {
            render([success:false,errors: errorsToResponse(dataDictItem.errors)] as JSON)
            return
        }

        dataDictItem.save flush: true
        render(successTrue)
    }

    @Transactional
    def update(DataDictItem dataDictItem) {
        if (dataDictItem == null) {
            render(successFalse)
            return
        }
        if (!dataDictItem.validate()) {
            log.info(dataDictItem.errors)
            render([success:false,errors: errorsToResponse(dataDictItem.errors)] as JSON)
            return
        }
        dataDictItem.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        DataDictItem.executeUpdate("delete DataDictItem where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def dataDictItem
        data.each {
            dataDictItem = DataDictItem.get(it.id)
            dataDictItem.properties = it
            log.info dataDictItem.validate()
            if(!dataDictItem.validate()) {
                render([success:false,errors: errorsToResponse(dataDictItem.errors)] as JSON)
                return
            }
        }
        dataDictItem.save(flush:true)
		render baseService.success(params)
    }
}
