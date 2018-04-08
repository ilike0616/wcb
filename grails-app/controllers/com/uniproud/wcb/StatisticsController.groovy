package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*

@Transactional(readOnly = true)
@UserAuthAnnotation
class StatisticsController {
    def modelService
    def static CUSTOMER_NUM = 'CUSTOMER_NUM'
    def static CONTRACT_ORDER_PAYMENT = 'CONTRACT_ORDER_PAYMENT'
    def static CONTRACT_ORDER_MONEY = 'CONTRACT_ORDER_MONEY'

    def customerYearStat() {
        def json = statEmpYearInfo(CUSTOMER_NUM)
        if(WebUtilTools.params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    /**
     * 根据statType进行订单统计
     * @return
     */
    def contractOrderYearStat() {
        def json = statEmpYearInfo(params.statType)
        if(WebUtilTools.params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    /**
     * 统计员工年度信息
     * @param statType
     * @return
     */
    def statEmpYearInfo(statType){
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        def empIds = []
        emps.each {e->
            empIds << e.id
        }
        def empYearMonth = [:]
        if(statType == CUSTOMER_NUM){
            empYearMonth = statEmpYearMonths(false,emp,emps)
        }else if(statType == CONTRACT_ORDER_PAYMENT){
            empYearMonth = statContractOrderPaymentYearMonths(false,emp,emps)
        }else if(statType == CONTRACT_ORDER_MONEY){
            empYearMonth = statContractOrderMoneyYearMonths(false,emp,emps)
        }
        def employees = []
        def yearMonths = generateYearMonth(params.year)
        Employee.createCriteria().list(){
            or {
                eq("id", emp.id)
                if (emps) {
                    inList('id', empIds)
                }
            }
            eq("deleteFlag",false)
        }.each{e->
            def employee = [employee:e.id,employeeName:e.name,detp:e.dept?.id,deptName:e.dept?.name]
            yearMonths.eachWithIndex {ym,m->
                def key = "${e.id}_${ym}"
                def value = empYearMonth.get("$key")
                if(!value) value = 0
                def enMonth = DateUtil.getEnglishMonth(m)
                employee << ["$enMonth":value]
            }
            employees << employee
        }
        return (employees as JSON)
    }

    /**
     * 统计年度信息（柱状图）
     * @return
     */
    def statYearStatColumnChart() {
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        def empYearMonth = [:]
        if(params.statType == CUSTOMER_NUM){
            empYearMonth = statEmpYearMonths(true,emp,emps)
        }else if(params.statType == CONTRACT_ORDER_PAYMENT){
            empYearMonth = statContractOrderPaymentYearMonths(true,emp,emps)
        }else if(params.statType == CONTRACT_ORDER_MONEY){
            empYearMonth = statContractOrderMoneyYearMonths(true,emp,emps)
        }
        def items = []
        def yearMonths = generateYearMonth(params.year)
        yearMonths.eachWithIndex{ym, m ->
            def count = 0
            empYearMonth.each {key,val ->
                if(ym == key){
                    count += val
                }
            }
            items << [data:count,name:"$ym"]
        }
        def json = [success:true,data:items] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    /**
     * 统计员工年度信息，是否根据月份统计
     * @param byMonth
     * @param emp
     * @param emps
     * @return
     */
    def statEmpYearMonths(byMonth,emp,emps){
        def dateMap = SysTool.getDayOfYear("THIS_YEAR")
        def empYearMonth = [:]
        def fmt = new SimpleDateFormat("yy-MM")
        Customer.createCriteria().list(){
            or {
                eq("owner", emp)
                if (emps) {
                    inList('owner', emps)
                }
            }
            eq("customerType", 2)
            eq("deleteFlag",false)
            between("dateCreated",dateMap.get("startDate"),dateMap.get("endDate"))
        }.each {customer->
            def key = customer.employee.id + "_" + fmt.format(customer.dateCreated)
            if(byMonth){
                key = fmt.format(customer.dateCreated)
            }
            if(empYearMonth.containsKey("$key")){
                empYearMonth << ["$key":empYearMonth.get("$key") + 1]
            }else{
                empYearMonth << ["$key":1]
            }
        }
        return empYearMonth
    }

    def statContractOrderPaymentYearMonths(byMonth,emp,emps){
        def dateMap = DateUtil.getStartAndEndDateByYear(params.year)
        def orderPaymentYearMonth = [:]
        def fmt = new SimpleDateFormat("yy-MM")
        ContractOrder.createCriteria().list(){
            or {
                eq("owner", emp)
                if (emps) {
                    inList('owner', emps)
                }
            }
            eq("deleteFlag",false)
            between("dateCreated",dateMap.get("startDate"),dateMap.get("endDate"))
        }.each {contractOrder->
            def paidMoney = contractOrder.paidMoney==null ? 0 : contractOrder.paidMoney
            def key = contractOrder.employee?.id + "_" + fmt.format(contractOrder.dateCreated)
            if(byMonth){
                key = fmt.format(contractOrder.dateCreated)
            }
            if(orderPaymentYearMonth.containsKey("$key")){
                orderPaymentYearMonth << ["$key":orderPaymentYearMonth.get("$key") + paidMoney]
            }else{
                orderPaymentYearMonth << ["$key":paidMoney]
            }
        }
        return orderPaymentYearMonth
    }

    def statContractOrderMoneyYearMonths(byMonth,emp,emps){
        def dateMap = DateUtil.getStartAndEndDateByYear(params.year)
        def orderPaymentYearMonth = [:]
        def fmt = new SimpleDateFormat("yy-MM")
        ContractOrder.createCriteria().list(){
            or {
                eq("owner", emp)
                if (emps) {
                    inList('owner', emps)
                }
            }
            eq("deleteFlag",false)
            between("dateCreated",dateMap.get("startDate"),dateMap.get("endDate"))
        }.each {contractOrder->
            def discountMoney = contractOrder.discountMoney==null ? 0 : contractOrder.discountMoney
            def key = contractOrder.employee?.id + "_" + fmt.format(contractOrder.dateCreated)
            if(byMonth){
                key = fmt.format(contractOrder.dateCreated)
            }
            if(orderPaymentYearMonth.containsKey("$key")){
                orderPaymentYearMonth << ["$key":orderPaymentYearMonth.get("$key") + discountMoney]
            }else{
                orderPaymentYearMonth << ["$key":discountMoney]
            }
        }
        return orderPaymentYearMonth
    }

    /**
     * ����1-12���µ�����
     * @param year
     * @return
     */
    def generateYearMonth(year){
        def fmt = new SimpleDateFormat('yy-MM')
        def currentCal = Calendar.getInstance()
        if(year){
            currentCal.set(Calendar.YEAR,year.toInteger())
        }
        def yearMonths = []
        for(i in Calendar.JANUARY..Calendar.DECEMBER){
            currentCal.set(Calendar.MONTH,i)
            yearMonths << fmt.format(currentCal.getTime())
        }
        return yearMonths
    }
}
