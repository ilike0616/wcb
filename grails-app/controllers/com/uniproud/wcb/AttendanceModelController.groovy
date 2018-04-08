package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue

@UserAuthAnnotation
@Transactional(readOnly = true)
class AttendanceModelController {

    def modelService
	def baseService

	def list(){
		def extraCondition = {
//			if(params.searchValue){
//				ilike("name","%$params.searchValue%")
//			}
		}
		render modelService.getDataJSON('attendance_model',extraCondition,true,true)
	}

	@Transactional
	def insert(AttendanceModel attendanceModel) {
		if (attendanceModel == null) {
			render baseService.error(params)
			return
		}
		log.info attendanceModel
        attendanceModel.properties['user'] = session.employee?.user
        attendanceModel.properties['employee']= session.employee
        if(attendanceModel.timeMode == 1){
            attendanceModel.properties['startTime2'] = null
            attendanceModel.properties['endTime2'] = null
        }
		
		render baseService.save(params,attendanceModel)
	}

    @Transactional
    def update(AttendanceModel attendanceModel) {
        log.info(params)
        if(attendanceModel.timeMode == 1){
            attendanceModel.properties['startTime2'] = null
            attendanceModel.properties['endTime2'] = null
        }
        def json = baseService.save(params,attendanceModel)
        baseService.jpush(JPushTool.buildMsgPushByTagsSimple(session.employee?.user?.jpushTag,attendanceModel.name,[attendance:"1"]))
        render json
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ids.each {
            def am = AttendanceModel.get(it.toLong())
            Employee.where {
                attendanceModel == am
            }.list().each {emp->
                emp.attendanceModel = null
                emp.save(flush: true)
            }
        }
        AttendanceModel.executeUpdate("delete AttendanceModel where id in (:ids)",[ids:ids*.toLong()])
        baseService.jpush(JPushTool.buildMsgPushByTagsSimple(session.employee?.user?.jpushTag,"考勤模型重置",[attendance:"1"]))
        render baseService.success(params)
    }

    def getSelectedEmployee(){
        def selectedValue = []
        if(params.attendanceModelId){
            def employeeClosure = {
                eq('attendanceModel.id',params.attendanceModelId?.toLong())
            }
            Employee.createCriteria().list(employeeClosure).each {
                selectedValue.add(it.id)
            }
        }
        def json = [selectedValue:selectedValue] as JSON
        render json
    }

    @Transactional
    def bindEmployee(){
        if(!params.attendanceModelId){
            render baseService.error(params)
            return
        }
        def attendanceModel = AttendanceModel.get(params.attendanceModelId?.toLong());
        Employee.executeUpdate("update Employee set attendanceModel=null where attendanceModel=:attendanceModel",[attendanceModel:attendanceModel])
        /*Employee.where{
            attendanceModel == attendanceModel
        }.list().each{
            it.attendanceModel = null
            it.save()
        }*/
        if(params.employees){
            if(params.employees instanceof String) {
                def employee = Employee.get(params.employees.toLong())
                employee.attendanceModel = attendanceModel
                employee.save()
            }else{
                params.employees.each{
                    def employee = Employee.get(it.toLong())
                    employee.attendanceModel = attendanceModel
                    employee.save()
                }
            }
        }
        baseService.jpush(JPushTool.buildMsgPushByTagsSimple(session.employee?.user?.jpushTag,attendanceModel.name,[attendance:"1"]))
        render(successTrue)
    }
}
