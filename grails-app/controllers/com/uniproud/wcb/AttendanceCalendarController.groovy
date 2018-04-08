package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

@UserAuthAnnotation
@Transactional(readOnly = true)
class AttendanceCalendarController {
	def modelService
	def baseService
    def list(){
        def extraCondition = {
        }
		render modelService.getDataJSON('attendance_calendar',extraCondition)
    }

    @Transactional
    def insert(AttendanceCalendar attendanceCalendar) {
		if (attendanceCalendar == null) {
			render baseService.error(params)
			return
		}
        attendanceCalendar.properties["user"] = session.employee.user
        attendanceCalendar.properties["employee"] = session.employee
		render baseService.save(params,attendanceCalendar)
    }

    @Transactional
    def update(AttendanceCalendar attendanceCalendar) {
		render baseService.save(params,attendanceCalendar)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Contact.executeUpdate("delete AttendanceCalendar where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    def getSpecialDay(){
        //params.attendanceCalendar = "71008"
        if(!params.attendanceCalendar){
            def json = [success:false] as JSON
            render json
            return
        }
        def fmt = new SimpleDateFormat("yyyy-MM-dd")
        def specialDays = []
        WorkDay.createCriteria().list(){
            eq("attendanceCalendar.id",params.attendanceCalendar?.toLong())
        }.each {
            specialDays << [workDate:fmt.format(it.workDate),kind:it.kind,remark: it.remark]
        }
        def json = [success:true,specialDays:specialDays] as JSON
        render json
    }

    @Transactional
    def insertOrUpdWorkDay(WorkDay workDay){
        if(!workDay.attendanceCalendar){
            render([success:false] as JSON)
            return
        }
        def wd = WorkDay.findByAttendanceCalendarAndUserAndWorkDate(workDay.attendanceCalendar,session.employee?.user,workDay.workDate)
        if(wd){
            wd.properties = params
            wd.save(flush: true)
        }else{
            workDay.properties["user"] = session.employee.user
            workDay.properties["employee"] = session.employee
            workDay.save(flush: true)
        }
        render([success:true] as JSON)
    }

    @Transactional
    def deleteWorkDay(WorkDay workDay){
        if(!workDay.attendanceCalendar || !workDay.workDate){
            render([success:false] as JSON)
            return
        }
        def wd = WorkDay.findByAttendanceCalendarAndUserAndWorkDate(workDay.attendanceCalendar,session.employee?.user,workDay.workDate)
        if(wd){
            wd.delete()
        }
        render([success:true] as JSON)
    }

}
