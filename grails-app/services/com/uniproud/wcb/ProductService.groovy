package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class ProductService {
    @Transactional
    def getChildrenIdsByProductKind(params){
        def query = ProductKind.where{
            id == params.productKind.toLong()
            if(params.userId){
                user{
                    id == params.userId
                }
            }
        }
        return spendChild(query.list())
    }

    def spendChild(productKindChilds){
        def ids = []
        productKindChilds.each {
            ids.push(it.id)
            if(it.getChildren()){
                ids.addAll(spendChild(it.getChildren()));
            }
        }
        return ids
    }

}
