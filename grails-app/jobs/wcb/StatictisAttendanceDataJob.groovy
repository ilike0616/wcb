package wcb

/**
 * 统计考勤数据作业
 */
class StatictisAttendanceDataJob {
    def attendanceDataService

    static triggers = {
        cron name: 'statisticsAttendanceData', cronExpression: "0 0 01 * * ?"
    }

    def execute(){
        attendanceDataService.statisticsAttendanceData()
    }
}
