package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
@UserAuthAnnotation
class SfaEventController {

    def baseService
    def grailsApplication
    def modelService
    def notifyModelService
    def sfaService

    def list() {
        def user = session.employee?.user
        def result = SfaEvent.createCriteria().list(params) {
            eq("user", user)
            sfa {
                eq('id', params.sfa as Long)
            }
            ne('deleteFlag',true)
            order("index", "asc")
        }
//        def filter = NotifyModelFilter.get(params.notifyModelFilter as Long)
        def data = []
        result.each { rs ->
            def acceptors = []
            rs.acceptors?.each {emp->
                acceptors << [id:emp.id,name: emp.name]
            }
            data << [id: rs.id, name: rs.name, index: rs.index, dateType: rs.dateType, startDate: rs.startDate, endDate:rs.endDate,
                     dateField: [id:rs.dateField?.id,fieldName:rs.dateField?.fieldName,text:rs.dateField?.text],
                     beforeEnd: rs.beforeEnd, diffPeriod: rs.diffPeriod, difftime: rs.difftime, lastingDays: rs.lastingDays,
                     dateFieldCycle:[id:rs.dateFieldCycle?.id,fieldName: rs.dateFieldCycle?.fieldName,text: rs.dateFieldCycle?.text],
                     beforeEndCycle:rs.beforeEndCycle,diffPeriodCycle:rs.diffPeriodCycle,difftimeCycle:rs.difftimeCycle,
                     lastingDaysCycle:rs.lastingDaysCycle,intervalPeriod:rs.intervalPeriod,
                     interval:rs.interval,cycleTimes:rs.cycleTimes,timeHour:rs.timeHour,timeMinute:rs.timeMinute,isLunar:rs.isLunar,
                     receiverType:rs.receiverType,isNotify:rs.isNotify,isSms:rs.isSms,isEmail:rs.isEmail,notifySubjectTemplate:rs.notifySubjectTemplate,
                     notifyContentTemplate:rs.notifyContentTemplate,smsSubjcetTemplate:rs.smsSubjcetTemplate,smsContentTemplate:rs.smsContentTemplate,
                     emailSubjectTemplate:rs.emailSubjectTemplate,emailContentTemplate:rs.emailContentTemplate,dateCreated: rs.dateCreated, lastUpdated: rs.lastUpdated,
                     employee:[id:rs.employee?.id,name: rs.employee?.name],sfa:[id:rs.sfa?.id,name:rs.sfa?.name],acceptors:acceptors]
        }
        def json = [success: true, data: data, total: result?.totalCount] as JSON
        render json
    }

    @Transactional
    def insert(SfaEvent event){
        if (event == null) {
            render baseService.error(params)
            return
        }
        event.properties['user'] = session.employee?.user
        event.properties['employee'] = session.employee
        if(params.acceptors){
            params.acceptors?.tokenize(',')?.each{
                event.addToAcceptors(Employee.get(it as Long))
            }
        }
        def maxIndex = SfaEvent.executeQuery("select max(index) from SfaEvent where sfa.id = :sfaId",[sfaId: event.sfa?.id]).first()
        if(maxIndex == null){
            maxIndex = 0
        }
        event.index = maxIndex + 1
        baseService.save(params,event)
        //已关联该事件所属方案的记录，产生事件执行记录
        def sfaE = SfaExecute.createCriteria().list {
            sfa{
                eq("id",event.sfa.id)
            }
            eq("deleteFlag",false)
        }
        sfaE.each {se->
            def object = grailsApplication.getClassForName(se.module.model.modelClass)
            def domain = object.get(se.linkId)
            sfaService.createSfaEventExecute(session.employee,se.module,event,se,domain,se.state==1?:3)
        }
        render baseService.success(params)
    }

    @Transactional
    def update(SfaEvent event){
        if (event == null) {
            render baseService.error(params)
            return
        }
        if(params.acceptors){
            event.acceptors?.clear()
            def acceptors = params.acceptors?.tokenize(',')
            event.properties['acceptors'] = acceptors*.toLong()
        }
        def modifyValue =  baseService.getModifiedField(event)
        baseService.save(params,event)
        //修改了以下字段数据时修改待执行的事件执行记录
        def msgFields = ['isNotify','isSms','isEmail','notifySubjectTemplate','notifyContentTemplate','smsSubjcetTemplate','smsContentTemplate','emailSubjectTemplate','emailContentTemplate']
        def editField = modifyValue.collect {
            if(msgFields.contains(it.fieldName)){
                it.fieldName
            }
        }
        if(editField){
            //修改时间关联的执行数据
            def sees = SfaEventExecute.createCriteria().list {
                sfaEvent{
                    eq("id",event.id)
                }
                ne("state",2)   //没有执行过的
                eq("deleteFlag",false)
            }
            def module = event.sfa.module
            def object = grailsApplication.getClassForName(module.model.modelClass)
            sees.each {
                def domain = object.get(it.sfaExecute.linkId)
                it.isNotify = event.isNotify
                it.notifyContent = notifyModelService.parseTemplate(event.user, module, domain, event.notifyContentTemplate)
                it.isSms = event.isSms
                it.smsContent = notifyModelService.parseTemplate(event.user, module, domain, event.smsContentTemplate)
                it.isEmail = event.isEmail
                it.emailSubject = notifyModelService.parseTemplate(event.user, module, domain, event.emailSubjectTemplate)
                it.emailContent = notifyModelService.parseTemplate(event.user, module, domain, event.emailContentTemplate)
                it.save()
            }
        }
        render baseService.success(params)
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def eventInstance
        data.each {
            eventInstance = SfaEvent.get(it.id)
            eventInstance.index = it.index
            if(!eventInstance.validate()) {
                render([success:false,errors: errorsToResponse(eventInstance.errors)] as JSON)
                return
            }
        }
        eventInstance.save(flush:true)
        render([success:true] as JSON )
    }

    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        SfaEvent.executeUpdate("update SfaEvent set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        //删除事件对应的执行记录
        SfaEventExecute.executeUpdate("update SfaEventExecute set deleteFlag=true where sfaEvent.id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }
}
