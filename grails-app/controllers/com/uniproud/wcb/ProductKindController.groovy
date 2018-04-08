package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
class ProductKindController {
    def modelService
	def baseService
	
    def list(){
        def user = session.employee?.user
        // 过滤自己及下属子对象
        def filterIds = []
        if(params.filter){
            def data = JSON.parse(params.filter)
            def filterName = data[0]?.property
            if(filterName == 'filterSelfAndChildrenId'){
                def pk = ProductKind.get(data[0].value?.toLong())
                filterIds.push(pk.id)
                filterIds.addAll(modelService.getChildrenIds(pk.children))
            }
        }
        def searchClosure
        if(params.export && params.export == 'export'){
            searchClosure = {}
        }else{
            searchClosure = {
                if(params.node && params.node != 'root'){
                    eq('parentKind.id',params.node?.toLong())
                }else{
                    isNull('parentKind')
                }
                if(user){
                    eq('user.id',user.id)
                }
                if(filterIds && filterIds.size() > 0){
                    not {'in'("id",filterIds)}
                }
                eq('deleteFlag',false)
            }
        }
        def query = ProductKind.createCriteria().list(searchClosure)
        render modelService.getTreeDataJSON(query,user,'product_kind')
    }

    /**
     * 下拉列表
     * @return
     */
    @Transactional
    def productKindsForEdit(){
        def user = session.employee?.user
        def query = ProductKind.where {
            parentKind == null
            user == user
            deleteFlag == false
        }
        def json = spendChild(query.list(sort:'id',order:'ASC')) as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def spendChild(productKinds){
        def child = []
        def obj = [:]
        productKinds.each {
            obj = [:]
            obj.put("id",it.id)
            obj.put("text",it.name)
            obj.put("expanded",true)
            def childs = []
            it.getChildren().each{
                if(it.deleteFlag == false){
                    childs << it
                }
            }
            if(childs && childs.size() > 0){
                def son = spendChild(childs)
                if(son) obj.put("children", son)
                obj << ["leaf":false]
            }else{
                obj << ["leaf":true]
            }
            child << obj
        }
        child
    }

    @Transactional
    def insert(ProductKind productKind) {
        if (productKind == null) {
			render baseService.error(params)
            return
        }

        if(session.employee){
            productKind.properties["user"] = session.employee?.user
			productKind.properties["employee"] = session.employee
        }
		render baseService.save(params,productKind)
    }

    @Transactional
    def update(ProductKind productKind) {
        if (productKind == null) {
			render baseService.error(params)
            return
        }

        if(session.employee){
            productKind.properties["user"] = session.employee?.user
        }
        productKind.save flush: true
		render baseService.success(params)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ProductKind.executeUpdate("update ProductKind set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def productKind = ProductKind.get(data["id"])
        productKind.properties["parentKind"] = data["parentKind"]
        if(!productKind.validate()) {
            render([success:false,errors: errorsToResponse(productKind.errors)] as JSON)
            return
        }
        productKind.save(flush: true)
        render([success:true] as JSON )
    }
}
