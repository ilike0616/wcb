package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional
class AttendanceModelService {
    def baseService
    def attendanceDataService

    def notifyAttendanceSignOrSignOutJob(){
        def attendanceModel = []
        Employee.createCriteria().list(){
        }.each {
            def attendanceModelId = it.attendanceModel?.id
            if(!attendanceModel.contains(attendanceModelId)){
                attendanceModel << attendanceModelId
            }
        }
        if(attendanceModel) {
            def attendanceModelList = AttendanceModel.createCriteria().list() {
                inList("id", attendanceModel)
            }
            attendanceModelList.each { am ->
                def earlyNotifyMin = 5
                if (am.earlyNotifyMinutes > 0) earlyNotifyMin = am.earlyNotifyMinutes

                def currentCal = Calendar.getInstance()
                def lastNotifyDate = am.lastNotifyDate
                def signStartCal = attendanceDataService.generateNewDateByParamDateAndTimePeriod(currentCal.getTime(), am.startTime1)
                def signEndCal = attendanceDataService.generateNewDateByParamDateAndTimePeriod(currentCal.getTime(), am.endTime1)
                def isAllowNotify = false
                if (signStartCal) {
                    def tempDiff = (signStartCal.getTimeInMillis() - currentCal.getTimeInMillis()) / (1000 * 60)
                    if (lastNotifyDate) {
                        def tempLastDiff = (signStartCal.getTimeInMillis() - lastNotifyDate.getTime()) / (1000 * 60)
                        if (tempDiff >= 0 && tempDiff <= earlyNotifyMin && tempLastDiff > earlyNotifyMin) {
                            isAllowNotify = true
                        }
                    } else {
                        if (tempDiff >= 0 && tempDiff <= earlyNotifyMin) {
                            isAllowNotify = true
                        }
                    }
                }
                if (signEndCal && !isAllowNotify) {
                    def tempDiff = (signEndCal.getTimeInMillis() - currentCal.getTimeInMillis()) / (1000 * 60)
                    if (lastNotifyDate) {
                        def tempLastDiff = (signEndCal.getTimeInMillis() - lastNotifyDate.getTime()) / (1000 * 60)
                        if (tempDiff >= 0 && tempDiff <= earlyNotifyMin && tempLastDiff > earlyNotifyMin) {
                            isAllowNotify = true
                        }
                    } else {
                        if (tempDiff >= 0 && tempDiff <= earlyNotifyMin) {
                            isAllowNotify = true
                        }
                    }
                }
                if (am.timeMode == 2) {
                    def signOutStartCal = attendanceDataService.generateNewDateByParamDateAndTimePeriod(currentCal.getTime(), am.startTime2)
                    def signOutEndCal = attendanceDataService.generateNewDateByParamDateAndTimePeriod(currentCal.getTime(), am.endTime2)
                    if (signOutStartCal && !isAllowNotify) {
                        def tempDiff = (signOutStartCal.getTimeInMillis() - currentCal.getTimeInMillis()) / (1000 * 60)
                        if (lastNotifyDate) {
                            def tempLastDiff = (signOutStartCal.getTimeInMillis() - lastNotifyDate.getTime()) / (1000 * 60)
                            if (tempDiff >= 0 && tempDiff <= earlyNotifyMin && tempLastDiff > earlyNotifyMin) {
                                isAllowNotify = true
                            }
                        } else {
                            if (tempDiff >= 0 && tempDiff <= earlyNotifyMin) {
                                isAllowNotify = true
                            }
                        }
                    }
                    if (signOutEndCal && !isAllowNotify) {
                        def tempDiff = (signOutEndCal.getTimeInMillis() - currentCal.getTimeInMillis()) / (1000 * 60)
                        if (lastNotifyDate) {
                            def tempLastDiff = (signOutEndCal.getTimeInMillis() - lastNotifyDate.getTime()) / (1000 * 60)
                            if (tempDiff >= 0 && tempDiff <= earlyNotifyMin && tempLastDiff > earlyNotifyMin) {
                                isAllowNotify = true
                            }
                        } else {
                            if (tempDiff >= 0 && tempDiff <= earlyNotifyMin) {
                                isAllowNotify = true
                            }
                        }

                    }
                }

                if (isAllowNotify) {
                    baseService.jpush(JPushTool.buildPushByTags("AttendanceModel_${am.id}", am.notifyMessage, am.notifyMessage, [moduleId: 'attendance', objectId: 1]))
                    am.lastNotifyDate = currentCal.getTime()
                    am.save()
                }
            }
        }
    }

}
