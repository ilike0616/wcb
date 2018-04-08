package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class AgentService {
    def modelService
    def getDataJson(query,params) {
        def agents = []
        def fields = modelService.getFieldWithOutDot('com.uniproud.wcb.Agent');
        query.list(params).each {
            def agent = [:]
            fields.each{field->
                agent << [(field.fieldName):it."${field.fieldName}"]
            }
            agent << ['parentAgent':it.parentAgent?.id]
            agents << agent
        }
        def json = [success:true,data:agents, total: query.count()] as JSON
        println(json)
        if(params.callback) {
            return "${params.callback}($json)"
        }else{
            return json
        }

    }
}
