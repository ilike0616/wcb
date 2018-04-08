package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*

@UserAuthAnnotation
@Transactional(readOnly = true)
class SaleAimController {
    def modelService
    def list(){
        def employee = session.employee

        def depts = []
        if(params.dept){
            def e = Dept.get(params.dept.toLong())
            depts.push(e.id)
            depts.addAll(modelService.getChildrenIds(e.children))
        }

        def employeeIds = []
        def tempEmp = Employee.get(employee.id)
        employeeIds.push(tempEmp.id)
        employeeIds.addAll(modelService.getChildrenIds(tempEmp.children))

        def deptEmp = [:]
        // 是否需要上下级
        Employee.createCriteria().list(){
            eq("user",employee.user)
            eq("isSalesMan",true)
            eq("enabled",true)
            inList('id',employeeIds)
            if(depts){
                inList("dept.id",depts)
            }
        }.each {emp->
            if(deptEmp.containsKey(emp.dept)){
                deptEmp << [(emp.dept):deptEmp.get(emp.dept) << emp]
            }else{
                deptEmp << [(emp.dept):[emp]]
            }
        }
        def data = []
        deptEmp.each {dept,emps->
            def empData = [:]
            def children = []
            // 员工
            emps.each {emp->
                empData = [:]
                empData << [owner:emp.id,ownerName:emp.name,dept:emp.dept?.id,deptName:emp.dept?.name,aimType:1,leaf:true]
                fillEmpData(emp,empData,1)
                children << empData
            }
            // 部门
            empData = [:]
            def deptM = dept.deptManager
            empData << [owner:deptM?.id,ownerName:"$dept.name(${deptM?.name})",dept:dept.id,deptName:dept.name,aimType:2,
                        leaf:false,children:children,"expanded":true]
            fillEmpData(deptM,empData,2)
            data << empData
        }
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(data as JSON)
        }
    }

    def fillEmpData(owner,empData,aimType){
        def format = new SimpleDateFormat('yyyy-MM')
        def currentCal = Calendar.getInstance()
        if(params.aimYear){
            currentCal.set(Calendar.YEAR,WebUtilTools.params.aimYear?.toInteger())
        }
        def aimOrderMoneySum = 0
        def aimCustomerSum = 0
        def aimPayMoneySum = 0
        def aimOrderProfitSum = 0
        def aimCustomerFollowSum = 0
        (0..<12).each {
            def num = it + 1
            currentCal.set(Calendar.MONTH,it)
            currentCal.set(Calendar.DATE,1)
            def aimYearMonth = format.format(currentCal.getTime())
            def saleAim = SaleAim.findByOwnerAndAimYearMonthAndAimType(owner,aimYearMonth,aimType)
            def aimOrderMoney = saleAim?.aimOrderMoney ? saleAim.aimOrderMoney : 0
            aimOrderMoneySum += aimOrderMoney
            def aimCustomer = saleAim?.aimCustomer ? saleAim.aimCustomer : 0
            aimCustomerSum += aimCustomer
            def aimPayMoney = saleAim?.aimPayMoney ? saleAim.aimPayMoney : 0
            aimPayMoneySum += aimPayMoney
            def aimOrderProfit = saleAim?.aimOrderProfit ? saleAim.aimOrderProfit : 0
            aimOrderProfitSum += aimOrderProfit
            def aimCustomerFollow = saleAim?.aimCustomerFollow ? saleAim.aimCustomerFollow : 0
            aimCustomerFollowSum += aimCustomerFollow
            empData << [aimYearMonth:aimYearMonth,"aimOrderMoney_$num":aimOrderMoney,"aimCustomer_$num":aimCustomer,
                        "aimPayMoney_$num":aimPayMoney,"aimOrderProfit_$num":aimOrderProfit,"aimCustomerFollow_$num":aimCustomerFollow]
        }
        empData << [aimOrderMoney_sum:aimOrderMoneySum,aimCustomer_sum:aimCustomerSum,aimPayMoney_sum:aimPayMoneySum
                    ,aimOrderProfit_sum:aimOrderProfitSum,aimCustomerFollow_sum:aimCustomerFollowSum]
    }

    def setList(){
        def format = new SimpleDateFormat('yyyy-MM')
        def currentCal = Calendar.getInstance()
        currentCal.set(Calendar.YEAR,params.aimYear?.toInteger())
        def owner = Employee.get(params.owner?.toLong())
        def aimType = params.aimType?.toInteger()
        def aimOrderMoney = [aimKind:'aimOrderMoney',aimKindName: '销售额']
        def aimOrderMoneySum = 0
        def aimCustomer = [aimKind:'aimCustomer',aimKindName: '客户数']
        def aimCustomerSum = 0
        def aimPayMoney = [aimKind:'aimPayMoney',aimKindName: '回款额']
        def aimPayMoneySum = 0
        def aimOrderProfit = [aimKind:'aimOrderProfit',aimKindName: '销售毛利']
        def aimOrderProfitSum = 0
        def aimCustomerFollow = [aimKind:'aimCustomerFollow',aimKindName: '客户跟进数']
        def aimCustomerFollowSum = 0
        (0..<12).each {
            def num = it + 1
            currentCal.set(Calendar.MONTH,it)
            currentCal.set(Calendar.DATE,1)
            def aimYearMonth = format.format(currentCal.getTime())
            def saleAim = SaleAim.findByUserAndOwnerAndAimYearMonthAndAimType(owner.user,owner,aimYearMonth,aimType)
            def aom = saleAim?.aimOrderMoney ? saleAim.aimOrderMoney : 0
            def ac = saleAim?.aimCustomer ? saleAim.aimCustomer : 0
            def apm = saleAim?.aimPayMoney ? saleAim.aimPayMoney : 0
            def aop = saleAim?.aimOrderProfit ? saleAim.aimOrderProfit : 0
            def acf = saleAim?.aimCustomerFollow ? saleAim.aimCustomerFollow : 0
            aimOrderMoney << [aimYearMonth: aimYearMonth,"aim_$num": aom]
            aimOrderMoneySum += aom
            aimCustomer << [aimYearMonth: aimYearMonth,"aim_$num": ac]
            aimCustomerSum += ac
            aimPayMoney << [aimYearMonth: aimYearMonth,"aim_$num": apm]
            aimPayMoneySum += apm
            aimOrderProfit << [aimYearMonth: aimYearMonth,"aim_$num": aop]
            aimOrderProfitSum += aop
            aimCustomerFollow << [aimYearMonth: aimYearMonth,"aim_$num": acf]
            aimCustomerFollowSum += acf
        }
        aimOrderMoney << [aim_sum:aimOrderMoneySum]
        aimCustomer << [aim_sum:aimCustomerSum]
        aimPayMoney << [aim_sum:aimPayMoneySum]
        aimOrderProfit << [aim_sum:aimOrderProfit]
        aimCustomerFollow << [aim_sum:aimCustomerFollowSum]
        def data = [aimOrderMoney,aimCustomer,aimPayMoney]
        if(1==2){
            data << [aimOrderProfit,aimCustomerFollow]
        }
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render([success:true,data:data] as JSON)
        }
    }

    @Transactional
    def update() {
        def otherParams = JSON.parse(params.otherParams)
        if(!otherParams.owner || !otherParams.aimYear){
            render(successFalse)
            return
        }
        def aimType = otherParams.aimType?.toLong()
        def owner = Employee.get(otherParams.owner.toLong())
        def yearMonthMap = [:]
        def format = new SimpleDateFormat('yyyy-MM')
        def currentCal = Calendar.getInstance()
        currentCal.set(Calendar.YEAR,otherParams.aimYear?.toInteger())
        def data = JSON.parse(params.data)
        data.each {d->
            (0..<12).each {
                def num = it + 1
                def aimValue = d."aim_$num"
                if(aimValue != null && aimValue != 0){
                    currentCal.set(Calendar.MONTH,it)
                    currentCal.set(Calendar.DATE,1)
                    def aimYearMonth = format.format(currentCal.getTime())
                    def saleAim
                    if(yearMonthMap.containsKey("$aimYearMonth")){
                        saleAim = yearMonthMap.get("$aimYearMonth")
                    }else{
                        saleAim = SaleAim.findByOwnerAndAimYearMonthAndAimType(owner,aimYearMonth,aimType)
                        if(!saleAim){
                            saleAim = new SaleAim(owner: owner, user: owner.user,employee:session.employee,
                                    dept: owner.dept,aimYearMonth: aimYearMonth,aimType: aimType)
                        }
                    }
                    saleAim."$d.aimKind" = aimValue
                    yearMonthMap << ["$aimYearMonth":saleAim]
                }
            }
        }
        yearMonthMap.each {key,saleAim->
            saleAim.save(flush: true)
        }
        render(successTrue)
    }

}
