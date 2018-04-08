package wcb

/**
 * 统计考勤数据作业
 */
class NotifyAttendanceSignOrSignOutJob {
    def attendanceModelService

    static triggers = {
        cron name: 'notifyAttendanceSignOrSignOutJob', cronExpression: "0 * * * * ?"
    }

    def execute(){
        attendanceModelService.notifyAttendanceSignOrSignOutJob()
    }
}
