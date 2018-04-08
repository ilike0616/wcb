package wcb


class WeixinTokenJob {
    def weixinService
    static triggers = {
        simple repeatInterval: 5*60*1000l // 每隔5分钟执行一次
    }

    def execute() {
        weixinService.initToken()
    }
}
