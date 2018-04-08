package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.beans.Transient
import java.text.SimpleDateFormat

@UserAuthAnnotation
@Transactional(readOnly = true)
class AttendanceDataController {

    def modelService
	def baseService
    def attendanceDataService

    def static SIGN = 1
    def static SIGN_OUT = 2

	def list(){
		def extraCondition = {
//			if(params.searchValue){
//				or {
//					employee{
//						ilike("name","%$params.searchValue%")
//					}
//					ilike("remark","%$params.searchValue%")
//				}
//			}
		}
		render modelService.getDataJSON('attendance_data',extraCondition)
	}

	@Transactional
	def insert() {
        def attendanceData = new AttendanceData()
        attendanceData.properties = params
		if (attendanceData == null) {
			render baseService.error(params)
			return
		}
        def attendanceModel = AttendanceModel.get(session.employee?.attendanceModel?.id)
        def signPeriod = params.signPeriod ? params.signPeriod.toInteger() : 1
        def signDate = new Date()
        // 判断是否超过签到时间
        def isExceedTimeLimit = attendanceDataService.judgeIsExceedTimeLimit(attendanceModel,signDate,signPeriod)
        if(isExceedTimeLimit == true){
            render([success:false,msg:'对不起，已经过了该时间段的签到时间！'] as JSON)
            return
        }

        // 判断是否已经签到过了
        def result = attendanceDataService.judgeIsSignedOrSignedOut(signDate,SIGN,signPeriod)
        if(result.isSignedOrSignedOut == true){
            render([success:true,msg:'对不起，您已经签到过了，禁止重复签到！',signOrSignOutDate:result.signOrSignOutDate] as JSON)
            return
        }

        attendanceData.signDate = signDate
        if(attendanceModel){ // 只有配置模板才能执行
            attendanceData.lateMinutes = attendanceDataService.getLateMinutes(attendanceModel,attendanceData.signDate,signPeriod)
            attendanceData.isLate = attendanceDataService.getIsLate(attendanceModel,attendanceData.lateMinutes)
            attendanceData.workTime = attendanceDataService.getWorkTime(attendanceModel,signPeriod)
            attendanceData.isWorkDay = attendanceDataService.judgeIsWorkDay(attendanceModel,attendanceData.signDate)
        }
        attendanceData.properties['user'] = session.employee?.user
        attendanceData.properties['employee']= session.employee

		def json = baseService.save(params,attendanceData)
        // 额外添加返回的信息
        def jsonResult = JSON.parse(json.toString())
        jsonResult << [signOrSignOutDate: signDate.getTime()]
        render jsonResult as JSON
	}

    @Transactional
    def update() {
        def attendanceModel = AttendanceModel.get(session.employee?.attendanceModel?.id)
        def signPeriod = params.signPeriod ? params.signPeriod.toInteger() : 1
        def signOutDate = new Date()
        def dateMap = SysTool.generateStartAndEndDate(signOutDate)
        def attendanceData
        def attendanceDataList = AttendanceData.findAllByEmployeeAndSignDateBetween(session.employee,dateMap.get('startDate'),dateMap.get('endDate'),[sort:'dateCreated',order:'desc'])

        if(attendanceModel){
            // 判断是否超过签到时间
            def isExceedTimeLimit = attendanceDataService.judgeIsExceedTimeLimit(attendanceModel,signOutDate,signPeriod)
            if(isExceedTimeLimit == true){
                render([success:false,msg:'对不起，已经过了该时间段的签退！'] as JSON)
                return
            }

            // 判断是否已经签到过了
            def result = attendanceDataService.judgeIsSignedOrSignedOut(signOutDate,SIGN_OUT,signPeriod)
            if(result.isSignedOrSignedOut == true){
                render([success:true,msg:'对不起，您已经签退过了，禁止重复签退！',signOrSignOutDate: result.signOrSignOutDate] as JSON)
                return
            }

            if(signPeriod == 1){ // 第一次签退，则取第一条记录
                attendanceData = attendanceDataList.get(0)
            }else if(signPeriod == 2){ // 第二次签退
                if(attendanceDataList.size() == 1){ // 如果查询结果为一条，且这条记录是第一段的数据，则说明第二段没有签到
                    def timePeriod = attendanceDataService.getParamDateBelongToWhichTimePeriod(attendanceModel,attendanceDataList.get(0)?.signDate)
                    if(timePeriod == 1){
                        render([success: false, msg: '您还未签到，请签到后再签退！']) as JSON
                        return
                    }
                    attendanceData = attendanceDataList.get(0)
                }else if(attendanceDataList.size() == 2){ // 如果查询结果为两条，则取第二条
                    attendanceData = attendanceDataList.get(1)
                }
            }
            attendanceData.properties = params
            attendanceData.signOutDate = signOutDate
            attendanceData.earlyWorkMinutes = attendanceDataService.getEarlyWorkMinutes(attendanceModel,attendanceData.signOutDate,signPeriod)
            attendanceData.isEarlyWork = attendanceDataService.getIsEarlyWork(attendanceModel,attendanceData.earlyWorkMinutes)
            attendanceData.realWorkTime = attendanceDataService.getRealWorkTime(attendanceModel,attendanceData.signDate,signOutDate,signPeriod)
        }
        def json = baseService.save(params,attendanceData)
        // 额外添加返回的信息
        def jsonResult = JSON.parse(json.toString())
        jsonResult << [signOrSignOutDate: signOutDate.getTime()]
        render jsonResult as JSON
    }

    @Transactional
    def testAttendanceDataStat(){
        attendanceDataService.statisticsAttendanceData()
        render ([success:true] as JSON)
    }

}
