package com.uniproud.wcb

import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodecFactory
import org.hibernate.criterion.CriteriaSpecification

@UserAuthAnnotation
@Transactional(readOnly = true)
class ShareController {
	
	def modelService
	def baseService
	
    def list(){
        def emp = Employee.get(session.employee?.id)
        def extraCondition = {
        }
		render modelService.getDataJSON('share',extraCondition,true,true)
    }

    def detailList(){
        RequestUtil.pageParamsConvert(params)
        def query = ShareDetail.where{
            if(params.shareId){
                eq("share.id",params.shareId.toLong())
            }
        }
        def data = []
        query.list(params).each {
            data << [id:it.id,employee:it.employee?.id,employeeName:it.employee?.name,shareTo:it.shareTo,
                            dateCreated:it.dateCreated,lastUpdated:it.lastUpdated]
        }
        def json = [success:true,data:data, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    @Transactional
    def insert(Share share) {
		if (share == null) {
			render baseService.error(params)
			return
		}
        def employee = session.employee
        share.properties["user"] = employee.user
        share.properties["employee"] = employee

        render baseService.save(params,share)
    }

    @Transactional
    def update(Share share) {
        render baseService.save(params,share)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        def shareList = Share.findAllByIdInList(ids)
        shareList.each {
            it.delete()
        }
		render baseService.success(params)
    }

    def initShare(){
        if(params.id){
            def share = Share.get(params.id.toLong())
            if(share.urlType == 2){
                def url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/share/index/${params.id}"
                share.url = url
            }
            def map = [share: share]
            render(view: "share", model: map)
        }
    }

    @Transactional
    def share() {
        if (!params.shareId || !params.shareTo) {
            render([success: false, msg: '分享失败！'] as JSON)
            return
        }
        def emp = session.employee
        def share = Share.get(params.shareId.toLong())
        def shareDetail = new ShareDetail(user: emp.user, employee: emp, shareTo: params.shareTo)
        share.addToShareDetails(shareDetail)
        share.save(flush: true)
        render([success: true] as JSON)
    }

    @Transactional
    @NormalAuthAnnotation
    def index(){
        if(params.id) {
            def share = Share.get(params.id.toLong())
            // 查看，次数加1
            share.readTimes = share.readTimes + 1
            share.save(flush: true)
            def map = [share: share]
            render(view: "shareTemplate", model: map)
        }
    }
}
