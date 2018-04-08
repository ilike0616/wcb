package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional(readOnly = true)
class DeptService {
    def departments(params,fields){
        def query = Dept.where{
            if(params.node && params.node != 'root'){
                parentDept{
                    id == params.node
                }
            }else{
                parentDept == null
            }
            user{
                id == params.userId?.toLong()
            }
            deleteFlag == false
            if(params.filterIds && params.filterIds.size() > 0){
                not {'in'("id",params.filterIds)}
            }
        }
        def child = []
        query.list()?.each {
            def obj = [:]
            obj << ["expanded":true]
            fields.each{field->
                if(field.fieldName.contains('.')){
                    def t = field.fieldName.tokenize('.')
                    def sub = [:]
                    if(obj.get(t[0])){
                        sub = obj.get(t[0])
                    }
                    sub << ["${t[1]}":it."${t[0]}"?."${t[1]}"]
                    obj << [(t[0]):sub]
                }else{
                    if(field.relation){
                        obj << ["$field.fieldName":[id:it."$field.fieldName"?.id]]
                    }else{
                        obj << ["$field.fieldName":it."$field.fieldName"]
                    }
                }
            }
            def childs = []
            it.getChildren().each{
                if((!params.filterIds && !it.deleteFlag) || (params.filterIds && !params.filterIds.contains(it.id) && !it.deleteFlag)){
                    childs << it
                }
            }
            if(childs && childs.size() > 0){
                obj << ["leaf":false]
            }else{
                obj << ["leaf":true]
            }

            child << obj
        }
        def json = [success:true,data:child] as JSON
        return json
    }
    @Transactional
    def departmentsForEdit(params){
        def fields = []
        def query = Dept.where{
            parentDept == null
            deleteFlag == false
            user{
                id == params.userId?.toLong()
            }
        }
        spendChild(query.list(),params,fields) as JSON
    }


    @Transactional
    def departmentsForEdit(params,fields){
        def query = Dept.where{
            parentDept == null
            deleteFlag == false
            user{
                id == params.userId?.toLong()
            }
            if(params.filterIds && params.filterIds.size() > 0){
                not {'in'("id",params.filterIds)}
            }
        }
        spendChild(query.list(),params,fields) as JSON
    }

    def spendChild(deptChilds,params,fields){
        def child = []
        def obj = [:]
        deptChilds.each {
            obj = [:]
            obj << ["id":it.getId()]
            obj << ["text":it.getName()]
            obj << ["expanded":true]
            fields.each{field->
                if(field.fieldName.contains('.')){
                    def t = field.fieldName.tokenize('.')
                    def sub = [:]
                    if(obj.get(t[0])){
                        sub = obj.get(t[0])
                    }
                    sub << ["${t[1]}":it."${t[0]}"?."${t[1]}"]
                    obj << [(t[0]):sub]
                }else{
                    obj << [(field.fieldName):it."$field.fieldName"]
                }
            }
            def childs = []
            it.getChildren().each{
                if((!params.filterIds && !it.deleteFlag) || (params.filterIds && !params.filterIds.contains(it.id) && !it.deleteFlag)){
                    childs << it
                }
            }
            if(childs && childs.size() > 0){
                def son = spendChild(childs,params,fields);
                if(son)  obj << ["children": son]
                obj << ["leaf":false]
            }else{
                obj << ["leaf":true]
            }

            if(params.checkModel && params.checkModel.toBoolean() == true){
                obj << ["checked" : false]
            }
            child << obj
        }
        child
    }

}
