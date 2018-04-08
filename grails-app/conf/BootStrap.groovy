class BootStrap {
    def initService

    def grailsApplication
    def annotationService
    def init = { servletContext ->
        //初始化系统数据模型
        initService.init()
        environments {
            def start = Calendar.instance.timeInMillis
            log.info "annotation初始化......"
            start = Calendar.instance.timeInMillis
            annotationService.init()
            def end  = Calendar.instance.timeInMillis
            def s = end - start
            log.info "annotation初始化完成,耗时:${s/1000}秒"
            development {
                log.info('设置默认时区为：GMT+8')
                // 设置默认时区
                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))
                log.info "初始化系统数据模型中....."
                start = Calendar.instance.timeInMillis
                end = Calendar.instance.timeInMillis
                s = end - start
                log.info "系统数据模型初始化完毕,耗时:${s / 1000}秒"
                if (grailsApplication.config.dataSource.dbCreate == 'create-drop'){
                    println "运行时环境为 dev,数据库为 create－drop,demo数据初始化中......"
                    start = Calendar.instance.timeInMillis
                    initService.initDev()
                    end = Calendar.instance.timeInMillis
                    s = end - start
                    println "运行时环境为 dev,数据库为 create－drop,demo数据初始化完成,耗时:${s / 1000}秒"
                }
                if (grailsApplication.config.dataSource.dbCreate == 'update'){
//                    初始化系统数据模型
                    start = Calendar.instance.timeInMillis
                    initService.initAllUserModuleVer()//初始化所有企业模板的版本信息
                    end = Calendar.instance.timeInMillis
                    s = end - start
                    log.info "初始化所有企业模板的版本信息完成，耗时:${s / 1000}秒"
                    start = Calendar.instance.timeInMillis
                    initService.initVerDate()
                    end = Calendar.instance.timeInMillis
                    s = end - start
                    log.info "初始化新模块新功能完成，耗时:${s / 1000}秒"
                }
            }
            production {
                println "运行时环境为 prod"
                log.info('设置默认时区为：GMT+8')
                // 设置默认时区
                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))
                log.info "初始化系统数据模型中....."
                start = Calendar.instance.timeInMillis
                end  = Calendar.instance.timeInMillis
                s = end - start
                log.info "系统数据模型初始化完毕,耗时:${s/1000}秒"
                if (grailsApplication.config.dataSource.dbCreate == 'create-drop'){
                    println "运行时环境为 prod,数据库为 create－drop,demo数据初始化中......"
                    start = Calendar.instance.timeInMillis
                    initService.initDev()
                    end = Calendar.instance.timeInMillis
                    s = end - start
                    println "运行时环境为 prod,数据库为 create－drop,demo数据初始化完成,耗时:${s / 1000}秒"
                }
                if (grailsApplication.config.dataSource.dbCreate == 'update'){
                    initService.initAllUserModuleVer()//初始化所有企业模板的版本信息
                    initService.initVerDate()
                }
            }

        }
    }
    def destroy = {

    }
}
