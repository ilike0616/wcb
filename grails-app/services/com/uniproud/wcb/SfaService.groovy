package com.uniproud.wcb

import grails.events.Listener
import grails.transaction.Transactional
import org.grails.plugin.platform.events.EventMessage
import schemasMicrosoftComOfficeWord.STVerticalAnchor

@Transactional
class SfaService {

    def baseService
    def notifyModelService
    def grailsApplication

    /**
     * 自动绑定SFA方案
     * @param domain
     * @param moduleId
     * @return
     */
    @Listener(namespace='sfa',topic = 'autoBindingSfa')
    def autoBindingSfa(EventMessage msg) {
        def domain = msg.data.domain
        def moduleId = msg.data.moduleId
        def employee = msg.data.employee
        if(!domain || !moduleId){
            return
        }
        def module = Module.findByModuleId(moduleId)
        //查询相应模块的自动SFA方案
        def sfas = Sfa.createCriteria().list {
            eq('user', employee?.user)
            eq('module',module)
            eq('auto', true)    //自动方案
            eq("state",2)      //启用状态
            eq('deleteFlag', false)
        }
        sfas?.each {sfa->
            bindingSfa(employee,module,sfa,domain)
        }
    }

    /**
     * 修改SFA方案的关联对象时，修改其对应的待执行事件执行记录
     * @param msg
     * @return
     */
    @Listener(namespace='sfa',topic = 'sfaUpdateDomain')
    def updateDomain(EventMessage msg){
        def domain = msg.data.domain
        def module = Module.findByModuleId(msg.data.moduleId)
        def employee = msg.data.employee
        def modifyValue = msg.data.modifyValue
        def sees = SfaEventExecute.createCriteria().list {
            sfaExecute{
                eq("module",module)
                eq("linkId",domain.id)
            }
            ne("state",2)
            eq('deleteFlag', false)
        }
        if(sees){
            sees.each {
                modifyValue.each{mv->
                    if(mv.fieldName == it.sfaEvent.dateField?.fieldName || mv.fieldName == it.sfaEvent.dateFieldCycle?.fieldName){
                        log.info it.executeDate
                        it.executeDate += mv.currentValue - mv.originalValue
                        log.info it.executeDate
                    }
                }
                if(it.isNotify) {
                    it.notifyContent = notifyModelService.parseTemplate(employee.user, module, domain, it.sfaEvent.notifyContentTemplate)
                }
                if(it.isSms){
                    it.smsContent = notifyModelService.parseTemplate(employee.user,module,domain,it.sfaEvent.smsContentTemplate)
                }
                if(it.isEmail){
                    it.emailSubject = notifyModelService.parseTemplate(employee.user,module,domain,it.sfaEvent.emailSubjectTemplate)
                    it.emailContent = notifyModelService.parseTemplate(employee.user,module,domain,it.sfaEvent.emailContentTemplate)
                }
                it.save flush: true
            }
        }
    }

    /**
     * 绑定SFA方案
     * @param employee
     * @param module
     * @param sfa
     * @param domain
     * @return
     */
    def bindingSfa(Employee employee,Module module,Sfa sfa,domain){
        def sfaExecute = new SfaExecute(user:employee?.user,employee:employee,sfa: sfa,module:module,linkId: domain.id)
        if(!sfaExecute.validate()){
            log.info sfaExecute.errors
        }
        sfaExecute.save flash:true
        sfa.detail?.each {sfaEvent->
            if(!sfaEvent.deleteFlag) {
                createSfaEventExecute(employee,module,sfaEvent,sfaExecute,domain)
            }
        }
    }

    /**
     * 根据事件生成执行记录
     * @param employee
     * @param module
     * @param sfaEvent
     * @param domain
     * @param state 默认待执行
     * @return
     */
    def createSfaEventExecute(Employee employee,Module module,SfaEvent sfaEvent,SfaExecute sfaExecute,domain,def state = 1){
        def executeDate = getFirstExecuteDate(sfaEvent, domain)
        def sfaEventExecute = new SfaEventExecute(user: employee?.user, sfaExecute: sfaExecute, sfaEvent: sfaEvent, state: state, executeDate: executeDate, isNotify: sfaEvent.isNotify, isSms: sfaEvent.isSms, isEmail: sfaEvent.isEmail)
        if (sfaEvent.isNotify) {
            sfaEventExecute.notifyContent = notifyModelService.parseTemplate(employee.user, module, domain, sfaEvent.notifyContentTemplate)
        }
        if (sfaEvent.isSms) {
            sfaEventExecute.smsContent = notifyModelService.parseTemplate(employee.user, module, domain, sfaEvent.smsContentTemplate)
        }
        if (sfaEvent.isEmail) {
            sfaEventExecute.emailSubject = notifyModelService.parseTemplate(employee.user, module, domain, sfaEvent.emailSubjectTemplate)
            sfaEventExecute.emailContent = notifyModelService.parseTemplate(employee.user, module, domain, sfaEvent.emailContentTemplate)
        }
        if (sfaEvent.receiverType == 1) {//所有者
            def receiver
            if (UserField.findByUserAndModuleAndFieldName(employee.user,module,"owner")) {
                receiver = domain.owner
            }
            if (!receiver && domain.getProperties().containsKey("employee")) {
                receiver = domain.employee
            }
            if (receiver) {
                sfaEventExecute.addToEmployees(Employee.get(receiver.id))
            }
        } else if (sfaEvent.receiverType == 2) {//客户
            if (domain.getClass().name == "com.uniproud.wcb.Customer") {
                sfaEventExecute.customer = domain
            } else if (domain.getProperties().containsKey("customer")) {
                sfaEventExecute.customer = domain.customer
            }
        } else if (sfaEvent.receiverType == 3) {//相关员工
            sfaEvent.acceptors?.each { acceptor ->
                sfaEventExecute.addToEmployees(acceptor)
            }
        }
        if (!sfaEventExecute.validate()) {
            log.info sfaEventExecute.errors
        }
        sfaEventExecute.save flash: true
    }

    /**
     * 获取第一次执行的时间
     * @param sfaEvent
     * @param sfaExecute
     * @param domain
     * @param module
     * @return
     */
    def getFirstExecuteDate(SfaEvent sfaEvent,domain){
        def executeDate
        if(sfaEvent.dateType == 1){     //绝对日期
            executeDate = sfaEvent.startDate
        }else if(sfaEvent.dateType == 2){       //相对日期
            if(sfaEvent.dateField) {
                executeDate = domain."${sfaEvent.dateField?.fieldName}"     //基准日期
                if (executeDate) {
                    if (sfaEvent.difftime) {
                        def difftime = sfaEvent.difftime
                        if (sfaEvent.beforeEnd == 1) {
                            difftime = -difftime
                        }
                        executeDate = DateUtil.add(executeDate, transferPeriod(sfaEvent.diffPeriod), difftime)
                    }
                }
            }
        }else if(sfaEvent.dateType == 3){       //循环执行
//            log.info "fieldName;"+sfaEvent.dateFieldCycle?.fieldName
            if(sfaEvent.dateFieldCycle) {
                executeDate = domain."${sfaEvent.dateFieldCycle?.fieldName}"     //基准日期
                if (executeDate) {
                    if (sfaEvent.difftimeCycle) {
                        def difftime = sfaEvent.difftimeCycle
                        if (sfaEvent.beforeEndCycle == 1) {
                            difftime = -difftime
                        }
                        executeDate = DateUtil.add(executeDate, transferPeriod(sfaEvent.diffPeriodCycle), difftime)
                    }
                }
            }
        }
        if(executeDate) {
            executeDate = adjustTime(sfaEvent, executeDate)
        }
        executeDate
    }

    /**
     * 轮询事件执行表
     */
    def pollingSfaEventExecute(){
        def executes = SfaEventExecute.createCriteria().list {
            eq("state",1)
            lt("executeDate",DateUtil.add(new Date(),Calendar.MINUTE,5))
            eq("deleteFlag",false)
            order("executeDate", "asc")
        }
        executes?.each {execute->
            def module = execute.sfaEvent.sfa.module
            def objectId = execute.sfaExecute.linkId
            def object = grailsApplication.getClassForName(module.model.modelClass)
            def domain = object?.get(objectId as Long)
            if(domain && !domain.deleteFlag) {  //关联对象已删除的不再提醒
                if (execute.isNotify) {//提醒
//                log.info "产生提醒消息"
                    def subject = execute.notifySubject
                    def content = execute.notifyContent
                    execute.employees?.each { emp ->
                        def dataCode = Math.round(Math.random()*100000000)  //8位随机数
                        def notify = new Notify(user: emp.user, employee: emp, type: 1, module: module, objectId: objectId, subject: subject, content: execute.notifyContent,dataCode: dataCode)
                        notify.save flush: true
                        //推送消息
                        def extra = [:]
                        extra.put('moduleId', module?.moduleId)
                        extra.put('objectId', objectId as String)
                        if(domain.getProperties().containsKey("dataCode")) {
                            domain.dataCode = dataCode
                            domain.save()
                            extra.put('dataCode', dataCode as String)
                        }
                        baseService.jpush(JPushTool.buildPushByAlias(emp.jpushAlias, subject, content, extra))
                    }
                }
                if (execute.isSms) {//发送短信

                }
                if (execute.isEmail) {//发送邮件

                }
                execute.state = 2
                execute.save flush: true
                //该事件是否还有下一次提醒，有的话加入到SfaEventExecute表中
                def nextExecuteDate = nextExecuteDate(execute)
//            log.info "================下次执行时间：${nextExecuteDate}"
                if (nextExecuteDate) {
                    def sfaEvent = execute.sfaEvent
                    def nextExecute = new SfaEventExecute(user: execute.user, sfaExecute: execute.sfaExecute, sfaEvent: sfaEvent, executeDate: nextExecuteDate,
                            isNotify: sfaEvent.isNotify, notifySubject: execute.notifySubject, notifyContent: execute.notifyContent,
                            isSms: sfaEvent.isSms, smsSubject: execute.smsSubject, smsContent: execute.smsContent,
                            isEmail: sfaEvent.isEmail, emailSubject: execute.emailSubject, emailContent: execute.emailContent)
                    nextExecute.customer = execute.customer
                    execute.employees?.each { emp ->
                        nextExecute.addToEmployees(emp)
                    }
                    if (nextExecute.hasErrors()) {
                        log.info nextExecute.errors
                    }
                    nextExecute.save flush: true
                }
            }else{
                execute.deleteFlag = true
                execute.save flush: true
            }
        }
    }

    /**
     * 事件的下次执行时间
     * @param sfaEventExecute
     * @return
     */
    def nextExecuteDate(SfaEventExecute sfaEventExecute){
        def nextExecuteDate
        def sfaEvent = sfaEventExecute.sfaEvent
        def format = "yyyyMMdd"
        if(sfaEvent.dateType == 1){
            def next = DateUtil.add(sfaEventExecute.executeDate,Calendar.DAY_OF_YEAR,1)
            if(DateUtil.format(next,format) <= DateUtil.format(sfaEvent.endDate,format)){
                nextExecuteDate = next
            }
        }else if(sfaEvent.dateType == 2){
            def module = sfaEventExecute.sfaExecute.module
            def object = grailsApplication.getClassForName(module.model.modelClass)
            def domain = object.get(sfaEventExecute.sfaExecute.linkId)
            def firstDate = getFirstExecuteDate(sfaEvent,domain)
            def next = DateUtil.add(sfaEventExecute.executeDate,Calendar.DAY_OF_YEAR,1)
            def last = DateUtil.add(firstDate,Calendar.DAY_OF_YEAR,sfaEvent.lastingDays)
            if(DateUtil.format(next,format) <= DateUtil.format(last,format)){
                nextExecuteDate = next
            }
        }else if(sfaEvent.dateType == 3){
            def module = sfaEventExecute.sfaExecute.module
            def object = grailsApplication.getClassForName(module.model.modelClass)
            def domain = object.get(sfaEventExecute.sfaExecute.linkId)
            def firstDate = getFirstExecuteDate(sfaEvent,domain)
            def next = DateUtil.add(sfaEventExecute.executeDate,Calendar.DAY_OF_YEAR,1)
            for(int i=0;i<sfaEvent.cycleTimes;i++){
                def first = DateUtil.add(firstDate,transferPeriod(sfaEvent.diffPeriodCycle),i*sfaEvent.difftimeCycle)
                def last = DateUtil.add(first,Calendar.DAY_OF_YEAR,sfaEvent.lastingDaysCycle)
                if(DateUtil.format(first,format)<= DateUtil.format(next,format) && DateUtil.format(next,format) <=DateUtil.format(last,format)){
                    nextExecuteDate = next
                    break
                }else if(DateUtil.format(next,format) > DateUtil.format(last,format)){
                    next = DateUtil.add(first,transferPeriod(sfaEvent.diffPeriodCycle),sfaEvent.difftimeCycle)
                }
            }
        }
        nextExecuteDate
    }


    /**
     * 根据日月年返回Calendar的单位常量
     * @param diffPeriod
     * @return
     */
    def transferPeriod(diffPeriod){
        def period
        switch (diffPeriod){
            case 1:
                period = Calendar.DAY_OF_YEAR
                break
            case 2:
                period = Calendar.MONTH
                break
            case 3:
                period = Calendar.YEAR
                break
        }
        period
    }

    /**
     * 调整执行时间的时分
     * @param sfaEvent
     * @param executeDate
     * @return
     */
    def adjustTime(sfaEvent,Date executeDate){
        if(sfaEvent.timeHour || sfaEvent.timeMinute){
            def hour = sfaEvent.timeHour?:0
            def minute = sfaEvent.timeMinute?:0
            executeDate = DateUtil.set(executeDate,Calendar.HOUR_OF_DAY,hour)
            executeDate = DateUtil.set(executeDate,Calendar.MINUTE,minute)
        }
        executeDate
    }
}
