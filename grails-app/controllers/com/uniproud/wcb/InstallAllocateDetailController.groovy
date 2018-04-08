package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class InstallAllocateDetailController {

    def baseService
    def modelService

    def list() {
        def emp = session.employee
        def extraCondition = {
            if(params.installOrder){
                    eq('installOrder.id', params.installOrder as Long)
            }else {
                eq('receivedMan', emp)
            }
        }
        render modelService.getDataJSON('install_allocate_detail', extraCondition,true,true)
    }

    @Transactional
    def grab() {
        if(!params.id){
            render baseService.error(params)
            return
        }
        def allocate = InstallAllocateDetail.get(params.id as Long)
        if(!allocate){
            render baseService.error(params)
            return
        }
        def allocateKind = allocate.installOrder?.allocateKind
        if(allocateKind == 2) {//预派模式
            allocate.receivedDate = new Date()
            allocate.isRecved = true
        }else if(allocateKind == 3){//抢接模式
            allocate.receivedDate = new Date()
            allocate.isRecved = true
            allocate.isClose = true
            //修改安装单
            def order = allocate.installOrder
            order.installState = 2  //安装状态：处理中
            order.receivedMan = allocate.receivedMan
            order.receivedDate = new Date()
            order.executeMan = allocate.receivedMan
            baseService.save(params,allocate,'install_order')
            //关闭其他预派单人记录
            order.allocates?.each {
                if(it != allocate) {
                    it.isClose = true
                    it.save()
                }
            }
        }
        render baseService.save(params,allocate)
    }

    @Transactional
    def confirm(){
        if(!params.id){
            render baseService.error(params)
            return
        }
        def allocate = InstallAllocateDetail.get(params.id as Long)
        if(!allocate){
            render baseService.error(params)
            return
        }
        def order = allocate.installOrder
        order.installState = 2  //安装状态：处理中
        order.receivedMan = allocate.receivedMan
        order.receivedDate = new Date()
        order.executeMan = allocate.receivedMan
        baseService.save(params,allocate,'install_order')
        //关闭其他预派单人记录
        order.allocates?.each {
            it.isClose = true
            it.save()
        }
        render baseService.success(params)
    }

    @Transactional
    def delete(InstallAllocateDetail installAllocateDetailInstance) {
        def ids = JSON.parse(params.ids) as List
        InstallOrder.executeUpdate("update InstallAllocateDetail set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

}
