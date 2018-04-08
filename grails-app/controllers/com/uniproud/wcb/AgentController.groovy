package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@Transactional(readOnly = true)
class AgentController {
    def agentService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = Agent.where{
            parentAgent == null
            deleteFlag == false
        }
        render agentService.getDataJson(query,params)
    }

    def commonList(){
        def query = Agent.where{
        }
        def datas = []
        query.list().each {
            datas << [id:it.id,agentId: it.agentId, name:it.name]
        }
        def json = [success:true,data:datas, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(Agent agent) {
        if (agent == null) {
            render(successFalse)
            return
        }
        if (!agent.validate()) {
            render([success:false,errors: errorsToResponse(agent.errors)] as JSON)
            return
        }
        agent.save flush: true
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Agent.executeUpdate("update Agent set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

    @Transactional
    def update(Agent agent) {
        if (agent == null) {
            render(successFalse)
            return
        }
        if(agent.isInner){
            if(agent.isOthers){
                agent.provinces = ''
            }
        }else{
            agent.isOthers = false
            agent.provinces = ''
        }
        if (!agent.validate()) {
            render([success:false,errors: errorsToResponse(agent.errors)] as JSON)
            return
        }
        agent.save flush: true
        render(successTrue)
    }

    /**
     * 初始化密码
     * @return
     */
    @Transactional
    def initPassword() {
        def ids = JSON.parse(params.ids) as List
        Agent.executeUpdate("update Agent set password='000000' where id in (:ids)",[ids:ids*.toLong()])
        render(successTrue )
    }

}
