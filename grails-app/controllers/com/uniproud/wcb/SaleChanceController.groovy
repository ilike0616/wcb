package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class SaleChanceController {
	
	def modelService
	def baseService

    def list(){
        def extraCondition = {
            if(params.specialParam){ // 特殊参数
                customer{
                    eq("id",params.specialParam.toLong())
                }
            }else if(params.customer){
                eq('customer.id',params.customer as Long)
            }
        }
		render modelService.getDataJSON('sale_chance',extraCondition)
    }

    def detailList(){
        if(params.object_id){
            if(params.object_id?.toLong() > 0){
                def emp = session.employee
                def extraCondition = {
                    eq("saleChance.id",params.object_id?.toLong())
                    eq('deleteFlag', false)
                    order("lastUpdated","desc")
                }
                render modelService.getDataJSON('sale_chance_detail',extraCondition,true,true)
            }else{
                render baseService.success(params)
            }
        }else{
            def emp = session.employee
            def extraCondition = {
                order("lastUpdated","desc")
            }
            render modelService.getDataJSON('sale_chance_detail',extraCondition)
        }
    }

    @Transactional
    def insert(SaleChance saleChance) {
		if (saleChance == null) {
			render baseService.error(params)
			return
		}
        if(params.competitors){
            def competitors = params.competitors.split(",")
            saleChance.properties["competitors"] = competitors*.toLong()
        }
        if(saleChance.foreseeMoney){
            if(saleChance.possibility){
                def userField = UserField.findByUserAndFieldName(session.employee.user,'possibility')
                saleChance.properties["possibilityMoney"] = saleChance.foreseeMoney *
                        convertPercentToDecimal(SysTool.getDictLabelValue(userField.dict,saleChance.possibility))
            }else{
                saleChance.properties["possibilityMoney"] = saleChance.foreseeMoney
            }
        }
        saleChance.properties["user"] = session.employee.user
        saleChance.properties["employee"] = session.employee
		baseService.save(params,saleChance)

        if(params.detail != null){
            def detail = JSON.parse(params.detail)
            //遍历明细、保存
            detail.each {
                SaleChanceDetail od = new SaleChanceDetail(it)
                od.properties['user'] = session.employee?.user
                od.properties['employee']= session.employee
                od.properties['owner']= saleChance.owner
                if(it['product.id']){
                    od.product = Product.get(it['product.id']?.toLong())
                }else if(it.product?.id){
                    od.product = Product.get(it.product?.id?.toLong())
                }
                if (od.hasErrors()) {
                    def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                    render baseService.validate(params,json)
                    return
                }
                od.save()
                saleChance.addToDetail(od)
            }
            baseService.save(params,saleChance)
        }
        render baseService.success(params)
    }

    @Transactional
    def update() {
        SaleChance saleChance = SaleChance.get(params.id as Long)
        bindData(saleChance, params,[exclude:'detail'])
        if(params.competitors){
            def competitors = params.competitors.split(",")
            saleChance.properties["competitors"] = competitors*.toLong()
        }else{
            saleChance.properties["competitors"] = null
        }
        if(saleChance.foreseeMoney){
            if(saleChance.possibility){
                def userField = UserField.findByUserAndFieldName(session.employee.user,'possibility')
                saleChance.properties["possibilityMoney"] = saleChance.foreseeMoney *
                        convertPercentToDecimal(SysTool.getDictLabelValue(userField.dict,saleChance.possibility))
            }else{
                saleChance.properties["possibilityMoney"] = saleChance.foreseeMoney
            }
        }
		baseService.save(params,saleChance)

        if(params.detail){
            def detail = JSON.parse(params.detail)
            detail.each {
                def od = null
                if(it.id){
                    od = SaleChanceDetail.get(it.id)
                    od.properties = it
                }else{
                    od = new SaleChanceDetail(it)
                    od.saleChance = saleChance
                }
                od.properties['user'] = session.employee?.user
                od.properties['employee']= session.employee
                od.properties['owner']= saleChance.owner
                if(it['product.id']){
                    od.product = Product.get(it['product.id']?.toLong())
                }else if(it.product?.id){
                    od.product = Product.get(it.product?.id?.toLong())
                }
                if (od.hasErrors()) {
                    log.info od.errors
                    def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                    render baseService.validate(params,json)
                    return
                }
                baseService.save(params,od,'sale_chance_detail')
            }
        }
        if(params.dels){
            def ids = JSON.parse(params.dels)
            if(ids.size()>0)
                SaleChanceDetail.executeUpdate("update SaleChanceDetail set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        }
        render baseService.save(params,saleChance)
    }

    @Transactional
    def close(SaleChance saleChance) {
        saleChance.properties["closeMan"] = session.employee
        saleChance.properties["isClosed"] = true
		baseService.save(params,saleChance)

        // 插入关闭日志表
        def closeCenter = new CloseCenter(saleChance.properties)
        closeCenter.saleChance = saleChance
        closeCenter.closeType = 1 // 商机
        closeCenter.save(flush: true)

        render baseService.success(params)
    }

    @Transactional
    def revertClose(SaleChance saleChance) {
        def revertCloseReason = saleChance.closeReason
        // 反关闭，清空关闭信息
        saleChance.properties["closeMan"] = null
        saleChance.properties["isClosed"] = false
        saleChance.properties["closeReason"] = null
        saleChance.properties["closeResult"] = null
        saleChance.properties["closeDate"] = null
        baseService.save(params,saleChance)

        // 插入关闭日志表
        def closeCenter = new CloseCenter()
        closeCenter.user = session.employee?.user
        closeCenter.owner = session.employee
        closeCenter.closeReason = revertCloseReason
        closeCenter.closeResult = null
        closeCenter.saleChance = saleChance
        closeCenter.isRevertClose = true
        closeCenter.closeType = 1 // 商机
        closeCenter.save(flush: true)

        render baseService.success(params)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        def follow = SaleChanceFollow.createCriteria().list {
            saleChance {
                'in'("id",ids*.toLong())
            }
            eq('deleteFlag',false)
        }
        if(follow.size() > 0){
            render ([success:false,msg:"您要删除的数据已关联跟进记录，不能删除！"] as JSON)
            return
        }
        SaleChance.executeUpdate("update SaleChance set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    /**
     * ת���ٷֱ�ΪС��
     * @param percent �ٷֱȱ�ʾ,�磺70%
     * @return ���ذٷֱȶ�Ӧ��С�����磺70%=0.7
     */
    def convertPercentToDecimal(percent){
        if(!percent){
            return 0
        }
        def num = percent.replace("%","").toDouble()
        def decimal = num / 100.0
        return decimal
    }

    def locusMap(){
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        def fns = Field.where{
            model{ modelClass == Employee.name }
        }.list()?.fieldName
        def extraCondition = {
            if(params.searchValue){
                or {
                    ilike("subject","%$params.searchValue%")
                }
            }
            isNotNull("location")
            isNotNull("longtitude")
            isNotNull("latitude")
        }
        def searchCondition = modelService.getSearchCondition(extraCondition,fns,params,emp,emps,false)
        def result = SaleChance.createCriteria().list(params){
            searchCondition.delegate = delegate
            searchCondition()
        }
        def data = []
        result.each { rs ->
            data << [id:rs.id,subject:rs.subject,location:rs.location,longtitude:rs.longtitude,latitude:rs.latitude]
        }
        def json = [success:true,data:data,total:result.totalCount] as JSON
        render json
    }

}
