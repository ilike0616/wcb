package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
class ProductController {

    def modelService
    def productService
	def baseService

    def list(){
        def emp = session.employee
        if(!params.userId){
            params.userId = emp?.user?.id
        }
        def extraCondition = {
//            if(params.searchValue){
//                ilike("name","%$params.searchValue%")
//            }
            if(params.productKind){
                def ids = productService.getChildrenIdsByProductKind(params)
                if(ids.size() > 0){
                    productKind{
                        'in'("id",ids)
                    }
                }
            }
            eq("deleteFlag",false)
        }
        def isPaging = true
        if(params.isPaging && params.isPaging.toBoolean() == false){
            isPaging = false
        }
        render modelService.getDataJSON('product',extraCondition,isPaging,true)
    }

	@Transactional
	def insert(Product product) {
        println(params)
		if (product == null) {
			render baseService.error(params)
			return
		}
        product.properties["user"] = session.employee?.user
        product.properties["employee"] = session.employee

		render baseService.save(params,product)
	}
	
    @Transactional
    def update(Product product) {
		render baseService.save(params,product)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Product.executeUpdate("update Product set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

}
