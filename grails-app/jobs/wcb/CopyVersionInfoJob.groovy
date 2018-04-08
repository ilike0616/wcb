package wcb

/**
 * 拷贝版本信息
 */
class CopyVersionInfoJob {
    def userService
    def concurrent = false
    def execute(context){
        def id = context.mergedJobDataMap.get("id")
        if(id){
            userService.copyVersionInfo(id.toLong())
        }
    }
}
