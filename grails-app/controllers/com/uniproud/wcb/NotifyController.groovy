package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class NotifyController {

    def modelService

    def list() {
        def emp = session.employee
        def extraCondition = {
            if(params.isRead){
                eq('isRead', false)
            }
            // 工作台查询最新消息
            if(params.fromWhere && params.fromWhere == 'workBench'){
                eq('isRead',false)
                eq('state',false)
            }
            eq("employee",emp)
        }
        render modelService.getDataJSON('notify', extraCondition,true,true)
    }

    /**
     * 未读消息数
     * @return
     */
    def getUnreadNum(){
        def emp = session.employee
        def num = Notify.createCriteria().get {
            eq('isRead', false)
            eq("employee",emp)
            projections {
                countDistinct "id"
            }
        }
        render ([success:true,num:num] as JSON)
    }
    /**
     * 置为已读
     */
    @Transactional
    def toRead(){
        def ids = JSON.parse(params.ids) as List
        Notify.executeUpdate("update Notify set isRead=true where id in (:ids)", [ids: ids*.toLong()])
        render([success: true] as JSON)
    }

    /**
     * 置为已处理
     * @return
     */
    @Transactional
    def toState(){
        def ids = JSON.parse(params.ids) as List
        Notify.executeUpdate("update Notify set state=1 where id in (:ids)", [ids: ids*.toLong()])
        render([success: true] as JSON)
    }

    @Transactional
    def delete(){
        def ids = JSON.parse(params.ids) as List
        Notify.executeUpdate("delete Notify where id in (:ids)", [ids: ids*.toLong()])
        render([success: true] as JSON)
    }
}
