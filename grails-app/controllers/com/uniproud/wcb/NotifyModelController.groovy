package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class NotifyModelController {

    def modelService
    def baseService
    def notifyModelService

    def list() {
        def extraCondition = {
            module{
                eq("moduleId", params.module?.moduleId)
            }
        }
        render modelService.getDataJSON('notify_model', extraCondition,true,true)
    }

    /**
     * 消息可接受者对应的可选字段
     * @return
     */
    def getNotifyFieldList() {
        def module = Module.findByModuleId(params.moduleId)
        def user = User.findById(session.employee?.user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }

        def notifyField = UserField.createCriteria().list {
            eq("user.id", useUser.id)
            eq("module.id", module.id)
            eq("dbType", "com.uniproud.wcb.Employee")
        }
        //可选择的消息接收人
        def list = []
        notifyField.each {
            list << [fieldName: it.fieldName, fieldText: it.text]
        }
        def json = [success: true, data: list, total: list.size()] as JSON
        render json
    }

    /**
     * 新增消息模型
     * @param notifyModel
     * @return
     */
    @Transactional
    def insert(NotifyModel notifyModel) {
        if (notifyModel == null) {
            render baseService.error(params)
            return
        }
        notifyModel.properties['user'] = session.employee?.user
        notifyModel.properties['employee'] = session.employee
        if(params.module?.moduleId){
            notifyModel.properties['module'] = Module.findByModuleId(params.module?.moduleId)
            //赋值isNotifyMany（消息接收者是否有可能是多人）
            def field = UserField.findByUserAndModuleAndFieldName(session.employee?.user,notifyModel.module,notifyModel.notifyField)
            def isNotifyMany = false
            if(field?.relation == "OneToMany" || field?.relation == "ManyToMany")
                isNotifyMany = true
            notifyModel.setIsNotifyMany(isNotifyMany)
        }else{
            render baseService.error(params)
            return
        }
        render baseService.save(params, notifyModel)
    }

    @Transactional
    def update(NotifyModel notifyModel){
        def field = UserField.findByUserAndModuleAndFieldName(session.employee?.user,notifyModel.module,notifyModel.notifyField)
        if(field == null){
            render baseService.error(params)
            return
        }
        def isNotifyMany = false
        if(field?.relation == "OneToMany" || field?.relation == "ManyToMany")
            isNotifyMany = true
        notifyModel.setIsNotifyMany(isNotifyMany)
        render baseService.save(params,notifyModel)
    }

    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        NotifyModel.executeUpdate("update NotifyModel set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render([success: true] as JSON)
    }
}
