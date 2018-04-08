/**
 * Created by guozhen on 2014/6/19.
 */
Ext.define('user.controller.WorkBenchController',{
    extend : 'Ext.app.Controller',
    views:['workBench.WorkBench','workBench.LatestCustomerList','workBench.LatestCustomerFollowList','workBench.LatestSaleChanceList'
    ,'workBench.LatestSaleChanceFollowList','workBench.LatestContractOrderList','workBench.LatestServiceTaskList','workBench.LatestDealServiceTaskList'
    ,'workBench.LatestAuditList','workBench.LatestNotifyList','workBench.LatestBulletinList','workBench.CustomerFollowRankingList'
    ,'workBench.CustomerAddRankingList','workBench.SaleChanceFollowRankingList','workBench.ContractOrderAmountRankingList','workBench.SaleChanceRankingChart'
    ,'workBench.SaleChanceMoneyRankingChart','workBench.CustomerYearStatList','workBench.ContractOrderPaymentYearStatList','workBench.AimPerformanceYearStatChart'] ,
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [
    ],
    init:function(){
        this.control({
            'workBench portlet':{ // 工作台关闭
                close:function(o){
                    this.GridDoActionUtil.doAjax('userPortal/closeEmployeePortal',{id: o.dataId},null);
                }
            }
        });
    }
})
