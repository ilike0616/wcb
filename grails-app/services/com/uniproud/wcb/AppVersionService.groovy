package com.uniproud.wcb

import grails.transaction.Transactional

@Transactional
class AppVersionService {

    def grailsApplication

    def final static APP_NAME = "35crm"
    def final static ANDROID_SUFFIX = ".apk"
    def final static IOS_SUFFIX = ".ipa"

    /**
     *  新增时保存升级包
     * @param platform Android/IOS
     * @param app 升级包
     */
    @Transactional
    def saveAppPackage(AppVersion appVersion, app) {
        def path = grailsApplication.config.apppath
        def suffix = ""
        switch (appVersion.platform) {
            case 'Android':
                suffix = ANDROID_SUFFIX
                path = "${path}/Android/${appVersion.edition}"
                break
            case 'IOS':
                suffix = IOS_SUFFIX
                path = "${path}/IOS/${appVersion.edition}"
                break
        }
        //如果文件夹不存在，先创建文件夹
        File dir = new File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        //如果文件夹不存在，先创建文件夹,用于存放最新版本的文件夹
        File newFile = new File("$path/new")
        if (!newFile.exists()) {
            newFile.mkdirs()
        }
        String appFileName = APP_NAME
        if(appVersion.edition>=99){
            appFileName = "upcrm"
        }
        //保存文件
        def appName = appFileName + '-' + appVersion.appVersion + suffix
        File file = new File(path, appName)
        //文件名，保存到数据库
        appVersion.appName = appName
        //保存上传的升级包
        app.transferTo(file)
        if(appVersion.platform=='IOS'){
            createPlist(appVersion,path,appFileName)
        }
        def ant = new AntBuilder()
        ant.copy(file:"$path/$appName",tofile:"$path/new/$appFileName$suffix")
    }
    /**
     * 如果是IOS的话，生成对应版本的下载文件，提供可控制版本下载的操作
     */
    def createPlist(appVersion,path,appFileName){
        def file = new File(path, "${appFileName}-${appVersion.appVersion}.plist")
        file.write("""
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
            <plist version="1.0">
            <dict>
            <key>items</key>
            <array>
            <dict>
            <key>assets</key>
            <array>
            <dict>
            <key>kind</key>
            <string>software-package</string>
            <key>url</key>
            <string>https://cloud.35crm.com/store/IOS/${appVersion.edition}/${appFileName}-${appVersion.appVersion}.ipa</string>
            </dict>
            </array>
            <key>metadata</key>
            <dict>
            <key>subtitle</key>
            <string>App Subtitle</string>
            <key>title</key>
            <string>\u001A\u001A\u001A</string>
            <key>bundle-version</key>
            <string>1.0.0</string>
            <key>kind</key>
            <string>software</string>
            <key>bundle-identifier</key>
            <string>${appVersion.edition>=99?"com.uniproud.ioscrm":"com.uniproud.wcb"}</string>
            </dict>
            </dict>
            </array>
            </dict>
            </plist>
        """)

    }
    /**
     * 修改时替换升级包
     * @param platform
     * @param app
     */
    def updateAppPackage(platform, app) {
        def path = grailsApplication.config.apppath
        def suffix = ""
        switch (platform) {
            case 'Android':
                suffix = ANDROID_SUFFIX
                break
            case 'IOS':
                suffix = IOS_SUFFIX
                break
        }
        File file = new File(path, APP_NAME + suffix)
        if (file.exists())
            file.delete()
        app.transferTo(file)
    }
}
