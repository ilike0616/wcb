package wcb


class UserPaymentRecordJob {

    def paymentRecordService

    static triggers = {
//        simple name:'simpleTrigger', startDelay:3000, repeatInterval: 30000, repeatCount: 10
        cron name: 'UserPaymentRecordJobTrigger', cronExpression: "0 0 1 * * ?"
    }

    def execute() {
        paymentRecordService.deductMoney()
    }
}
