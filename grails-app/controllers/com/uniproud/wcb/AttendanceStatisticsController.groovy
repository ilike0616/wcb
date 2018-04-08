package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class AttendanceStatisticsController {
    def modelService
	def baseService
    def attendanceDataService

	def list(){
		def extraCondition = {
//			if(params.searchValue){
//				ilike("employee.name","%$params.searchValue%")
//			}
		}
		render modelService.getDataJSON('attendance_statistics',extraCondition)
	}

    def test(){
        def format = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
        def employee = session?.employee
        def am = AttendanceModel.get(employee.attendanceModel?.id)
        /*// result 7.5
        def startDate = '2015-04-17 08:00:00'
        def endDate = '2015-04-17 17:30:00'
        // result 2.7
        def startDate = '2015-04-17 09:20:00'
        def endDate = '2015-04-17 12:30:00'
        // result 5
        def startDate = '2015-04-17 12:20:00'
        def endDate = '2015-04-17 18:30:00'
        // result 6.5 跨天
        def startDate = '2015-04-17 12:20:00'
        def endDate = '2015-04-18 10:30:00'
        // result 0
        def startDate = '2015-04-17 18:20:00'
        def endDate = '2015-04-17 20:30:00'*/
        // result 14.5 跨天
        def startDate = '2015-06-17 12:20:00'
        def endDate = '2015-06-22 10:30:00'
        def startDateCal = Calendar.getInstance()
        startDateCal.setTime(format.parse(startDate))
        def endDateCal = Calendar.getInstance()
        endDateCal.setTime(format.parse(endDate))
        def hours = attendanceDataService.calculateHoursByAttendanceModel(am,startDateCal.getTime(),endDateCal.getTime())
        render([success:true,msg:"startDate=${startDate},endDate=${endDate},result=${hours}"])
    }

    def test2(){
        def results = AttendanceData.createCriteria().list {
            projections {
                // 两种不同的方式
                /*sqlGroupProjection 'employee_id,sign_date, count(*) as work_day,sum(late_minutes) as late_minutes,sum(early_work_minutes) as early_work_minutes',
                        'employee_id,sign_date', ['employee_id','sign_date','work_day','late_minutes','early_work_minutes'],
                        [STRING,DATE, INTEGER,INTEGER,INTEGER]*/
                groupProperty('employee.id')
                count('isLate')
            }
        }
        results.each{
            println(it)
            println(it[0])
        }
        render (success:true) as JSON
    }
}
