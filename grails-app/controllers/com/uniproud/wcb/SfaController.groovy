package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
@UserAuthAnnotation
class SfaController {

    def baseService
    def grailsApplication
    def modelService
    def sfaService

    def list() {
        def extraCondition = {
        }
        render modelService.getDataJSON('sfa', extraCondition,true,true)
    }

    @Transactional
    def insert(Sfa sfa){
        if(sfa == null && !params.linkModuleId){
            render baseService.error(params)
            return
        }
        sfa.properties["user"] = session.employee.user
        sfa.properties["employee"] = session.employee
        sfa.module = Module.findByModuleId(params.linkModuleId)
        if (sfa.hasErrors()){
            log.info sfa.errors
            def json = [success:false,errors: errorsToResponse(sfa.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params,sfa)
    }

    @Transactional
    def update(Sfa sfa){
        if(params.linkModuleId){
            sfa.module = Module.findByModuleId(params.linkModuleId)
        }
        if (sfa.hasErrors()){
            log.info sfa.errors
            def json = [success:false,errors: errorsToResponse(sfa.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params,sfa)
    }

    @Transactional
    def enable(){
        def ids = JSON.parse(params.ids) as List
        Sfa.executeUpdate("update Sfa set state=2 where id in (:ids)",[ids:ids*.toLong()])
        //启用关联方案
        def se = SfaExecute.createCriteria().list {
            sfa{
                inList("id",ids*.toLong())
            }
            eq("state",2)   //禁用的
            eq("deleteFlag",false)
        }
        if(se) {
            SfaExecute.executeUpdate("update SfaExecute set state=1 where id in (:ids)", [ids: se*.id])      //置为启用
            //启用事件执行记录
            def see = SfaEventExecute.createCriteria().list {
                sfaExecute {
                    'in'("id", se*.id)
                }
                eq("state", 3)//禁用的
                eq("deleteFlag", false)
            }
            if(see) {
                SfaEventExecute.executeUpdate("update SfaEventExecute set state=1 where id in (:ids)", [ids: see*.id])//置为待执行
            }
        }
        render baseService.success(params)
    }

    /**
    *   禁用方案
     */
    @Transactional
    def disable(){
        def ids = JSON.parse(params.ids) as List
        Sfa.executeUpdate("update Sfa set state=1 where id in (:ids)",[ids:ids*.toLong()])
        //禁止关联方案
        def se = SfaExecute.createCriteria().list {
            sfa{
                inList("id",ids*.toLong())
            }
            eq("state",1)   //启用的
            eq("deleteFlag",false)
        }
        if(se) {
            SfaExecute.executeUpdate("update SfaExecute set state=2 where id in (:ids)", [ids: se*.id])
            //禁止事件执行记录
            def see = SfaEventExecute.createCriteria().list {
                sfaExecute {
                    'in'("id", se*.id)
                }
                eq("state", 1)//待执行
                eq("deleteFlag", false)
            }
            if(see) {
                SfaEventExecute.executeUpdate("update SfaEventExecute set state=3 where id in (:ids)", [ids: see*.id])
            }
        }
        render baseService.success(params)
    }

    /**
    *   启用方案
     */
    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        Sfa.executeUpdate("update Sfa set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        SfaEvent.executeUpdate("update SfaEvent set deleteFlag=true where sfa.id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

    def getHandBingList(){
        if(!params.linkId || !params.moduleId){
            render baseService.error(params)
            return
        }
        def user = session.employee.user
        def module = Module.findByModuleId(params.moduleId)
        def sfas = Sfa.findAllByUserAndModuleAndAutoAndDeleteFlag(user,module,false,false)
        def executes = SfaExecute.findAllByUserAndModuleAndLinkIdAndStateAndDeleteFlag(user,module,params.linkId,1,false)*.sfa*.id
        def list = []
        sfas.each {
            if(executes.contains(it.id)) {
                list << [boxLabel: it.name, name: 'field', inputValue: it.id, checked: true]
            }else{
                list << [boxLabel: it.name, name: 'field', inputValue: it.id]
            }
        }
        def json = [data:list] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     *
     * @return
     */
    @Transactional
    def handBingSfa(){
        if(!params.linkId || !params.moduleId){
            render baseService.error(params)
            return
        }
        def enableSfas = params.enableSfas?.tokenize(",");
        def employee = session.employee
        def module = Module.findByModuleId(params.moduleId)
        def linkId = params.linkId as Long
        //已绑定的SFA方案
        def executes = SfaExecute.findAllByUserAndModuleAndLinkIdAndStateAndDeleteFlag(employee.user,module,linkId,1,false)
        def object = grailsApplication.getClassForName(module.model.modelClass)
        def domain = object.get(params.linkId as Long)
        //新增绑定
        enableSfas.each{
            if(!executes*.sfa*.id.contains(it)){
                //启动SFA
                def sfa = Sfa.get(it as Long)
                sfaService.bindingSfa(employee,module,sfa,domain)
            }
        }
        //取消绑定
        executes.each {
            if(enableSfas==null || !enableSfas.contains(it.id)){
                it.deleteFlag = true
                it.save()
                SfaEventExecute.executeUpdate("update SfaEventExecute set deleteFlag=true where sfaExecute.id=:ids",[ids:it.id])
            }
        }
        render([success:true,msg: '启用成功！'] as JSON)
    }
}
