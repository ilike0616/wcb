package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class ContractOrderController {

	def modelService
	def baseService

	def list(){
		def emp = session.employee
		def extraCondition = {
			if(params.specialParam){
				eq("customer.id",params.specialParam.toLong())
			}
			if(params.customer){
				eq('customer.id',params.customer as Long)
			}
			if(params.specialSelect == "income"){
				'in'("paidState",[1,2])	//未收款、部分收款
			}
			if(params.specialSelect == "invoice"){
				'in'("invoiceState",[1,2])	//未开票、部分开票
			}
			if(params.saleChance){
				eq('saleChance.id',params.saleChance as Long)
			}
		}
		render modelService.getDataJSON('contract_order',extraCondition)
	}

	def detailList(){
		if(params.object_id){
			if(params.object_id?.toLong() > 0){
				def emp = session.employee
				def orderIdOrSaleChanceId = "contractOrder.id"
				def moduleId = "contract_order_detail"
				if(params.chanceToOrder && params.chanceToOrder.toBoolean() == true){ // 商机转订单
					orderIdOrSaleChanceId = "saleChance.id"
					moduleId = "sale_chance_detail"
				}
				def extraCondition = {
					eq("$orderIdOrSaleChanceId",params.object_id?.toLong())
					eq('deleteFlag', false)
					order("lastUpdated","desc")
				}
				render modelService.getDataJSON("$moduleId",extraCondition,true,true)
			}else{
				render baseService.success(params)
			}
		}else{
			def emp = session.employee
			def extraCondition = {
				order("lastUpdated","desc")
			}
			render modelService.getDataJSON('contract_order_detail',extraCondition)
		}
	}

	@Transactional
	def insert(ContractOrder contractOrder){
		if (contractOrder == null) {
			render baseService.error(params)
			return
		}
		contractOrder.properties['user'] = session.employee?.user
		contractOrder.properties['employee']= session.employee
		if (contractOrder.hasErrors()) {
			def json = [success:false,errors: errorsToResponse(contractOrder.errors)] as JSON
			render baseService.validate(params,json)
			return
		}
		baseService.save(params,contractOrder)
		// 商机转订单时，修改状态为成功、可能性为最后一个，阶段为最后一个阶段
		if(params.saleChance){
			def saleChance = SaleChance.get(params.saleChance.toLong())
			saleChance.properties['chanceState'] = 2
			saleChance.properties['possibility'] = getDictLastValue('sale_chance','possibility',session.employee?.user)
			saleChance.properties['followPhase'] = getDictLastValue('sale_chance','followPhase',session.employee?.user)
			saleChance.save(flush: true)
		}
		if(params.detail != null){
			def detail = JSON.parse(params.detail)
			//遍历明细、保存
			detail.each {
				ContractOrderDetail od = new ContractOrderDetail()
				bindData(od,it,[exclude:'id']) // 有可能从商机过来，id值存在，所以排除掉id
				if(params.saleChance){ // 商机转订单
					od.properties["saleChance"] = contractOrder.saleChance
					od.properties["saleChanceDetail"] = it.id
				}
				od.properties['user'] = session.employee?.user
				od.properties['employee']= session.employee
				od.properties['owner']= contractOrder.owner
				if(it['product.id']){
					od.product = Product.get(it['product.id']?.toLong())
				}else if(it.product?.id){
					od.product = Product.get(it.product?.id?.toLong())
				}
				if (od.hasErrors()) {
					println od.errors
					def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
					render baseService.validate(params,json)
					return
				}
				od.save()
				contractOrder.addToDetail(od)
			}
			baseService.save(params,contractOrder)
		}
		render baseService.success(params)
	}

	@Transactional
	def update() {
		ContractOrder contractOrder = ContractOrder.get(params.id as Long)
		if(params.detail){
			def detail = JSON.parse(params.detail)
			detail.each {
				if(it.id){
					ContractOrderDetail od = ContractOrderDetail.get(it.id)
					od.properties = it
					od.properties['user'] = session.employee?.user
					od.properties['employee']= session.employee
					od.properties['owner']= contractOrder.owner
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
					baseService.save(params,od,'contract_order_detail')
				}else{
					ContractOrderDetail od = new ContractOrderDetail(it)
					od.properties['user'] = session.employee?.user
					od.properties['employee']= session.employee
					od.properties['owner']= contractOrder.owner
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
					od.contractOrder = contractOrder
					baseService.save(params,od,'contract_order_detail')
				}
			}
		}
		if(params.dels){
			def ids = JSON.parse(params.dels)
			if(ids.size()>0)
				ContractOrderDetail.executeUpdate("update ContractOrderDetail set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		}
		bindData(contractOrder, params,[exclude:'detail'])
		render baseService.save(params,contractOrder)
	}

	@Transactional
	def delete() {
		def ids = JSON.parse(params.ids) as List
		def income = FinanceIncomeExpense.createCriteria().list {
			contractOrder {
				'in'("id",ids*.toLong())
			}
			eq('deleteFlag',false)
		}
		if(income.size() > 0){
			render ([success:false,msg:"您要删除的数据已关联财务出入账记录，不能删除！"] as JSON)
			return
		}
		def invoice = Invoice.createCriteria().list {
			contractOrder {
				'in'("id",ids*.toLong())
			}
			eq('deleteFlag',false)
		}
		if(invoice.size() > 0){
			render ([success:false,msg:"您要删除的数据已关联财务出入账记录，不能删除！"] as JSON)
			return
		}
		def fareClaim = FareClaims.createCriteria().list {
			contractOrder {
				'in'("id",ids*.toLong())
			}
			eq('deleteFlag',false)
		}
		if(fareClaim.size() > 0){
			render ([success:false,msg:"您要删除的数据已关联费用报销记录，不能删除！"] as JSON)
			return
		}
		ContractOrder.createCriteria().list {
			'in'("id",ids*.toLong())
		}?.each {
			it.deleteFlag = true
			it.save(flush: true)
		}
		ContractOrderDetail.executeUpdate("update ContractOrderDetail set deleteFlag=true where contractOrder.id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
	}

	/**
	 * 获取fieldName对应的数据字典的长度
	 * @param moduleId
	 * @param fieldName
	 * @param user
	 * @return
	 */
	def getDictLastValue(moduleId,fieldName,user){
		def result = null
		if(moduleId && fieldName){
			Module m = Module.findByModuleId(moduleId)
			def userField = UserField.findByFieldNameAndModuleAndUser(fieldName,m,user)
			def dict = userField?.dict
			if(dict){
				def maxItemId = DataDictItem.executeQuery("select max(itemId) from DataDictItem where dict=:dict",[dict:dict])
				result = maxItemId[0] ? maxItemId[0] : null
			}
		}
		return result
	}
}
