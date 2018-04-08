package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class AuditController {

	def auditService
	def modelService
	def userService
	def baseService

	def list() {
		def emp = session.employee
//		def emps = modelService.getEmployeeChildrens(emp.id)
//		emps << emp
//		emps.unique()
		def extraCondition = {
            if(params.type){
                eq("type",params.type.toInteger())
            }
			if(params.auditType == '1'){//待审
				//待审核或审核中的且当前审核人是emp
//				if (emps) {
//					inList('auditor', emps)
//				}
				eq('auditor.id',emp.id)
				inList("auditState",[1, 2])
			}else if(params.auditType == '2'){//已审
				//已审核的人中有emp
				auditors {
					eq("id",emp.id)
//					inList("id", emps.id)
				}
			}else if(params.auditType =='3'){//全部
				def ops = AuditOpinion.findAllByUserAndAuditorAndDeleteFlag(emp.user,emp,false)
				if(ops.size() > 0) {
					inList("id", ops*.audit*.id)
				}else{
					eq("id",0)
				}
			}
		}
		def json = modelService.getDataJSON('work_audit',extraCondition,true,true)
		render json
	}

	def listOpinion(){
		def extraCondition = {
			if(params.audit){
				eq("audit.id",params.audit?.toLong())
				//ne("state",1)
			}
			order("dateCreated", "asc")
		}
		render modelService.getDataJSON('audit_opinion',extraCondition,true,true)
	}

	def viewQz(){
		def audit = Audit.get(params.id as Long)
		def time = new Date().getTime()
		if(audit.qz == 'goout'){
			redirect(controller: "goOutApply", action: "list", id: audit.goout?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'trip'){
			redirect(controller: "businessTripApply", action: "list", id: audit.trip?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'leave'){
			redirect(controller: "leaveApply", action: "list", id: audit.leave?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'overtime'){
			redirect(controller: "overtimeApply", action: "list", id: audit.overtime?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'fareClaims'){
			redirect(controller: "fareClaims", action: "list", id: audit.fareClaims?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'income'){
			redirect(controller: "financeIncome", action: "list", id: audit.income?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'expense'){
			redirect(controller: "financeExpense", action: "list", id: audit.expense?.id, params: [isExcludeEmp:true,dc:time])
		}else if(audit.qz == 'invoice'){
			redirect(controller: "invoice", action: "list", id: audit.invoice?.id, params: [isExcludeEmp:true,dc:time])
		}
	}

	/**
	 * 审核
	 */
	@Transactional
	def audit(AuditOpinion opinion){
		if (opinion.hasErrors()) {
			println opinion.errors
			def json = [success:false,errors: errorsToResponse(opinion.errors)] as JSON
			render baseService.error(params,json)
			return
		}
		if(!auditService.checkBeforeAudit(opinion)){
			render aduitForbidden
			return
		}
		render auditService.audit(params,opinion)
	}

}
