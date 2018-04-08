package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class PaymentRecordController {

    def modelService
    def paymentRecordService

    def list() {
        def extraCondition = {
        }
        render modelService.getDataJSON('payment_record', extraCondition)
    }
    @AdminAuthAnnotation
    def listForAdmin() {
        RequestUtil.pageParamsConvert(params)
        def data = []
        PaymentRecord.list(sort: "dateCreated", order: "desc").each {
            data << [user: [id: it.user?.id, name: it.user?.name], amountFee: it.amountFee, preBalance: it.preBalance,
                     postBalance: it.postBalance, syNum: it.syNum, kfNum: it.kfNum, dateCreated: it.dateCreated,lastUpdated:it.lastUpdated]
        }
        def json = [success: true, data: data, total: data.size()] as JSON
        if (params.callback) {
            render "${params.callback}($json)"
        } else {
            render(json)
        }
    }

}
