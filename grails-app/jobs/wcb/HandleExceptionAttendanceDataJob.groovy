package wcb

class HandleExceptionAttendanceDataJob {
    def attendanceDataService

    static triggers = {
        cron name: 'handleExceptionAttendanceData', cronExpression: "0 0 23 * * ?"
    }

    def execute(){
        attendanceDataService.handleExceptionAttendanceData()
    }
}
