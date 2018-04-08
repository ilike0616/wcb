package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

@Transactional
class SmsRecordService {

    def grailsApplication

    /**
     * 验证是否触发限制
     * @param mobile 手机号
     * @param ip IP地址
     * @return true-未达到上限；false-触发上限，不能再发送
     */
    def checkLimit(mobile, ip) {
        def ipLimit = grailsApplication.config.SMS_IP_LIMIT as Integer
        def mobileLimit = grailsApplication.config.SMS_MOBILE_LIMIT as Integer
        def dateMap = SysTool.generateStartAndEndDate(new Date())
        def ipList = SmsRecord.findAllByIpAndDateCreatedBetween(ip, dateMap.get('startDate'), dateMap.get('endDate'))
//        log.info "IP($ip)已发送次数：${ipList.size()}"
        if (ipList.size() >= ipLimit) {
            return 1
        }
        def mobileList = SmsRecord.findAllByMobileAndDateCreatedBetween(mobile, dateMap.get('startDate'), dateMap.get('endDate'))
//        log.info "手机号($mobile)已发送次数：${mobileList.size()}"
        if (mobileList.size() >= mobileLimit) {
            return 2
        }
        if(mobileList){
            mobileList?.sort {
                it.dateCreated
            }
            def last = mobileList.last()
            if(new Date().getTime() - last.dateCreated.getTime() < 60*1000){
//                log.info "同一手机号一分钟之内已经发送一次"
                return 3
            }
        }
        return 0
    }

    /**
     * 发送验证码
     * @param mobile 手机号
     * @param verify 验证码
     * @return
     */
    def sendSms(mobile, verify) {
        def result = [:]
        def url = grailsApplication.config.SMS_URL
        def account = grailsApplication.config.SMS_ACCOUNT
        def pwd = grailsApplication.config.SMS_PASSWORD
        def tpl = grailsApplication.config.SMS_VERIFY_TEMPLATE
        def content = tpl.replaceAll("【verify】", verify as String)
        def http = new HTTPBuilder(url + "&account=" + account + "&password=" + pwd + "&mobile=" + mobile + "&content=" + content)
        http.request(Method.POST, JSON) {
            requestContentType = ContentType.JSON
//            headers = ["Authorization": "Bearer " + token]
//            body = [account: account, password: pwd, mobile: mobile,content:content]
            response.success = { resp, reader ->
                def text = new XmlSlurper().parseText(reader.text)
                result << [code:(text.code as String) as Integer]
                result << [smsid:(text.smsid as String) as Integer]
                result << [msg:text.msg as String]
            }
            response.failure = { resp, reader ->
                log.info reader.text
            }
        }
        result
    }
    /**
     * 验证码是否正确,根据手机号获取最后一次发送并在有效期内的验证码进行比对
     * @param mobile
     * @param verify
     */
    def checkVerify(mobile, verify) {
        def result = false
        def valid = grailsApplication.config.SMS_VALID_TIME as Integer
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date())
        cal.add(Calendar.MINUTE, -valid)
        Date date = cal.getTime()
        def sms = SmsRecord.createCriteria().list {
            eq('mobile', mobile)
            gt('dateCreated', date)
            maxResults(1)
            order("dateCreated", "desc")
        }
        sms.each {
            if(it.content == verify)
                result = true
        }
        result
    }
}
