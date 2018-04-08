package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@UserAuthAnnotation
@Transactional(readOnly = true)
class BulletinController {
	
	def modelService
	def baseService

    def list(){
        def emp = Employee.get(session.employee?.id)

        println params
        def extraCondition = {
            createAlias('acceptDepts','acceptDepts', CriteriaSpecification.LEFT_JOIN)
            createAlias('acceptors','acceptors', CriteriaSpecification.LEFT_JOIN)
//            if(params.searchValue){
//		        ilike("subject","%$params.searchValue%")
//            }
            or{
                eq("allCompany",true)
                eq("acceptors.id",emp.id)
                eq('acceptDepts.id',emp.dept?.id)
                eq('employee.id',emp.id)
            }
        }
		render modelService.getDataJSONExcludeEmp('bulletin',extraCondition)
    }

    @Transactional
    def insert(Bulletin bulletin) {
        def user = session.employee?.user
		if (bulletin == null) {
			render baseService.error(params)
			return
		}
        if(params.acceptors){
            def acceptors = params.acceptors.split(",")
            bulletin.properties["acceptors"] = acceptors*.toLong()
        }
        if(params.acceptDepts){
            def acceptDepts = params.acceptDepts.split(",")
            bulletin.properties["acceptDepts"] = acceptDepts*.toLong()
        }
        bulletin.properties["user"] = user
        bulletin.properties["employee"] = session.employee

        def result = baseService.save(params,bulletin)

        def jsonResult = JSON.parse(result.toString())
        if(jsonResult.success){
            def extra = [:] //JPUSH推送参数
            extra.put('moduleId', 'bulletin')
            extra.put('objectId', bulletin.id as String)
            if(bulletin.getProperties().containsKey("dataCode")) {
                def dataCode = Math.round(Math.random() * 100000000)  //8位随机数
                extra.put('dataCode', dataCode as String)
                bulletin.dataCode = dataCode
                bulletin.save()
            }
            if(bulletin.allCompany == true){ // 全公司
                baseService.jpush(JPushTool.buildPushByTags(user.jpushTag,bulletin.subject,bulletin.content,extra))
            }else{
                // 如果部门不为空，则把部门Push到手机，然后再把不包含在部门里面的员工Push到手机
                def acceptDeptsTags = []
                if(bulletin.acceptDepts && bulletin.acceptDepts?.size() > 0){
                    bulletin.acceptDepts.each {Dept dept->
                        acceptDeptsTags.add(dept.jpushTag)
                    }
                    baseService.jpush(JPushTool.buildPushByTags(acceptDeptsTags,bulletin.subject,bulletin.content,extra))
                }
                if(bulletin.acceptors && bulletin.acceptors?.size() > 0){
                    bulletin.acceptors.each {Employee acceptor->
                        if(bulletin.acceptDepts){
                            if(!bulletin.acceptDepts.contains(acceptor.dept)){
                                baseService.jpush(JPushTool.buildPushByAlias(acceptor.jpushAlias,bulletin.subject,bulletin.content,extra))
                            }
                        }else{
                            baseService.jpush(JPushTool.buildPushByAlias(acceptor.jpushAlias,bulletin.subject,bulletin.content,extra))
                        }
                    }
                }
            }
        }

        render result

    }

    @Transactional
    def update(Bulletin bulletin) {
        def user = session.employee?.user
        if(bulletin.allCompany == false){
            if(params.acceptors){
                def acceptors = params.acceptors.split(",")
                bulletin.properties["acceptors"] = acceptors*.toLong()
            }
            if(params.acceptDepts){
                def acceptDepts = params.acceptDepts.split(",")
                bulletin.properties["acceptDepts"] = acceptDepts*.toLong()
            }
        }else{
            bulletin.properties["acceptors"] = null
            bulletin.properties["acceptDepts"] = null
        }

        def isModified = false
        if (bulletin.isDirty()) {
            def modifiedFieldNames = bulletin.getDirtyPropertyNames() //获取所有变更字段名
            def notifyFields = ['content','allCompany','acceptors','acceptDepts']
            for (fieldName in modifiedFieldNames) {
                if(notifyFields.contains(fieldName)){
                    isModified = true
                }
            }
        }
        def result = baseService.save(params,bulletin)

        def jsonResult = JSON.parse(result.toString())
        if(jsonResult.success && isModified == true){
            if(bulletin.allCompany == true){ // 全公司
                JPushTool.buildPushByTags(user.jpushTag,bulletin.subject,bulletin.content)
            }else{
                def acceptDeptsTags = []
                if(bulletin.acceptDepts && bulletin.acceptDepts?.size() > 0){
                    bulletin.acceptDepts.each {Dept dept->
                        acceptDeptsTags.add(dept.jpushTag)
                    }
                    JPushTool.buildPushByTags(acceptDeptsTags,bulletin.subject,bulletin.content)
                }
                if(bulletin.acceptors && bulletin.acceptors?.size() > 0){
                    bulletin.acceptors.each {Employee acceptor->
                        if(!bulletin.acceptDepts.contains(acceptor.dept)){
                            JPushTool.buildPushByAlias(acceptor.jpushAlias,bulletin.subject,bulletin.content)
                        }
                    }
                }
            }
        }
		render result
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Bulletin.executeUpdate("update Bulletin set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

}
