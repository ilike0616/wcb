package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import grails.transaction.Transactional

@AdminAuthAnnotation
@UserAuthAnnotation
class DeptController {

    def deptService
    def modelService

    @UserAuthAnnotation
    def list(){
        if(session.employee){   // employee不为空，说明是普通用户层
           params.userId = session.employee?.user?.id
        }
        def user = User.get(params.userId.toLong())

        // 过滤自己及下属子对象
        def filterIds = []
        if(params.filter){
            def data = JSON.parse(params.filter)
            def filterName = data[0]?.property
            if(filterName == 'filterSelfAndChildrenId'){
                def e = Dept.get(data[0].value?.toLong())
                filterIds.push(e.id)
                filterIds.addAll(modelService.getChildrenIds(e.children))
                params.filterIds = filterIds
            }
        }

        def searchClosure
        if(params.export && params.export == 'export'){
            searchClosure = {}
        }else{
            searchClosure = {
                if(params.node && params.node != 'root'){
                    eq('parentDept.id',params.node?.toLong())
                }else{
                    isNull('parentDept')
                }
                if(user){
                    eq('user.id',user.id)
                }
                if(filterIds && filterIds.size() > 0){
                    not {'in'("id",filterIds)}
                }
                eq('deleteFlag',false)
            }
        }
        def query = Dept.createCriteria().list(searchClosure)
        render modelService.getTreeDataJSON(query,user,'dept')
    }

    /**
     * 普通列表，不是树形列表
     * @return
     */
    @AdminAuthAnnotation
    @AgentAuthAnnotation
    @UserAuthAnnotation
    def commonList(){
        def extraCondition = {
            if(params.searchValue){
                ilike("name","%$params.searchValue%")
            }
        }
        render modelService.getDataJSON('dept',extraCondition)
    }

    /*
     * admin层查询
     * @return
     */
    @AdminAuthAnnotation
    def listForAdmin(){
        if(params.findCondition){   // 员工新增时用到
            params.userId = params.findCondition?.toLong()
        }
        def fields = modelService.getFieldWithOutDot("com.uniproud.wcb.Dept")
        def json = deptService.departmentsForEdit(params,fields)
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @UserAuthAnnotation
    def departmentsForEdit(){
        if(session.employee){
            params.userId = session.employee?.user?.id
        }
        def json = deptService.departmentsForEdit(params)
        log.info(json)
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(Dept dept) {
        log.info params
        if (dept == null) {
            render([success:false] as JSON)
            return
        }

        if(session.employee){
            dept.user = session.employee.user
        }
        if (!dept.validate()) {
            render([success:false,errors: errorsToResponse(dept.errors)] as JSON)
            return
        }
        dept.save flush: true
        render([success:true] as JSON)
    }

    @Transactional
    def update(Dept dept) {
        log.info(dept)
        if (dept == null) {
            render([success:false] as JSON)
            return
        }
        if(session.employee){
            dept.user = session.employee.user
        }
        if (!dept.validate()) {
            render([success:false,errors: errorsToResponse(dept.errors)] as JSON)
            return
        }
        dept.save flush: true
        render([success:true] as JSON)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Dept.executeUpdate("update Dept set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render([success:true] as JSON )
    }

    @Transactional
    def save() {
        log.info params
        def data = JSON.parse(params.data)
        def dept = Dept.get(data["id"])
        dept.properties = data
        if(!dept.validate()) {
            log.info dept.errors
            render([success:false,errors: errorsToResponse(dept.errors)] as JSON)
            return
        }
        dept.save(flush: true)
        render([success:true] as JSON )
    }
}
