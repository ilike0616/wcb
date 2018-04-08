package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional
class AttendanceDataService {
    def format = new SimpleDateFormat("yyyy-MM-dd")
    def formatWithHM = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    def formatWithHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * 获取开始和结束时间之间应该上班的天数
     * @param attendanceModel
     * @param startDate
     * @param endDate
     */
    def getAllWorkDay(AttendanceModel attendanceModel,Date startDate, Date endDate){
        def allWorkDay = 0
        if(attendanceModel){
            Calendar startDateCal = Calendar.getInstance()
            startDateCal.setTime(startDate)
            Calendar endDateCal = Calendar.getInstance()
            endDateCal.setTime(endDate)
            while(startDateCal.getTimeInMillis() <= endDateCal.getTimeInMillis()){
                def isWorkDay = judgeIsWorkDay(attendanceModel,startDateCal.getTime())
                if(isWorkDay == true) allWorkDay += 1
                startDateCal.add(Calendar.DATE,1)
            }
        }
        return allWorkDay
    }

    /**
     * 根据考勤模型得到一天应该工作的时长（小时）
     * @param attendanceModel 考勤模型
     * @param period 第几个时间段,目前支持 0:全部；1：第一时间段；2：第二时间段
     */
    def getWorkTime(AttendanceModel attendanceModel,int period){
        def workTime = 0;
        if(attendanceModel){
            if(period == 0){
                if(attendanceModel.timeMode == 1){ // 单时间模式
                    if(attendanceModel.startTime1 && attendanceModel.endTime1){
                        def startCalendar = generateNewDateByTimePeriod(attendanceModel.startTime1)
                        def endCalendar = generateNewDateByTimePeriod(attendanceModel.endTime1)
                        Long diff = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()
                        workTime = calculateHours(diff)
                    }
                }else if(attendanceModel.timeMode == 2){ // 多时间模式
                    if(attendanceModel.startTime1 && attendanceModel.endTime1 && attendanceModel.startTime2 && attendanceModel.endTime2){
                        def startCalendar1 = generateNewDateByTimePeriod(attendanceModel.startTime1)
                        def endCalendar1 = generateNewDateByTimePeriod(attendanceModel.endTime1)
                        def startCalendar2 = generateNewDateByTimePeriod(attendanceModel.startTime2)
                        def endCalendar2 = generateNewDateByTimePeriod(attendanceModel.endTime2)
                        Long diff1 = endCalendar1.getTimeInMillis() - startCalendar1.getTimeInMillis()
                        Long diff2 = endCalendar2.getTimeInMillis() - startCalendar2.getTimeInMillis()
                        workTime = calculateHours(diff1 + diff2)
                    }
                }
            }else if(period == 1){
                if(attendanceModel.startTime1 && attendanceModel.endTime1){
                    def startCalendar = generateNewDateByTimePeriod(attendanceModel.startTime1)
                    def endCalendar = generateNewDateByTimePeriod(attendanceModel.endTime1)
                    Long diff = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()
                    workTime = calculateHours(diff)
                }
            }else if(period == 2){
                if(attendanceModel.startTime2 && attendanceModel.endTime2){
                    def startCalendar = generateNewDateByTimePeriod(attendanceModel.startTime2)
                    def endCalendar = generateNewDateByTimePeriod(attendanceModel.endTime2)
                    Long diff = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()
                    workTime = calculateHours(diff)
                }
            }
        }
        return workTime
    }

    /**
     * 根据签到和签退时间计算出实际上班时长（小时）
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param attendanceModel 考勤模型
     * @param period 时间段，目前支持 1：第一时间段；2：第二时间段
     */
    def getRealWorkTime(AttendanceModel attendanceModel,Date startDate,Date endDate,int period){
        def startCalendar = Calendar.getInstance()
        startCalendar.setTime(startDate)
        def endCalendar = Calendar.getInstance()
        endCalendar.setTime(endDate)
        if(1!=1){ // 考虑某些公司规定，早到公司或晚离开公司按正常上下班算（先保留，以备后续使用）
            // 得到模板配置的时间
            def modelStartCalendar
            def modelEndCalendar
            if(period == 1){ // 第一段
                modelStartCalendar = generateNewDateByTimePeriod(attendanceModel.startTime1)
                modelEndCalendar = generateNewDateByTimePeriod(attendanceModel.endTime1)
            }else if(period == 2){ // 第二段
                modelStartCalendar = generateNewDateByTimePeriod(attendanceModel.startTime2)
                modelEndCalendar = generateNewDateByTimePeriod(attendanceModel.endTime2)
            }
            // 上班时间在考勤模板规定时间之前签到，则上班时间按照规定时间算
            if(modelStartCalendar > startCalendar) startCalendar = modelStartCalendar
            // 下班时间在考勤模板规定时间之后签退，则下班时间按照规定时间算
            if(endCalendar > modelEndCalendar) endCalendar = modelEndCalendar
        }
        def milliSeconds = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()
        return calculateHours(milliSeconds)
    }
    /**
     * 双时间段模式下得到传递的参数日期属于哪个时间段
     * @param attendanceModel 考勤模型
     * @param paramDate 参数日期
     * @return 1：第一段；2：第二段
     */
    def getParamDateBelongToWhichTimePeriod(AttendanceModel attendanceModel,Date paramDate){
        def timePeriod = 1;
        if(attendanceModel){
            if(attendanceModel.timeMode == 2){
                if(attendanceModel.startTime1 && attendanceModel.endTime1 && attendanceModel.startTime2 && attendanceModel.endTime2){
                    def endCalendar1 = generateNewDateByTimePeriod(attendanceModel.endTime1)
                    def paramDateCalendar = Calendar.getInstance()
                    paramDateCalendar.setTime(paramDate)
                    if(paramDateCalendar.getTimeInMillis() <= endCalendar1.getTimeInMillis()){ // 参数日期在第一段结束时间之前，属于第一段
                        timePeriod = 1
                    }else { // 否则属于第二段
                        timePeriod = 2
                    }
                }
            }
        }
        return timePeriod
    }

    /**
     * 根据迟到分钟数，判断是否算迟到
     * @param lateMinutes 迟到分钟数
     * @param attendanceModel 考勤模型
     * @return
     */
    def getIsLate(AttendanceModel attendanceModel,def lateMinutes){
        def isLate = false
        if(attendanceModel){
            isLate = lateMinutes > attendanceModel.maxLateTime ? true : false
        }
        return isLate
    }

    /**
     * 得到迟到分钟数
     * @param date 签到时间
     * @param attendanceModel 考勤模型
     * @param signPeriod 第几次签到,支持：1：第一时间段,2：第二时间段
     * @return
     */
    def getLateMinutes(AttendanceModel attendanceModel,Date date,int signPeriod){
        def lateMinutes = 0
        if(attendanceModel){ // 绑定模板才能计算
            def dateCalendar = Calendar.getInstance()
            dateCalendar.setTime(date)

            def rulesWorkCal
            if(signPeriod == 1){
                rulesWorkCal = generateNewDateByTimePeriod(attendanceModel.startTime1)
            }else if(signPeriod == 2){
                rulesWorkCal = generateNewDateByTimePeriod(attendanceModel.startTime2)
            }

            if(dateCalendar.getTimeInMillis() > rulesWorkCal.getTimeInMillis()){
                long diff = dateCalendar.getTimeInMillis() - rulesWorkCal.getTimeInMillis()
                lateMinutes = calculateMinutes(diff)
                // 如果模型规定最大迟到分钟数不为空且大于0，并且当前迟到的分钟数不大于模型规定的值，则迟到为0
                if(attendanceModel.maxLateTime && attendanceModel.maxLateTime > 0 && lateMinutes <= attendanceModel.maxLateTime){
                    lateMinutes = 0
                }
            }
        }
        return lateMinutes
    }

    /**
     * 根据早退分钟数，判断是否算早退
     * @param lateMinutes 早退分钟数
     * @param attendanceModel 考勤模型
     * @return
     */
    def getIsEarlyWork(AttendanceModel attendanceModel,def earlyWorkMinutes){
        def isEarlyWork = false
        if(attendanceModel){
            isEarlyWork = earlyWorkMinutes > attendanceModel.maxEarlyWork ? true : false
        }
        return isEarlyWork
    }

    /**
     * 得到早退分钟数
     * @param date 签到时间
     * @param attendanceModel 考勤模型
     * @param latePeriod 第几次签到,支持：1：第一时间段,2：第二时间段
     * @return
     */
    def getEarlyWorkMinutes(AttendanceModel attendanceModel,Date date,int signOutPeriod){
        def earlyWorkMinutes = 0
        if(attendanceModel){
            def dateCalendar = Calendar.getInstance()
            dateCalendar.setTime(date)
            def rulesOffWorkCal
            if(signOutPeriod == 1){
                rulesOffWorkCal = generateNewDateByTimePeriod(attendanceModel.endTime1)
            }else if(signOutPeriod == 2){
                rulesOffWorkCal = generateNewDateByTimePeriod(attendanceModel.endTime2)
            }

            if(dateCalendar.getTimeInMillis() < rulesOffWorkCal.getTimeInMillis()){
                long diff = rulesOffWorkCal.getTimeInMillis() - dateCalendar.getTimeInMillis()
                earlyWorkMinutes = calculateMinutes(diff)
                // 如果模型规定最大早退分钟数不为空且大于0，并且当前早退的分钟数不大于模型规定的值，则早退为0
                if(attendanceModel.maxEarlyWork && attendanceModel.maxEarlyWork > 0 && earlyWorkMinutes <= attendanceModel.maxEarlyWork){
                    earlyWorkMinutes = 0
                }
            }
        }
        return earlyWorkMinutes
    }

    /**
     * 根据传递的时分,获取新的时间
     * @param timePeriod  时分参数，格式如：09:30
     */
    def generateNewDateByTimePeriod(timePeriod){
        return generateNewDateByParamDateAndTimePeriod(null,timePeriod)
    }
    /**
     * 根据传递日期加上时分,得到新的时间
     * @param timePeriod  时分参数，格式如：09:30
     */
    def generateNewDateByParamDateAndTimePeriod(Date paramDate,String timePeriod){
        if(!timePeriod) return null
        def calendar = Calendar.getInstance()
        def dateStr
        if(!paramDate){
            dateStr = format.format(calendar.getTime())
        }else{
            calendar.setTime(paramDate)
            dateStr = format.format(calendar.getTime())
        }
        dateStr = dateStr + " " + timePeriod
        def date
        try{
            date = formatWithHM.parse(dateStr)
        }catch(Exception e){
            log.info("AttendanceDataService getWorkTime is error!")
        }
        calendar.setTime(date)
        return  calendar
    }

    /**
     * 根据毫秒数计算出分钟数,保留一位小数
     * @param milliSecond 毫秒
     * @param decimal 四舍五入小数位数
     * @return
     */
    def calculateMinutes(long milliSecond){
        def minutes = milliSecond.intdiv(60 * 1000);
        long seconds = milliSecond.intdiv(1000) % 60;
        def secToMinutes = Math.round(seconds / 60.0) // 秒转为分钟
        minutes += secToMinutes
        return minutes >=0 ? minutes : 0
    }

    /**
     * 根据毫秒数计算出小时数,保留一位小数
     * @param milliSecond 毫秒
     * @param decimal 四舍五入小数位数
     */
    def calculateHours(long milliSecond){
        def hours = milliSecond.intdiv(60 * 60 * 1000);
        def minutes = milliSecond.intdiv(60 * 1000) % 60;
        long seconds = milliSecond.intdiv(1000) % 60;
        def secToMinutes = Math.round(seconds / 60.0) // 秒转为分钟
        minutes += secToMinutes
        def minToHours = Math.round(minutes / 60.0 * 10 ) / 10 // 分钟转为小时，保留decimal位小数
        def totalHours = hours + minToHours
        return totalHours >=0 ? totalHours : 0
    }

    /**
     * 根据小时数计算出天数
     * @param attendanceModel
     * @param hours
     */
    def Double calculateHoursByMinutes(def minutes){
        def hours = 0
        if(minutes && minutes > 0){
            hours += Math.round(minutes / 60 * 10) / 10
        }
        return hours
    }

    /**
     * 根据开始时间和结束时间，加上模板计算出（请假，外出，出差）小时数，排除休息日
     *  注意：此方法是根据开始和结束时间计算的
     * 如：周末加班从08:00-19:00,而正常工作时间是09:00-18:00，则计算从08:00-19:00
     * @param attendanceModel
     * @param startDate
     * @param endDate
     * @return
     */
    def calculateRealHoursByAttendanceModel(AttendanceModel attendanceModel,Date startDate,Date endDate){
        return calculateRealHoursByAttendanceModel(attendanceModel,startDate,endDate,1)
    }
    /**
     * 根据开始时间和结束时间，计算出（请假，加班，外出，出差）小时数
     * 注意：此方法是根据开始和结束时间计算的
     * 如：周末加班从08:00-19:00,而正常工作时间是09:00-18:00，则计算从08:00-19:00
     * @param attendanceModel 考勤模型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param type 0:不排除休息日；1：排除休息日
     */
    def calculateRealHoursByAttendanceModel(AttendanceModel attendanceModel,Date startDate,Date endDate,int type){
        def hours = 0
        if(attendanceModel && startDate && endDate){
            def startDateCal = Calendar.getInstance()
            startDateCal.setTime(startDate)
            def endDateCal  = Calendar.getInstance()
            endDateCal.setTime(endDate)
            if(startDateCal.getTimeInMillis() <= endDateCal.getTimeInMillis()){
                def tempStartDateCal = Calendar.getInstance()
                def tempEndDateCal = Calendar.getInstance()
                def startDateStr = format.format(startDateCal.getTime())
                def endDateStr = format.format(endDateCal.getTime())
                endDateCal.add(Calendar.DATE,1) // 为了避免最后一天漏掉
                // 注意：如果tempDateStr=endDateStr时，处理完之后应该直接跳出循环
                while(startDateCal.getTimeInMillis() < endDateCal.getTimeInMillis()){
                    tempStartDateCal.setTime(startDateCal.getTime())
                    if(type == 1){ // 休息日不统计
                        def isWorkDay = judgeIsWorkDay(attendanceModel,tempStartDateCal.getTime())
                        if(isWorkDay == false){
                            startDateCal.add(Calendar.DATE,1) // 往后加一天
                            continue
                        }
                    }
                    def tempDateStr = format.format(startDateCal.getTimeInMillis())
                    if(tempDateStr == startDateStr || tempDateStr == endDateStr){ // 说明是第一天或最后一天
                       if(tempDateStr == endDateStr){ // 最后一天,结束时间等于实际的结束时间
                            // 因为结束时间加了一天，所以此处要先减掉
                            endDateCal.add(Calendar.DATE,-1)
                            if(startDateStr != endDateStr){ // 跨天
                                def modelStart1Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.startTime1)
                                tempStartDateCal.setTime(modelStart1Cal.getTime())
                            }
                            tempEndDateCal.setTime(endDateCal.getTime())
                        }else{
                            if(attendanceModel.timeMode == 1){
                                def modelEnd1Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.endTime1)
                                tempEndDateCal.setTime(modelEnd1Cal.getTime())
                            }else{
                                def modelEnd2Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.endTime2)
                                tempEndDateCal.setTime(modelEnd2Cal.getTime())
                            }
                        }
                        hours += calculateHours(tempEndDateCal.getTimeInMillis() - tempStartDateCal.getTimeInMillis())
                        // 最后一天，直接跳出
                        if(tempDateStr == endDateStr){
                            break
                        }
                    }else{ // 不是第一天也不是最后一天，则开始时间=考勤模型的开始时间，结束时间=考勤模型的结束时间
                        hours += getWorkTime(attendanceModel,0)
                    }
                    // 往后加一天
                    startDateCal.add(Calendar.DATE,1)
                }
            }
        }
        return hours
    }

    /**
     * 根据开始时间和结束时间，加上模板计算出（请假，外出，出差）小时数，排除休息日
     * @param attendanceModel
     * @param startDate
     * @param endDate
     * @return
     */
    def calculateHoursByAttendanceModel(AttendanceModel attendanceModel,Date startDate,Date endDate){
        return calculateHoursByAttendanceModel(attendanceModel,startDate,endDate,1)
    }
    /**
     * 根据开始时间和结束时间，加上模板计算出（请假，加班，外出，出差）小时数
     * 注意：此方法是根据正常上下班了计算的
     * 如：周末加班从08:00-19:00,而正常工作下班时间是18:00，则计算从09:00-18:00
     * @param attendanceModel 考勤模型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param type 0:不排除休息日；1：排除休息日
     */
    def calculateHoursByAttendanceModel(AttendanceModel attendanceModel,Date startDate,Date endDate,int type){
        def hours = 0
        if(attendanceModel && startDate && endDate){
            def startDateCal = Calendar.getInstance()
            startDateCal.setTime(startDate)
            def endDateCal  = Calendar.getInstance()
            endDateCal.setTime(endDate)
            if(startDateCal.getTimeInMillis() <= endDateCal.getTimeInMillis()){
                def modelStart1Cal
                def modelEnd1Cal
                def modelStart2Cal
                def modelEnd2Cal
                def tempStartDateCal = Calendar.getInstance()
                def tempEndDateCal = Calendar.getInstance()
                def startDateStr = format.format(startDateCal.getTime())
                def endDateStr = format.format(endDateCal.getTime())
                endDateCal.add(Calendar.DATE,1) // 为了避免最后一天漏掉
                // 注意：如果tempDateStr=endDateStr时，处理完之后应该直接跳出循环
                while(startDateCal.getTimeInMillis() < endDateCal.getTimeInMillis()){
                    tempStartDateCal.setTime(startDateCal.getTime())
                    if(type == 1){ // 休息日不统计
                        def isWorkDay = judgeIsWorkDay(attendanceModel,tempStartDateCal.getTime())
                        if(isWorkDay == false) {
                            startDateCal.add(Calendar.DATE,1) // 往后加一天
                            continue
                        }
                    }
                    def tempDateStr = format.format(startDateCal.getTimeInMillis())
                    modelStart1Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.startTime1)
                    modelEnd1Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.endTime1)
                    modelStart2Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.startTime2)
                    modelEnd2Cal = generateNewDateByParamDateAndTimePeriod(startDateCal.getTime(),attendanceModel.endTime2)
                    if(tempDateStr == startDateStr || tempDateStr == endDateStr){ // 说明是第一天或最后一天
                        if(tempDateStr == endDateStr){ // 最后一天,结束时间等于实际的结束时间
                            // 因为结束时间加了一天，所以此处要先减掉
                            endDateCal.add(Calendar.DATE,-1)
                            if(startDateStr != endDateStr){ // 跨天
                                tempStartDateCal.setTime(modelStart1Cal.getTime())
                            }
                            tempEndDateCal.setTime(endDateCal.getTime())
                        }else{
                            if(attendanceModel.timeMode == 1){
                                tempEndDateCal.setTime(modelEnd1Cal.getTime())
                            }else{
                                tempEndDateCal.setTime(modelEnd2Cal.getTime())
                            }
                        }
                        if(attendanceModel.timeMode == 1){
                            // 开始时间必须小于考勤模型的结束时间（因为如果开始时间大于等于考勤模型的结束时间，则说明在工作时间段之外）
                            if(tempStartDateCal.getTimeInMillis() < modelEnd1Cal.getTimeInMillis()){
                                // 如果开始时间小于考勤模型的开始时间，则开始时间=考勤模型的开始时间
                                if(tempStartDateCal.getTimeInMillis() < modelStart1Cal.getTimeInMillis()){
                                    tempStartDateCal = modelStart1Cal
                                }
                                // 如果结束时间大于考勤模型的结束时间，则结束时间=考勤模型的结束时间
                                if(tempEndDateCal.getTimeInMillis() > modelEnd1Cal.getTimeInMillis()){
                                    tempEndDateCal = modelEnd1Cal
                                }
                                hours += calculateHours(tempEndDateCal.getTimeInMillis() - tempStartDateCal.getTimeInMillis())
                            }
                        }else{
                           // 要求开始时间在考勤模型第二段结束时间之前且结束时间在考勤模型第一段时间之后
                            if(tempStartDateCal.getTimeInMillis() < modelEnd2Cal.getTimeInMillis()
                                    && tempEndDateCal.getTimeInMillis() > modelStart1Cal.getTimeInMillis()){
                                if(tempEndDateCal.getTimeInMillis() < modelStart2Cal.getTimeInMillis()){ // 结束时间在考勤模型的第一段
                                    // 结束时间在第一段和第二段之间，则结束时间为考勤模型第一段结束时间
                                    if(tempEndDateCal.getTimeInMillis() > modelEnd1Cal.getTimeInMillis()){
                                        tempEndDateCal = modelEnd1Cal
                                    }
                                    // 开始时间在第一段开始时间之前，则开始时间为考勤模型的第一段开始时间
                                    if(tempStartDateCal.getTimeInMillis() < modelStart1Cal.getTimeInMillis()){
                                        tempStartDateCal = modelStart1Cal
                                    }
                                    hours += calculateHours(tempEndDateCal.getTimeInMillis() - tempStartDateCal.getTimeInMillis())
                                }else{ // 结束时间在第二段
                                    // 结束时间在第二段结束时间之后，则结束时间为考勤模型第二段的结束时间
                                    if(tempEndDateCal.getTimeInMillis() > modelEnd2Cal.getTimeInMillis()){
                                        tempEndDateCal = modelEnd2Cal
                                    }
                                    // 开始时间在第一段开始时间之前，则开始时间为考勤模型的第一段开始时间
                                    if(tempStartDateCal.getTimeInMillis() < modelStart1Cal.getTimeInMillis()){
                                        // 时间 = 第一段的实际工作时间 + （结束时间-第二段开始时间）
                                        hours += getWorkTime(attendanceModel,1)
                                        hours += calculateHours(tempEndDateCal.getTimeInMillis() - modelStart2Cal.getTimeInMillis())
                                    }else if(tempStartDateCal.getTimeInMillis() < modelStart2Cal.getTimeInMillis()){
                                        hours += calculateHours(modelEnd1Cal.getTimeInMillis() - tempStartDateCal.getTimeInMillis())
                                        hours += calculateHours(tempEndDateCal.getTimeInMillis() - modelStart2Cal.getTimeInMillis())
                                    }else if(tempStartDateCal.getTimeInMillis() >= modelEnd2Cal.getTimeInMillis()){
                                        hours += calculateHours(tempEndDateCal.getTimeInMillis() - tempStartDateCal.getTimeInMillis())
                                    }
                                }
                            }
                        }
                        // 最后一天，直接跳出
                        if(tempDateStr == endDateStr){
                            break
                        }
                    }else{ // 不是第一天也不是最后一天，则开始时间=考勤模型的开始时间，结束时间=考勤模型的结束时间
                        hours += getWorkTime(attendanceModel,0)
                    }
                    // 往后加一天
                    startDateCal.add(Calendar.DATE,1)
                }
            }
        }
        return hours
    }

    /**
     * 根据小时数计算出天数
     * @param attendanceModel
     * @param hours
     */
    def calculateDaysByHours(AttendanceModel attendanceModel,def hours){
        def days = 0
        if(attendanceModel){
            def workTime = getWorkTime(attendanceModel,0)
            if(workTime && workTime > 0){
                days += Math.round(hours / workTime * 100) / 100
            }
        }
        return days
    }

    /**
     * 判断给定的日期是否工作日
     * @param judgeDate 判断的日期
     * @param attendanceModel
     * @return true:是工作日；false：不是工作日；null：判断的日期为空，不确定
     */
    def judgeIsWorkDay(AttendanceModel attendanceModel,Date judgeDate){
        def isWorkDay
        if(!judgeDate || !attendanceModel) return isWorkDay
        def judgeDateStr = format.format(judgeDate)
        attendanceModel = AttendanceModel.get(attendanceModel.id)
        def attendanceCalendar = attendanceModel.attendanceCalendar
        if(attendanceCalendar){
            WorkDay.createCriteria().list(){
                eq("attendanceCalendar",attendanceCalendar)
                eq("user",attendanceModel.user)
            }.each {workDay->
                def workDayStr = format.format(workDay.workDate)
                if(workDayStr.equals(judgeDateStr)){
                    if(workDay.kind == 1){ // 休息日
                        isWorkDay = false
                    }else if(workDay.kind == 2){ // 工作日
                        isWorkDay = true
                    }
                }
            }
        }
        if(!isWorkDay){
            Calendar judgeCalendar = Calendar.getInstance()
            judgeCalendar.setTime(judgeDate)
            def week = judgeCalendar.get(Calendar.DAY_OF_WEEK)
            switch (week){
                case Calendar.MONDAY:
                    isWorkDay = attendanceModel.monday
                    break
                case Calendar.TUESDAY:
                    isWorkDay = attendanceModel.tuesday
                    break
                case Calendar.WEDNESDAY:
                    isWorkDay = attendanceModel.wednesday
                    break
                case Calendar.THURSDAY:
                    isWorkDay = attendanceModel.thursday
                    break
                case Calendar.FRIDAY:
                    isWorkDay = attendanceModel.friday
                    break
                case Calendar.SATURDAY:
                    isWorkDay = attendanceModel.saturday
                    break
                case Calendar.SUNDAY:
                    isWorkDay = attendanceModel.sunday
                    break
            }
        }
        return isWorkDay
    }

    /**
     * 判断是否超过规定的签到签退时间
     * 1、
     * 2、如果考勤模型为双时间模型
     * 如果签到和签退的时间段标识为第一段，签到或者签退时间大于第一段时间模型的结束时间，则禁止，
     * 如果签到和签退的时间段标识为第二段，签到或者签退时间小于等于第一段时间模型的结束时间，则禁止
     * @param attendanceModel 考勤模型
     * @param signOrSignOutDate 签到或签退时间
     * @param period 时间段标识, 1：第一段；2: 第二段
     */
    def judgeIsExceedTimeLimit(AttendanceModel attendanceModel,Date signOrSignOutDate,int period){
        def isExceedTimeLimit = false
        def signOrSignOutDateCalendar = Calendar.getInstance()
        signOrSignOutDateCalendar.setTime(signOrSignOutDate)
        def currentCalendar = Calendar.getInstance()
        def dateMap = SysTool.generateStartAndEndDate(currentCalendar.getTime())
        // 当天必须递交当天的，否则说明超过签到签退时间了
        if(signOrSignOutDateCalendar.getTime().compareTo(dateMap.get('startDate')) < 0
                || signOrSignOutDateCalendar.getTime().compareTo(dateMap.get('endDate')) > 0){
            isExceedTimeLimit = true
        }
        if(attendanceModel){
            if(attendanceModel.timeMode == 2){
                def endTime1Calendar = generateNewDateByTimePeriod(attendanceModel.endTime1)
                if(period == 1 && signOrSignOutDateCalendar.getTimeInMillis() > endTime1Calendar.getTimeInMillis()){
                    isExceedTimeLimit = true
                }else if(period == 2 && signOrSignOutDateCalendar.getTimeInMillis() <= endTime1Calendar.getTimeInMillis()){
                    isExceedTimeLimit = true
                }
            }
        }
        return isExceedTimeLimit
    }

    /**
     * 判断是否已经签到或签退过
     * @param emp 员工
     * @param signOrSignOut 签到或签退 1：签到；2：签退
     * @param period 时间段标识 1：第一段；2：第二段
     * @return JSON对象[isSignedOrSignedOut:true/false,tempSignOrSignOutDate:signOrSignOutDate]
     */
    def judgeIsSignedOrSignedOut(Date signOrSignOutDate,int signOrSignOut,int period){
        def isSignedOrSignedOut = false
        def tempSignOrSignOutDate // 签到或签退时间
        def employee = WebUtilTools.session?.employee
        def attendanceModel = AttendanceModel.get(employee.attendanceModel?.id)
        def dateMap = SysTool.generateStartAndEndDate(signOrSignOutDate)
        if(signOrSignOut == 1){
            if(period == 1){ // 第一段签到
                def dataList = AttendanceData.findAllByEmployeeAndSignDateBetween(employee,dateMap.get('startDate'),dateMap.get('endDate'))
                // 数据库里面当天有记录，就说明第一段签到过了，不允许重复签到
                if(dataList && dataList.size() == 1){
                    def timePeriod = getParamDateBelongToWhichTimePeriod(attendanceModel,dataList.get(0)?.signDate)
                    if(timePeriod == 1) isSignedOrSignedOut = true
                }else if(dataList && dataList.size() == 2){
                    isSignedOrSignedOut = true
                }
                if(isSignedOrSignedOut){ // 第一段签到时间
                    tempSignOrSignOutDate = dataList.get(0).signDate?.getTime()
                }
            }else if(period == 2){ // 第二段签到
                // 查询当前员工，签到时间在今天，且签退时间为Null
                def dataList = AttendanceData.findAllByEmployeeAndSignDateBetween(employee,dateMap.get('startDate'),dateMap.get('endDate'))
                if(dataList && dataList.size() == 1){ // 这一条记录有可能是第一段的数据，也有可能是第二段的数据
                    def timePeriod  = getParamDateBelongToWhichTimePeriod(attendanceModel,dataList.get(0)?.signDate)
                    if(timePeriod == 2) isSignedOrSignedOut = true
                }else if(dataList && dataList.size() == 2){
                    isSignedOrSignedOut = true
                }
                if(isSignedOrSignedOut){ // 第二段签到时间
                    if(dataList.size() == 1){
                        tempSignOrSignOutDate = dataList.get(0).signDate?.getTime()
                    }else{
                        tempSignOrSignOutDate = dataList.get(1).signDate?.getTime()
                    }
                }
            }
        }else if(signOrSignOut == 2){
            if(period == 1){ // 第一段签退
                def dataList = AttendanceData.findAllByEmployeeAndSignOutDateBetween(employee,dateMap.get('startDate'),dateMap.get('endDate'))
                // 数据库里面当天有记录，就说明第一段签退过了，不允许重复签退
                if(dataList && dataList.size() == 1){
                    def timePeriod = getParamDateBelongToWhichTimePeriod(attendanceModel,dataList.get(0)?.signOutDate)
                    if(timePeriod == 1) isSignedOrSignedOut = true
                }else if(dataList && dataList.size() == 2){
                    isSignedOrSignedOut = true
                }
                if(isSignedOrSignedOut){ // 第一段签退时间
                    tempSignOrSignOutDate = dataList.get(0).signOutDate?.getTime()
                }
            }else if(period == 2){ // 第二段签退
                // 查询当前员工，签退时间
                def dataList = AttendanceData.findAllByEmployeeAndSignOutDateBetween(employee,dateMap.get('startDate'),dateMap.get('endDate'))
                if(dataList && dataList.size() == 1){ // 这一条记录有可能是第一段的数据，也有可能是第二段的数据
                    def timePeriod  = getParamDateBelongToWhichTimePeriod(attendanceModel,dataList.get(0)?.signOutDate)
                    if(timePeriod == 2) isSignedOrSignedOut = true
                }else if(dataList && dataList.size() == 2){
                    isSignedOrSignedOut = true
                }
                if(isSignedOrSignedOut){ // 第二段签到时间
                    if(dataList.size() == 1){
                        tempSignOrSignOutDate = dataList.get(0).signOutDate?.getTime()
                    }else{
                        tempSignOrSignOutDate = dataList.get(1).signOutDate?.getTime()
                    }
                }
            }
        }
        return [isSignedOrSignedOut:isSignedOrSignedOut,signOrSignOutDate:tempSignOrSignOutDate]
    }

    /**
     * 处理考勤数据里面的异常信息
     * @return
     */
    def handleExceptionAttendanceData(){
        def attendanceDataList = AttendanceData.where {
            or {
                signDate == null
                signOutDate == null
            }
        }.list()
        attendanceDataList.each {attendanceData->
            def attendanceModel = attendanceData.employee?.attendanceModel
            if(!attendanceData.signDate && attendanceData.signOutDate) attendanceData.signDate = attendanceData.signOutDate
            if(attendanceData.signDate && !attendanceData.signOutDate) attendanceData.signOutDate = attendanceData.signDate
            if(attendanceModel){
                def signPeriod = getParamDateBelongToWhichTimePeriod(attendanceModel,attendanceData.signOutDate)
                attendanceData.earlyWorkMinutes = getEarlyWorkMinutes(attendanceModel,attendanceData.signOutDate,signPeriod)
                attendanceData.isEarlyWork = getIsEarlyWork(attendanceModel,attendanceData.earlyWorkMinutes)
                attendanceData.realWorkTime = getRealWorkTime(attendanceModel,attendanceData.signDate,attendanceData.signOutDate,signPeriod)
            }
            attendanceData.save()
        }
    }

    @Transactional
    def statisticsAttendanceData() {
        // 1、先得到今天是几号
        def currentCalendar = Calendar.getInstance()
        def day = currentCalendar.get(Calendar.DAY_OF_MONTH)

        // 1.5、得到统计的开始和结束时间（日期为上个月的开始和结束日期）
        def dateMap = SysTool.getDayOfMonth('LAST_MONTH')
        def startDate = dateMap.get('startDate')
        def endDate = dateMap.get('endDate')

        // 2、查询出今天要统计的模板
        def attendanceModelIds = []
        def queryModel = AttendanceModel.where {
            statDay == day
        }
        queryModel.list()?.each {attendanceModel->
            attendanceModelIds << attendanceModel.id
        }
        // 3、根据模板找到要统计的员工
        def employees = [:]
        def employeeModels = [:]    // 员工对应的考勤模型
        def employeeIds = []
        if(attendanceModelIds && attendanceModelIds.size() > 0){
            def queryEmployee = Employee.where {
                attendanceModel{
                    id in (attendanceModelIds)
                }
                enabled == true
            }
            queryEmployee.list().each {employee->
                def attendanceModelId = employee.attendanceModel?.id
                if(!employees.containsKey(employee.id)){
                    employees << [(employee.id) : new AttendanceStatistics()]
                    employeeIds << employee.id
                    employeeModels << [(employee.id):employee.attendanceModel]
                }
            }
            // 4、统计请假
            def leaveApplyClosure = {
                employee{
                    'in'('id',employeeIds)
                }
                audit{
                    eq('auditState',3)
                }
                between('startDate',startDate,endDate)
            }
            LeaveApply.createCriteria().list(leaveApplyClosure)?.each {leaveApply->
                def empId = leaveApply.employee?.id
                def attendanceStat = employees.get(empId)
                def attendanceModel = employeeModels.get(empId)
                if(attendanceStat){
                    if(!attendanceStat.leaveHours) attendanceStat.leaveHours = 0
                    if(!attendanceStat.leaveDay) attendanceStat.leaveDay = 0
                    attendanceStat.leaveHours += calculateHoursByAttendanceModel(attendanceModel,leaveApply.startDate,leaveApply.endDate)
                    attendanceStat.leaveDay = calculateDaysByHours(attendanceModel,attendanceStat.leaveHours)
                }
                employees << [(empId):attendanceStat]
            }
            // 5、统计外出
            def goOutApplyClosure = {
                employee{
                    'in'('id',employeeIds)
                }
                audit{
                    eq('auditState',3)
                }
                between('startDate',startDate,endDate)
            }
            GoOutApply.createCriteria().list(goOutApplyClosure)?.each {goOutApply->
                def empId = goOutApply.employee?.id
                def attendanceStat = employees.get(empId)
                def attendanceModel = employeeModels.get(empId)
                if(attendanceStat){
                    if(!attendanceStat.goOutHours) attendanceStat.goOutHours = 0
                    attendanceStat.goOutHours += calculateHoursByAttendanceModel(attendanceModel,goOutApply.startDate,goOutApply.endDate)
                }
                employees << [(empId):attendanceStat]
            }
            // 6、统计出差
            def businessTripApplyClosure = {
                employee{
                    'in'('id',employeeIds)
                }
                audit{
                    eq('auditState',3)
                }
                between('startDate',startDate,endDate)
            }
            BusinessTripApply.createCriteria().list(businessTripApplyClosure)?.each {businessTripApply->
                def empId = businessTripApply.employee?.id
                def attendanceStat = employees.get(empId)
                def attendanceModel = employeeModels.get(empId)
                if(attendanceStat){
                    if(!attendanceStat.businessTripDay) attendanceStat.businessTripDay = 0
                    def businessTripHours = calculateHoursByAttendanceModel(attendanceModel,businessTripApply.startDate,businessTripApply.endDate)
                    attendanceStat.businessTripDay += calculateDaysByHours(attendanceModel,businessTripHours)
                }
                employees << [(empId):attendanceStat]
            }
            // 7、统计加班
            def overtimeApplyClosure = {
                employee{
                    'in'('id',employeeIds)
                }
                audit{
                    eq('auditState',3)
                }
                between('startDate',startDate,endDate)
            }
            OvertimeApply.createCriteria().list(overtimeApplyClosure)?.each {overtimeApply->
                def empId = overtimeApply.employee?.id
                def attendanceStat = employees.get(empId)
                def attendanceModel = employeeModels.get(empId)
                if(attendanceStat){
                    if(!attendanceStat.overTimeHours) attendanceStat.overTimeHours = 0
                    if(!attendanceStat.overtimeWorkDay) attendanceStat.overtimeWorkDay = 0
                    attendanceStat.overTimeHours += calculateRealHoursByAttendanceModel(attendanceModel,overtimeApply.startDate,overtimeApply.endDate,0)
                    attendanceStat.overtimeWorkDay += calculateDaysByHours(attendanceModel,attendanceStat.overTimeHours)
                }
                employees << [(empId):attendanceStat]
            }
            // 8、统计考勤数据
            AttendanceData.createCriteria().list {
                between('signDate',startDate,endDate)
                projections {
                    sqlGroupProjection 'employee_id,sign_date, count(*) as work_day,sum(late_minutes) as late_minutes,sum(early_work_minutes) as early_work_minutes',
                            'employee_id,sign_date', ['employee_id','sign_date','work_day','late_minutes','early_work_minutes'],
                            [STRING,DATE, INTEGER,DOUBLE,DOUBLE]
                }
            }.each {ad->
                def empId = ad[0].toLong()
                def attendanceStat = employees.get(empId)
                if(attendanceStat){
                    if(!attendanceStat.workDay) attendanceStat.workDay = 0
                    if(!attendanceStat.lateHours) attendanceStat.lateHours = 0
                    if(!attendanceStat.earlyWorkHours) attendanceStat.earlyWorkHours = 0
                    attendanceStat.workDay = attendanceStat.workDay + ad[2]
                    attendanceStat.lateHours = attendanceStat.lateHours + calculateHoursByMinutes(ad[3])
                    attendanceStat.earlyWorkHours = attendanceStat.earlyWorkHours + calculateHoursByMinutes(ad[4])
                    employees << [(empId):attendanceStat]
                }
            }
            AttendanceData.createCriteria().list {
                between('signDate',startDate,endDate)
                eq('isLate',true)
                projections {
                    groupProperty('employee.id')
                    count('isLate')
                }
            }.each {ad->
                def empId = ad[0].toLong()
                def attendanceStat = employees.get(empId)
                if(attendanceStat){
                    attendanceStat.lateTimes = ad[1]
                    employees << [(empId):attendanceStat]
                }
            }
            AttendanceData.createCriteria().list {
                between('signDate',startDate,endDate)
                eq('isEarlyWork',true)
                projections {
                    groupProperty('employee.id')
                    count('isEarlyWork')
                }
            }.each {ad->
                def empId = ad[0].toLong()
                def attendanceStat = employees.get(empId)
                if(attendanceStat){
                    attendanceStat.earlyWorkTimes = ad[1]
                    employees << [(empId):attendanceStat]
                }
            }
        }
        // 9、汇总数据到考勤汇总表
        currentCalendar.setTime(startDate)
        def yearMonth = new SimpleDateFormat('yyyy-MM').format(currentCalendar.getTime())
        employees.each {key,attendanceStat->
            def employee = Employee.get(key)
            def user = employee?.user
            attendanceStat.employee = employee
            attendanceStat.user = user
            attendanceStat.yearMonth = yearMonth
            def attendanceModel = employeeModels.get(key)
            def allWorkDay = getAllWorkDay(attendanceModel,startDate,endDate)
            attendanceStat.allWorkDay = allWorkDay
            attendanceStat.absenceDay = allWorkDay - (attendanceStat.workDay ? attendanceStat.workDay : 0)
            // 保留一位小数
            if(attendanceStat.lateHours){
                attendanceStat.lateHours = Math.round(attendanceStat.lateHours * 10) / 10
            }
            if(attendanceStat.earlyWorkHours){
                attendanceStat.earlyWorkHours = Math.round(attendanceStat.earlyWorkHours * 10) / 10
            }
            if (attendanceStat.hasErrors()) {
                def json = [success:false,errors: errorsToResponse(attendanceStat.errors)] as JSON
                return
            }

            attendanceStat.save(flush:true)
        }
    }

    /**
     * 没测试
     * @param lng1
     * @param lat1
     * @param attendanceModel
     * @return
     */
    def calculateDistance(Double lng1,Double lat1,AttendanceModel attendanceModel){
        Double radLat1 = rad(lat1);
        Double radLat2 = rad(lat2);
        Double a = radLat1 - radLat2;
        Double b = rad(lng1) - rad(lng2);
        Double s = 2 * Math.Asin(Math.Sqrt(Math.Pow(Math.Sin(a/2),2) +
                Math.Cos(radLat1)*Math.Cos(radLat2)*Math.Pow(Math.Sin(b/2),2)));
        s = s * 6370693.5; // WGS84椭球的长半轴就为6378137.0,百度为6378137.0
        s = Math.Round(s * 10000) / 10000;
        return s;
    }
    private def rad(Double d){
        return d * Math.PI / 180.0;
    }
}
