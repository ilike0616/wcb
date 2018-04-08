package com.uniproud.wcb

import grails.transaction.Transactional

@Transactional
class PaymentRecordService {

    /**
     * 执行扣费，并保存记录
     * @return
     */
    def deductMoney() {
        def dateMap = SysTool.generateStartAndEndDate(new Date())
        def users = User.createCriteria().list {
            between("dueDate", dateMap.get('startDate'), dateMap.get('endDate'))      //扣款日期时今天的
            eq("enabled", true)          //启用的
            ne("monthlyFee", new BigDecimal(0))          // 月费用不为零的
        }
        users.each { user ->
            def preBalance = user.balance
            user.balance -= user.monthlyFee
            if (user.balance < 0) {
                user.enabled == false       //欠费停用
            }
            //计算下次扣费时间
            user.dueDate = SysTool.todayOfNextMonth(user.dueDate)
            user.save()
            def record = new PaymentRecord(user: user, amountFee: user.monthlyFee, preBalance: preBalance, postBalance: user.balance, syNum: user.employees?.size(), kfNum: user.allowedNum)
            record.save()
        }
    }
}
