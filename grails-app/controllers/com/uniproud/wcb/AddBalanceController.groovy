package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@Transactional(readOnly = true)
class AddBalanceController {
    def agentService
    def static final FORBIDDEN_REDUCE_FOR_ZERO = '当前账户可用金额为0，禁止退费！'
    def static final FORBIDDEN_REDUCE = '退费金额大于当前拥有金额，禁止退费！'

    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @UserAuthAnnotation
    def list(){
        RequestUtil.pageParamsConvert(params)
        def query = AddBalance.where{
            if(params.fromWhere && params.fromWhere == 'agent'){ // 代理商调用
                if(params.kind && params.kind?.toLong() == 0){ // 全部
                    createAgent == session.agent || agent == session.agent
                }else{
                    if(params.addBalanceType && params.addBalanceType == 'my'){ // 代理商查询自己充值过的记录
                        agent == session.agent
                    }else{ // 为别人充值
                        createAgent == session.agent
                    }
                }
            }else if(params.fromWhere && params.fromWhere == 'user'){ // 企业调用
                user == session.employee?.user
            }
            // 查询不同充值类型
            if(params.kind && params.kind?.toLong() != 0){
                kind == params.kind?.toLong()
            }
        }
        def addBalances = []
        query.list(params).each {
            def operator
            def operatorName
            if(it.createAdmin){
                operator = it.createAdmin.id
                operatorName = it.createAdmin.name + "(admin)"
            }else{
                operator = it.createAgent.id
                operatorName = it.createAgent.name + "(agent)"
            }
            def prepaidUser
            def prepaidUserName
            if(it.agent){
                prepaidUser = it.agent.id
                prepaidUserName = it.agent.name + "(agent)"
            }else{
                prepaidUser = it.user.id
                prepaidUserName = it.user.name + "(user)"
            }
            addBalances << [id:it.id,kind:it.kind,createAdmin:it.createAdmin?.id,createAdminName:it.createAdmin?.name,
                            createAgent:it.createAgent?.id,createAgentName:it.createAgent?.name,agent:it.agent?.id,
                            agentName:it.agent?.name,user:it.user?.id,userName:it.user?.name,preBalance:it.preBalance,
                            postBalance:it.postBalance,balance:it.balance,realAddBalance:it.realAddBalance,remark:it.remark,
                            type:it.type,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,operator:operator,operatorName:operatorName,
                            prepaidUser:prepaidUser,prepaidUserName:prepaidUserName]
        }
        def json = [success:true,data:addBalances, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    /**
     * 充值
     * @param addBalance
     * @return
     */
    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @Transactional
    def addBalance(AddBalance addBalance) {
        println(params)
        if (addBalance == null) {
            render(successFalse)
            return
        }

        if(addBalance.kind == 1) { // 管理员为代理商充值
            // 分两种情况。1：为一级代理商充值；2：为二级代理商充值，需要扣掉上级代理商的金额
            def agent = Agent.get(params.objectId?.toLong())
            def parentAgent = agent.parentAgent
            // 判断是一级代理商还是二级代理商
            if(parentAgent){ // 二级代理商
                if(parentAgent.balance < addBalance.balance){
                    log.info(agent.errors)
                    render([success:false,msg:'上级代理商余额不足， 请先充值！'] as JSON)
                    return
                }
                // 为二级代理商充值，需要扣掉上级代理商的金额
                parentAgent.balance = parentAgent.balance.subtract(addBalance.balance)
                parentAgent.save()
            }
            // 充值记录
            addBalance.createAdmin = session.admin
            addBalance.agent = agent
            addBalance.preBalance = agent.balance
            addBalance.postBalance = agent.balance.add(addBalance.balance)
            // 更新当前操作的代理商信息
            agent.balance = agent.balance.add(addBalance.balance)
            agent.sumAddBalance = agent.sumAddBalance.add(addBalance.balance)
            agent.sumAddRealBalance = agent.sumAddRealBalance.add(addBalance.realAddBalance)
            agent.save()
        }else if(addBalance.kind == 2){ // 管理员为用户充值
            def user = User.get(params.objectId?.toLong())
            def userAgent = user.agent
            if(userAgent.balance < addBalance.balance){
                log.info(userAgent.errors)
                render([success:false,msg:'所属代理商的余额不足， 请先充值！'] as JSON)
                return
            }
            // 充值记录
            addBalance.createAdmin = session.admin
            addBalance.user = user
            addBalance.preBalance = user.balance
            addBalance.postBalance = user.balance.add(addBalance.balance)
            // 更新用户信息
            user.balance = user.balance.add(addBalance.balance)
            user.sumAddBalance = user.sumAddBalance.add(addBalance.balance)
            user.sumAddRealBalance = user.sumAddRealBalance.add(addBalance.realAddBalance)
            user.save()
            // 更新代理商信息
            userAgent.balance = userAgent.balance.subtract(addBalance.balance)
            userAgent.save()
        }else if(addBalance.kind == 3){ // 代理商为下级代理商充值
            def agent = Agent.get(params.objectId?.toLong())
            def createAgent = Agent.get(session.agent?.id)
            // 首先判断代理商余额是否足够本次充值
            if(createAgent.balance < addBalance.balance){
                log.info(createAgent.errors)
                render([success:false,msg:'您的余额不足， 请先充值！'] as JSON)
                return
            }
            addBalance.createAgent = session.agent
            addBalance.agent = agent
            addBalance.preBalance = agent.balance
            addBalance.postBalance = agent.balance.add(addBalance.balance)
            // 更新被充值的代理商信息
            agent.balance = agent.balance.add(addBalance.balance)
            agent.sumAddBalance = agent.sumAddBalance.add(addBalance.balance)
            agent.sumAddRealBalance = agent.sumAddRealBalance.add(addBalance.realAddBalance)
            agent.save()
            // 更新操作代理商的信息，上级代理商需要扣掉本次充值金额
            createAgent.balance = createAgent.balance.subtract(addBalance.balance)
            createAgent.save()
        }else if(addBalance.kind == 4){ // 代理商为企业充值
            def user = User.get(params.objectId?.toLong())
            def userAgent = user.agent
            if(userAgent.balance < addBalance.balance){
                log.info(userAgent.errors)
                render([success:false,msg:'您的余额不足， 请先充值！'] as JSON)
                return
            }
            addBalance.createAgent = userAgent
            addBalance.user = user
            addBalance.preBalance = user.balance
            addBalance.postBalance = user.balance.add(addBalance.balance)
            // 更新用户信息
            user.balance = user.balance.add(addBalance.balance)
            user.sumAddBalance = user.sumAddBalance.add(addBalance.balance)
            user.sumAddRealBalance = user.sumAddRealBalance.add(addBalance.realAddBalance)
            user.save()
            // 更新代理商信息
            userAgent.balance = userAgent.balance.subtract(addBalance.balance)
            userAgent.save()
        }else if(addBalance.kind == 5){ // 企业在线充值
        }
        if (!addBalance.validate()) {
            log.info(agent.errors)
            render([success:false,errors: errorsToResponse(addBalance.errors)] as JSON)
            return
        }
        addBalance.type = 1 // 充值
        addBalance.save flush: true
        render(successTrue)
    }

    /**
     * 退费（注意：传过来的信息为负数）
     * @param addBalance
     * @return
     */
    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @Transactional
    def reduceBalance(AddBalance addBalance) {
        println(params)
        if (addBalance == null) {
            render(successFalse)
            return
        }

        if(addBalance.kind == 1) { // 管理员为代理商退费
            // 分两种情况。1：为一级代理商退费；2：为二级代理商退费，需要加到上级代理商的金额里面
            def agent = Agent.get(params.objectId?.toLong())
            if(agent.balance <= 0){
                log.info(agent.errors)
                render([success:false,msg:FORBIDDEN_REDUCE_FOR_ZERO] as JSON)
                return;
            }
            def parentAgent = agent.parentAgent
            if(Math.abs(addBalance.balance) > agent.balance){ // 退费金额大于实际拥有的金额
                log.info(agent.errors)
                render([success:false,msg:FORBIDDEN_REDUCE] as JSON)
                return
            }
            if(parentAgent){ // 二级代理商
                // 为二级代理商退费，需要把扣掉的金额加到上级代理商上面
                parentAgent.balance = parentAgent.balance.subtract(addBalance.balance)
                parentAgent.save()
            }
            // 退费记录
            addBalance.createAdmin = session.admin
            addBalance.agent = agent
            addBalance.preBalance = agent.balance
            addBalance.postBalance = agent.balance.add(addBalance.balance)
            // 更新当前操作的代理商信息
            agent.balance = agent.balance.add(addBalance.balance)
            agent.sumAddBalance = agent.sumAddBalance.add(addBalance.balance)
            agent.sumAddRealBalance = agent.sumAddRealBalance.add(addBalance.realAddBalance)
            agent.save()
        }else if(addBalance.kind == 2){ // 管理员为用户退费
            def user = User.get(params.objectId?.toLong())
            if(user.balance <= 0){
                log.info(user.errors)
                render([success:false,msg:FORBIDDEN_REDUCE_FOR_ZERO] as JSON)
                return;
            }
            def userAgent = user.agent
            if(Math.abs(addBalance.balance) > user.balance){
                log.info(user.errors)
                render([success:false,msg:FORBIDDEN_REDUCE] as JSON)
                return
            }
            // 退费记录
            addBalance.createAdmin = session.admin
            addBalance.user = user
            addBalance.preBalance = user.balance
            addBalance.postBalance = user.balance.add(addBalance.balance)
            // 更新用户信息
            user.balance = user.balance.add(addBalance.balance)
            user.sumAddBalance = user.sumAddBalance.add(addBalance.balance)
            user.sumAddRealBalance = user.sumAddRealBalance.add(addBalance.realAddBalance)
            user.save()
            // 更新代理商信息
            userAgent.balance = userAgent.balance.subtract(addBalance.balance)
            userAgent.save()
        }else if(addBalance.kind == 3){ // 代理商为下级代理商退费
            def agent = Agent.get(params.objectId?.toLong())
            if(agent.balance <= 0){
                log.info(agent.errors)
                render([success:false,msg:FORBIDDEN_REDUCE_FOR_ZERO] as JSON)
                return;
            }
            def createAgent = Agent.get(session.agent?.id)
            // 首先判断代理商余额是否足够本次退费
            if(Math.abs(addBalance.balance) > agent.balance){
                log.info(agent.errors)
                render([success:false,msg:FORBIDDEN_REDUCE] as JSON)
                return
            }
            addBalance.createAgent = session.agent
            addBalance.agent = agent
            addBalance.preBalance = agent.balance
            addBalance.postBalance = agent.balance.add(addBalance.balance)
            // 更新被退费的代理商信息
            agent.balance = agent.balance.add(addBalance.balance)
            agent.sumAddBalance = agent.sumAddBalance.add(addBalance.balance)
            agent.sumAddRealBalance = agent.sumAddRealBalance.add(addBalance.realAddBalance)
            agent.save()
            // 更新操作代理商的信息，上级代理商需要扣掉本次退费金额
            createAgent.balance = createAgent.balance.subtract(addBalance.balance)
            createAgent.save()
        }else if(addBalance.kind == 4){ // 代理商为企业退费
            def user = User.get(params.objectId?.toLong())
            if(user.balance <= 0){
                log.info(user.errors)
                render([success:false,msg:FORBIDDEN_REDUCE_FOR_ZERO] as JSON)
                return;
            }
            def userAgent = user.agent
            if(Math.abs(addBalance.balance) > user.balance){
                log.info(user.errors)
                render([success:false,msg:FORBIDDEN_REDUCE] as JSON)
                return
            }
            addBalance.createAgent = userAgent
            addBalance.user = user
            addBalance.preBalance = user.balance
            addBalance.postBalance = user.balance.add(addBalance.balance)
            // 更新用户信息
            user.balance = user.balance.add(addBalance.balance)
            user.sumAddBalance = user.sumAddBalance.add(addBalance.balance)
            user.sumAddRealBalance = user.sumAddRealBalance.add(addBalance.realAddBalance)
            user.save()
            // 更新代理商信息
            userAgent.balance = userAgent.balance.subtract(addBalance.balance)
            userAgent.save()
        }else if(addBalance.kind == 5){ // 企业在线退费
        }
        if (!addBalance.validate()) {
            log.info(agent.errors)
            render([success:false,errors: errorsToResponse(addBalance.errors)] as JSON)
            return
        }
        addBalance.type = 2 // 退费
        addBalance.save flush: true
        render(successTrue)
    }

}
