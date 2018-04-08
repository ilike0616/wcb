package wcb

/**
 * Created by like on 2015/9/2.
 */
class SfaEventExecuteJob {
    def sfaService

    static triggers = {
        cron name: 'SfaEventExecuteJobTrigger', cronExpression: "0 0/5 * * * ?"
    }

    def execute(){
        sfaService.pollingSfaEventExecute()
    }
}
