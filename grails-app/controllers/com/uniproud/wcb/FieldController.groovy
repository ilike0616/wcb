package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*
@AdminAuthAnnotation
class FieldController {
    def  modelService
    def list() {
        log.info params
        RequestUtil.pageParamsConvert(params)
        def query = Field.where{
            if(params.model){
                model{
                    id  == params.model
                }
            }
        }
        def fields = []
        query.list(params).each{
        	fields << [id:it.id,fieldName:it.fieldName,
                        dbType:it.dbType,relation:it.relation,
                        remark:it.remark,dateCreated:it.dateCreated,
                        lastUpdated:it.lastUpdated,model:[modelName:it['model']['modelName'],
                        modelClass:it['model']['modelClass']]]
        } 
        def json = [success:true,data:fields, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    def init(){
        def query =  Field.where{
            model{
                modelName:'View'
            }
        }
        log.info query.list()
    }
}
