package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@UserAuthAnnotation
class TaskAssignedController {

	def modelService
	def baseService

	@Transactional(readOnly = true)
	def list(){
        println params
		def emp = session.employee
		def extraCondition = {
			if(params.taskType == '1'){//全部
			}else if(params.taskType == '2'){//待办
				'in'("state",[1,2])
			}else if(params.taskType =='3'){//已办
				'in'("state",[3])
			}
		}
		def json = modelService.getDataJSON('task_assigned',extraCondition,true)
        render json
	}
	
	@Transactional
	def insert(TaskAssigned task){
		if(task == null){
			render baseService.error(params)
			return
		}
		task.properties["user"]= session.employee.user
		task.properties["employee"] = session.employee
		if(params.executor){
			def executor = params.executor.split(",")
			task.properties["executor"] = executor*.toLong()
		}
		if(params.cc){
			def cc = params.cc.split(",")
			task.properties["cc"] = cc*.toLong()
		}
		if (task.hasErrors()){
			log.info task.errors
			def json = [success:false,errors: errorsToResponse(task.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		render baseService.save(params,task)
	}
	
	@Transactional
	def update(TaskAssigned task){
		if(params.executor){
			def executor = params.executor.split(",")
			task.properties["executor"] = executor*.toLong()
		}
		if(params.cc){
			def cc = params.cc.split(",")
			task.properties["cc"] = cc*.toLong()
		}
		render baseService.save(params,task)
	}
	
	@Transactional
	def delete(){
		def ids = JSON.parse(params.ids) as List
		TaskAssigned.executeUpdate("update TaskAssigned set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
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
		log.info json
		if(params.callback) {
			render "${params.callback}($json)"
		}else{
			render(json)
		}
	}
	
	def test(){
		chain(action: 'insert',params:[executor:[2,3], customer:30,taskContent:'刚刚1', cc:1])
	}
}
