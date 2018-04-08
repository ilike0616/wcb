package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@UserAuthAnnotation
class MyTaskController {

    def modelService
    def baseService

    def list() {
        def emp = session.employee
//        def emps = modelService.getEmployeeChildrens(emp.id)
//        emps << emp
        def ids = TaskAssigned.createCriteria().list {
            or {
                executor {
                    eq("id", emp.id)
                }
            }
            if (params.taskType == '1') {//全部
            } else if (params.taskType == '2') {//待办
                'in'("state", [1, 2])
            } else if (params.taskType == '3') {//已办
                'in'("state", [3])
            }
        }?.id
        def ccIds = TaskAssigned.createCriteria().list {
            or {
                cc {
                    eq("id", emp.id)
                }
            }
            if (params.taskType == '1') {//全部
            } else if (params.taskType == '2') {//待办
                'in'("state", [1, 2])
            } else if (params.taskType == '3') {//已办
                'in'("state", [3])
            }
        }?.id
        ids.addAll(ccIds)
        def extraCondition = {
            if (ids?.unique().size() > 0) {
                inList("id", ids?.unique())
            }else{
                eq("id", 0 as Long)
            }
        }
        def json = modelService.getDataJSON('my_task', extraCondition, true, true)
        render json
    }
    /**
     * 新增评论
     */
    @Transactional
    def comment(){
        def comment = new Comment()
        bindData(comment, params,[exclude:'id'])
        comment.properties["employee"] = session.employee
        if(comment.hasErrors()){
            def json = [success:false,errors: errorsToResponse(comment.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        baseService.save(params,comment)
        TaskAssigned task = TaskAssigned.get(params.id)
        if(params.state){
            task.state = (params.state as Integer)
        }
        task.addToComments(comment)
        task.lastComment = comment
        if(task.hasErrors()){
            def json = [success:false,errors: errorsToResponse(task.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        render baseService.save(params, task)
    }
    /**
     * 获取某一任务的回复和汇报
     * @param id-任务的id
     * @return
     */
    def getComments(){
        def task = TaskAssigned.get(params.id)
        def comments = task.comments
        def data = []
        def saveFileDir = grailsApplication.config.HTTP_FILE_URL // 文件路径
        comments.each {
            def employee = it.employee
            def photos = []
            it.photos?.each { item ->
                def p = Doc.get(item.id)
                photos << [id:p.id,name:saveFileDir+'/'+p.name,orgName:p.orgName]
            }
            data << [id:it.id,employee:[id:employee.id,name:employee.name],content:it.content,photos:photos,dateCreated:it.dateCreated]
        }
        def json = [success:true,data:data, total: data.size()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }
}
