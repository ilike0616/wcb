package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@AgentAuthAnnotation
@Transactional(readOnly = true)
class SubAgentController {
    def agentService
    def modelService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = Agent.where{
            if(params.fromWhere && params.fromWhere == 'agent'){ // 代理商查询下级代理商
                def ids = []
                if(session.agent){
                    def agent = Agent.get(session.agent?.id)
                    ids.addAll(modelService.getChildrenIds(agent.children))
                }
                id in (ids)
                //parentAgent == session.agent
            }else{ // 管理员查看代理商
                parentAgent != null
            }
            deleteFlag == false
        }
        render agentService.getDataJson(query,params)
    }

    @Transactional
    def insert(Agent agent) {
        log.info params
        if (agent == null) {
            render(successFalse)
            return
        }
        if (!agent.validate()) {
            log.info(agent.errors)
            render([success:false,errors: errorsToResponse(agent.errors)] as JSON)
            return
        }
        if(params.fromWhere && params.fromWhere == 'agent'){ // 代理商查询下级代理商
            agent.parentAgent = session.agent
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
