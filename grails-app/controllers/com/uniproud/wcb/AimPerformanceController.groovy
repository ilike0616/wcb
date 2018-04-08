package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue

@UserAuthAnnotation
@Transactional(readOnly = true)
class AimPerformanceController {
    def modelService
    def list(){
        if(!params.aimKind) params.aimKind = 'aimCustomer'

        def data = getData(params)

        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(data as JSON)
        }
    }

    def getData(params){
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
        def employeeList = []
        // 是否需要上下级
        Employee.createCriteria().list(){
            eq("user",employee.user)
            eq("isSalesMan",true)
            eq("enabled",true)
            inList("id",employeeIds)
            if(depts){
                inList("dept.id",depts)
            }
        }.each {emp->
            if(deptEmp.containsKey(emp.dept)){
                deptEmp << [(emp.dept):deptEmp.get(emp.dept) << emp]
            }else{
                deptEmp << [(emp.dept):[emp]]
            }
            if(!employeeList.contains(emp)){
                employeeList << emp
            }
        }
        def employees = [:]
        if(employeeList){
            employees = statEmployeesAimCompleteData(employeeList)
        }

        def data = []
        deptEmp.each {dept,emps->
            def empData = [:]
            def children = []
            // 员工
            emps.each {emp->
                empData = [:]
                def empAimComplete = employees.get(emp.id)
                empData << [owner:emp.id,ownerName:emp.name,dept:emp.dept?.id,deptName:emp.dept?.name,aimType:1,leaf:true]
                fillLowerEmpData(emp,empData,empAimComplete)
                children << empData
            }
            // 部门
            empData = [:]
            def deptM = dept.deptManager
            empData << [owner:deptM?.id,ownerName:"$dept.name(${deptM?.name})",dept:dept.id,deptName:dept.name,aimType:2,
                        leaf:false,children:children,"expanded":true]
            fillDeptManagerData(deptM,empData,children)
            data << empData
        }
        return data
    }

    /**
     * 统计实际完成数据
     * @param employeeList
     * @return
     */
    def statEmployeesAimCompleteData(employeeList){
        def aimKind = WebUtilTools.params.aimKind
        def format = new SimpleDateFormat("yyyy-MM")
        def employees = [:]
        def date = DateUtil.getStartAndEndDateByYear(WebUtilTools.params.aimYear)
        def monthMap = [:]
        // 客户数
        if("aimCustomer".equals(aimKind) || "all".equals(aimKind)){
            Customer.createCriteria().list(){
                inList('employee',employeeList)
                eq('deleteFlag',false)
                between('dateCreated',date.get('startDate'),date.get('endDate'))
                projections {
                    sqlGroupProjection 'employee_id,date_created, count(*) as totalNum',
                        'employee_id,date_created', ['employee_id','date_created','totalNum'],
                        [LONG,DATE, INTEGER]
                }
            }.each{rs->
                def yearMonth = format.format(rs[1])
                def rs2 = rs[2]?rs[2]:0
                if(employees.containsKey(rs[0])){
                    if(employees.get(rs[0]).containsKey("$yearMonth")){
                        monthMap = employees.get(rs[0]).get("$yearMonth")
                        def aimCustomer = monthMap.get("aimCustomer")?monthMap.get("aimCustomer"):0
                        monthMap << ['aimCustomer':aimCustomer+rs2]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }else{
                        monthMap = ["$yearMonth":['aimCustomer':rs2]]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }
                }else{
                    monthMap = ["$yearMonth":['aimCustomer':rs2]]
                    employees << [(rs[0]): monthMap]
                }
            }
        }
        // 销售额
        if("aimOrderMoney".equals(aimKind) || "all".equals(aimKind)){
            ContractOrder.createCriteria().list(){
                inList('employee',employeeList)
                eq('deleteFlag',false)
                between('dateCreated',date.get('startDate'),date.get('endDate'))
                projections {
                    sqlGroupProjection 'employee_id,date_created, sum(discount_money) as totalMoney',
                            'employee_id,date_created', ['employee_id','date_created','totalMoney'],
                            [LONG,DATE, DOUBLE]
                }
            }.each{rs->
                def yearMonth = format.format(rs[1])
                def rs2 = rs[2]?rs[2]:0
                if(employees.containsKey(rs[0])){
                    if(employees.get(rs[0]).containsKey("$yearMonth")){
                        monthMap = employees.get(rs[0]).get("$yearMonth")
                        def aimOrderMoney = monthMap.get("aimOrderMoney")?monthMap.get("aimOrderMoney"):0
                        monthMap << ['aimOrderMoney':aimOrderMoney+rs2]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }else{
                        monthMap = ["$yearMonth":['aimOrderMoney':rs2]]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }
                }else{
                    monthMap = ["$yearMonth":['aimOrderMoney':rs2]]
                    employees << [(rs[0]): monthMap]
                }
            }
        }
        // 回款额
        if("aimPayMoney".equals(aimKind) || "all".equals(aimKind)){
            FinanceIncomeExpense.createCriteria().list(){
                inList('employee',employeeList)
                eq('financeType',1) // 入账
                eq('incomeKind',1) // 入账类型为订单
                eq('chargeState',2) // 已记账
                between('financeDate',date.get('startDate'),date.get('endDate'))
                projections {
                    sqlGroupProjection 'employee_id,finance_date, sum(money) as totalMoney',
                            'employee_id,finance_date', ['employee_id','finance_date','totalMoney'],
                            [LONG,DATE, DOUBLE]
                }
            }.each{rs->
                def yearMonth = format.format(rs[1])
                def rs2 = rs[2]?rs[2]:0
                if(employees.containsKey(rs[0])){
                    if(employees.get(rs[0]).containsKey("$yearMonth")){
                        monthMap = employees.get(rs[0]).get("$yearMonth")
                        def aimPayMoney = monthMap.get("aimPayMoney")?monthMap.get("aimPayMoney"):0
                        monthMap << ['aimPayMoney':aimPayMoney+rs2]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }else{
                        monthMap = ["$yearMonth":['aimPayMoney':rs2]]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }
                }else{
                    monthMap = ["$yearMonth":['aimPayMoney':rs2]]
                    employees << [(rs[0]): monthMap]
                }
            }
        }

        // 客户跟进数量
        if("aimCustomerFollow".equals(aimKind) || "all".equals(aimKind)){
            CustomerFollow.createCriteria().list(){
                inList('employee',employeeList)
                eq('deleteFlag',false)
                between('dateCreated',date.get('startDate'),date.get('endDate'))
                projections {
                    sqlGroupProjection 'employee_id,date_created, count(subject) as totalNum',
                            'employee_id,date_created', ['employee_id','date_created','totalNum'],
                            [LONG,DATE, DOUBLE]
                }
            }.each{rs->
                def yearMonth = format.format(rs[1])
                def rs2 = rs[2]?rs[2]:0
                if(employees.containsKey(rs[0])){
                    if(employees.get(rs[0]).containsKey("$yearMonth")){
                        monthMap = employees.get(rs[0]).get("$yearMonth")
                        def aimCustomerFollow = monthMap.get("aimCustomerFollow")?monthMap.get("aimCustomerFollow"):0
                        monthMap << ['aimCustomerFollow':aimCustomerFollow+rs2]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }else{
                        monthMap = ["$yearMonth":['aimCustomerFollow':rs2]]
                        employees << [(rs[0]):employees.get(rs[0]) << monthMap]
                    }
                }else{
                    monthMap = ["$yearMonth":['aimCustomerFollow':rs2]]
                    employees << [(rs[0]): monthMap]
                }
            }
        }
        return employees
    }

    /**
     * 统计部门下面的员工数据
     * @param owner
     * @param empData
     * @param empAimComplete
     * @return
     */
    def fillLowerEmpData(owner,empData,empAimComplete){
        def format = new SimpleDateFormat('yyyy-MM')
        def currentCal = Calendar.getInstance()
        if(WebUtilTools.params.aimYear){
            currentCal.set(Calendar.YEAR,WebUtilTools.params.aimYear?.toInteger())
        }
        def aimKind = WebUtilTools.params.aimKind
        def aimSum = 0
        def aimCompleteSum = 0
        (0..<12).each {
            def num = it + 1
            currentCal.set(Calendar.MONTH,it)
            currentCal.set(Calendar.DATE,1)
            def aimYearMonth = format.format(currentCal.getTime())
            def saleAim = SaleAim.findByOwnerAndAimYearMonthAndAimType(owner,aimYearMonth,1)
            // 目标
            def aim = saleAim?."$aimKind" ? saleAim."$aimKind" : 0
            aimSum += aim
            // 完成
            def monthMap = [:]
            if(empAimComplete?.get("$aimYearMonth")){
                monthMap = empAimComplete.get("$aimYearMonth")
            }
            def aimComplete = monthMap?.get(aimKind) ? monthMap?.get(aimKind).toDouble() : 0
            aimCompleteSum += aimComplete
            // 完成百分比
            def aimCompletePercent = 0
            if(aim != 0 && aimComplete != 0){
                aimCompletePercent = Math.round(aimComplete / aim * 100)
            }
            empData << ["aim_$num":aim,"aimComplete_$num":aimComplete,
                        "aimCompletePercent_$num":aimCompletePercent]
        }
        // 总计完成百分比
        def aimCompletePercentSum = 0
        if(aimSum != 0 && aimCompleteSum != 0){
            aimCompletePercentSum = Math.round(aimCompleteSum / aimSum * 100)
        }
        empData << [aim_sum:aimSum,aimComplete_sum:aimCompleteSum,aimCompletePercent_sum:aimCompletePercentSum]
    }

    /**
     * 统计部门数据（部门+下属员工）
     * @param owner
     * @param empData
     * @param childrens
     * @return
     */
    def fillDeptManagerData(owner,empData,childrens){
        def format = new SimpleDateFormat('yyyy-MM')
        def currentCal = Calendar.getInstance()
        if(WebUtilTools.params.aimYear){
            currentCal.set(Calendar.YEAR,WebUtilTools.params.aimYear?.toInteger())
        }
        def aimKind = WebUtilTools.params.aimKind
        def aimSum = 0
        def aimCompleteSum = 0
        (0..<12).each {
            def num = it + 1
            def everyMonthAimSum = 0
            def everyMonthAimCompleteSum = 0
            currentCal.set(Calendar.MONTH,it)
            currentCal.set(Calendar.DATE,1)
            def aimYearMonth = format.format(currentCal.getTime())
            def saleAim = SaleAim.findByOwnerAndAimYearMonthAndAimType(owner,aimYearMonth,2)
            def selfAim = saleAim?."$aimKind" ? saleAim?."$aimKind" : 0
            everyMonthAimSum += selfAim
            childrens.each{empAimInfo->
                // 目标
                def aim = empAimInfo?.get("aim_$num") ? empAimInfo?.get("aim_$num") : 0
                everyMonthAimSum += aim
                // 完成
                def aimComplete = empAimInfo?.get("aimComplete_$num") ? empAimInfo?.get("aimComplete_$num") : 0
                everyMonthAimCompleteSum += aimComplete
            }
            // 完成百分比
            def everyAimCompletePercent = 0
            if(everyMonthAimSum != 0 && everyMonthAimCompleteSum != 0){
                everyAimCompletePercent = Math.round(everyMonthAimCompleteSum / everyMonthAimSum * 100)
            }
            empData << ["aim_$num":everyMonthAimSum,"aimComplete_$num":everyMonthAimCompleteSum,
                        "aimCompletePercent_$num":everyAimCompletePercent]

            aimSum += everyMonthAimSum
            aimCompleteSum += everyMonthAimCompleteSum
        }
        // 总计完成百分比
        def aimCompletePercentSum = 0
        if(aimSum != 0 && aimCompleteSum != 0){
            aimCompletePercentSum = Math.round(aimCompleteSum / aimSum * 100)
        }
        empData << [aim_sum:aimSum,aimComplete_sum:aimCompleteSum,aimCompletePercent_sum:aimCompletePercentSum]
    }

    def detailList(){
        def format = new SimpleDateFormat('yyyy-MM')
        def currentCal = Calendar.getInstance()
        currentCal.set(Calendar.YEAR,params.aimYear?.toInteger())
        def owner = Employee.get(params.owner?.toLong())
        def aimKindMap = ['aimOrderMoney':'销售额','aimCustomer':'客户数','aimPayMoney':'回款额']
        if(1==2){
            aimKindMap << ['aimOrderProfit':'销售毛利','aimCustomerFollow':'客户跟进数']
        }

        params.aimKind = 'all' // 手动设置成all，以便统计所有的类型
        def employeeList = []
        def aimType = params.aimType.toInteger()
        // 部门还是个人
        if(aimType == 1){
            employeeList << owner
        }else{
            Employee.createCriteria().list(){
                eq("user",owner.user)
                eq("isSalesMan",true)
                eq("enabled",true)
                eq("dept",owner.dept)
            }.each {emp->
                if(!employeeList.contains(emp)){
                    employeeList << emp
                }
            }
        }
        def employeesComplete = statEmployeesAimCompleteData(employeeList)
        //employeeList.remove(owner)
        def data = []
        def empData = [:]
        aimKindMap.each {aimKind,aimKindName->
            empData = [:]
            empData << [aimKind:"$aimKind",aimKindName: "$aimKindName"]
            def aimSum = 0
            def aimCompleteSum = 0
            (0..<12).each {
                def num = it + 1
                currentCal.set(Calendar.MONTH,it)
                currentCal.set(Calendar.DATE,1)
                def aimYearMonth = format.format(currentCal.getTime())
                // 目标
                def aim = 0
                def saleAim = SaleAim.findByOwnerAndAimYearMonthAndAimType(owner,aimYearMonth,aimType)
                if(aimType == 1){ // 个人
                    aim = saleAim?."$aimKind" ? saleAim."$aimKind" : 0
                }else{ // 部门（部门下员工+部门）
                    aim += saleAim?."$aimKind" ? saleAim."$aimKind" : 0
                    employeeList.each {e->
                        saleAim = SaleAim.findByOwnerAndAimYearMonthAndAimType(e,aimYearMonth,1)
                        aim += saleAim?."$aimKind" ? saleAim."$aimKind" : 0
                    }
                }
                // 完成
                def aimComplete = 0
                def monthMap = [:]
                def empComplete = employeesComplete.get(owner.id)
                if(empComplete?.containsKey("$aimYearMonth")){
                    monthMap = empComplete.get("$aimYearMonth")
                }
                if(aimType == 1){ // 个人
                    aimComplete = monthMap?.get(aimKind) ? monthMap?.get(aimKind).toDouble() : 0
                }else{ // 部门（部门下员工+部门）
                    aimComplete += monthMap?.get(aimKind) ? monthMap?.get(aimKind).toDouble() : 0
                    employeeList.each {e->
                        monthMap = [:]
                        empComplete = employeesComplete.get(e.id)
                        if(empComplete?.containsKey("$aimYearMonth")){
                            monthMap = empComplete.get("$aimYearMonth")
                        }
                        aimComplete += monthMap?.get(aimKind) ? monthMap?.get(aimKind).toDouble() : 0
                    }
                }
                // 完成百分比
                def aimCompletePercent = 0
                if(aim != 0 && aimComplete != 0){
                    aimCompletePercent = Math.round(aimComplete / aim * 100)
                }
                empData << ["aim_$num":aim,"aimComplete_$num":aimComplete,
                            "aimCompletePercent_$num":aimCompletePercent]

                aimSum += aim
                aimCompleteSum += aimComplete
            }
            def aimCompletePercentSum = 0
            if(aimSum != 0 && aimCompleteSum != 0){
                aimCompletePercentSum = Math.round(aimCompleteSum / aimSum * 100)
            }
            empData << [aim_sum:aimSum,aimComplete_sum:aimCompleteSum,aimCompletePercent_sum:aimCompletePercentSum]
            data << empData
        }
        println(data)
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render([success:true,data:data] as JSON)
        }
    }

    def aimPerformanceYearStatChart(){
        def format = new SimpleDateFormat("yy-MM")
        if(!params.aimKind) params.aimKind = 'aimCustomer'

        def employee = session.employee
        def tempEmployeeIds = []
        def tempEmp = Employee.get(employee.id)
        tempEmployeeIds.push(tempEmp.id)
        tempEmployeeIds.addAll(modelService.getChildrenIds(tempEmp.children))

        def deptIds = []
        def deptFields = []
        def employeeIds = []
        def employeeFields = []
        Employee.createCriteria().list(){
            eq("user",employee.user)
            eq("isSalesMan",true)
            eq("enabled",true)
            inList("id",tempEmployeeIds)
        }.each {emp->
            if(!deptIds.contains(emp.dept?.id)){
                deptIds << emp.dept?.id
                deptFields << emp.dept.name + "_" + emp.dept?.id
            }
            if(!employeeIds.contains(emp.id)){
                employeeIds << emp.id
                employeeFields << emp.name + "_" + emp.id
            }
        }

        def data = getData(params)

        def dataList = []
        def aimType
        if(deptIds && deptIds.size() > 1){ // 按部门
            dataList = data
            aimType = 2
        }else if(employeeIds){
            dataList = data[0].children
            aimType = 1
        }
        def resultData = []
        dataList?.each {d->
            def tempData = [ownerName:d.ownerName]
            def aimArr = []
            def aimCompleteArr = []
            (1..<13).each {
                def aimProp = "aim_$it"
                def aimCompleteProp = "aimComplete_$it"
                d.each { key, value ->
                    if (d.aimType == aimType && key == aimProp) {
                        aimArr << value
                    }
                    if (d.aimType == aimType && key == aimCompleteProp) {
                        aimCompleteArr << value
                    }
                }
            }
            tempData << [aimArr: aimArr,aimCompleteArr: aimCompleteArr]
            resultData << tempData
        }

        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(([success: true,data: resultData]) as JSON)
        }
    }

    /**
     * 按月统计目标业绩完成
     */
    def statAimPerformanceByMonthForPhone(){
        params.aimKind = 'all'
        def fmt = new SimpleDateFormat("yyyy-MM")
        def emp
        if(params.employee){
            emp = Employee.get(params.employee.toLong())
        }else{
            emp = Employee.get(session.employee?.id)
        }
        def aimYearMonth
        if(params.aimYearMonth){
            aimYearMonth = params.aimYearMonth
        }else{
            aimYearMonth = fmt.format(new Date())
        }
        def result = []
        // 查询目标
        def aimList = SaleAim.createCriteria().list {
            eq("employee",emp)
            eq("aimYearMonth",aimYearMonth)
        }.each {
            result = [aimCustomer: it.aimCustomer,aimOrderMoney: it.aimOrderMoney,aimPayMoney: it.aimPayMoney]
        }
        if(!aimList){
            result = [aimCustomer: 0,aimOrderMoney: 0,aimPayMoney: 0]
        }
        // 统计完成
        def monthMap = [:]
        def employeesComplete = statEmployeesAimCompleteData([emp])
        def empComplete = employeesComplete.get(emp.id)
        if(empComplete?.containsKey("$aimYearMonth")){
            monthMap = empComplete.get("$aimYearMonth")
        }
        def aimCustomerComplete = monthMap.get("aimCustomer") ? monthMap.get("aimCustomer").toDouble() : 0
        def aimOrderMoneyComplete = monthMap.get("aimOrderMoney") ? monthMap.get("aimOrderMoney").toDouble() : 0
        def aimPayMoneyComplete = monthMap.get("aimPayMoney") ? monthMap.get("aimPayMoney").toDouble() : 0
        result << [aimCustomerComplete: aimCustomerComplete,aimOrderMoneyComplete: aimOrderMoneyComplete,aimPayMoneyComplete: aimPayMoneyComplete]

        render([success:true,aimPerformance:result] as JSON)
    }

}
