package com.uniproud.wcb

import grails.transaction.Transactional

@Transactional
class DataDictService {

    def init(users){
        users.each {
            initUser(it)
            initUser2(it)
        }
    }
    @Transactional
    def initUser(user){
        if(DataDict.findByDataIdAndUser(1,user) == null){
            def dict = new DataDict(user:user,dataId:1,text: '客户种类',issys: true)
            dict.addToItems(new DataDictItem(user:user,itemId:1,text: '普通客户',seq: 1))
            dict.addToItems(new DataDictItem(user:user,itemId:2,text: '特殊客户',seq: 2))
            dict.addToItems(new DataDictItem(user:user,itemId:3,text: '重要客户',seq: 3))
            dict.addToItems(new DataDictItem(user:user,itemId:4,text: 'VIP客户',seq: 4))
            dict.save()
        }

        if(DataDict.findByDataIdAndUser(2,user) == null){
            new DataDict(user:user,dataId:2,text: '客户阶段')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '阶段1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '阶段2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '阶段3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(3,user) == null){
            new DataDict(user:user,dataId:3,text: '客户等级')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '等级1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '等级2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '等级3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(4,user) == null){
            new DataDict(user:user,dataId:4,text: '信用等级')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '等级1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '等级2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '等级3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(5,user) == null){
            new DataDict(user:user,dataId:5,text: '客户来源')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '来源1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '来源2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '来源3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(6,user) == null){
            new DataDict(user:user,dataId:6,text: '联系人类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '主联系人',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '次联系人',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(7,user) == null){
            new DataDict(user:user,dataId:7,text: '性别')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '男',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '女',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(8,user) == null){
            new DataDict(user:user,dataId:8,text: '证件类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '身份证',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '营业执照',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '其它',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(9,user) == null){
            new DataDict(user:user,dataId:9,text: '跟进种类')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '电话拜访',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '上门拜访',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(10,user) == null){
            new DataDict(user:user,dataId:10,text: '跟进结果')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '跟进结果1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '跟进结果2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '跟进结果3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(11,user) == null){
            new DataDict(user:user,dataId:11,text: '动作类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '签到',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '签退',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '拍照',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(12,user) == null){
            new DataDict(user:user,dataId:12,text: '对象类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '客户',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '对象',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(13,user) == null){
            new DataDict(user:user,dataId:13,text: '任务类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '任务类型1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '任务类型2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '任务类型3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(14,user) == null){
            new DataDict(user:user,dataId:14,text: '任务结果')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '任务结果1',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '任务结果2',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '任务结果3',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(15,user) == null){
            new DataDict(user:user,dataId:15,text: '服务状态')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '待处理',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '处理中',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '已处理',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(16,user) == null){
            new DataDict(user:user,dataId:16,text: '产品分类类别')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '通讯类',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '建材类',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '水果类',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(17,user) == null){
            new DataDict(user:user,dataId:17,text: '产品单位')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '瓶',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '箱',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '米',seq: 3))
                    .save()
        }

        /******************************* 计划总结 开始 *******************************/
        if(DataDict.findByDataIdAndUser(18,user) == null){
            new DataDict(user:user,dataId:18,text: '类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '日报',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '周报',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '月报',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '其他',seq: 4))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(19,user) == null){
            new DataDict(user:user,dataId:19,text: '状态')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '暂存',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '已交',seq: 2))
                    .save()
        }

        /******************************* 计划总结 结束 *******************************/
        if(DataDict.findByDataIdAndUser(20,user) == null){
            new DataDict(user:user,dataId:20,text: '任务状态',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '未开始',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '进行中',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '完成',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '关闭',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(21,user) == null){
            new DataDict(user:user,dataId:21,text: '审核类型',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '外出申请',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '出差申请',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '请假申请',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '加班申请',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '费用报销',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '财务入账',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '财务出账',seq: 7))
                    .addToItems(new DataDictItem(user:user,itemId:8,text: '开票记录',seq: 7))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(22,user) == null){
            new DataDict(user:user,dataId:22,text: '审核类型',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '未审核',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '通过',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '通过，继续流转',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '未通过',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(23,user) == null){
            new DataDict(user:user,dataId:23,text: '审核状态',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '待审核',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '审核中',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '审核通过',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '审核未通过',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(24,user) == null){
            new DataDict(user:user,dataId:24,text: '请假类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '事假',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '婚嫁',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '产假',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '病假',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(25,user) == null){
            new DataDict(user:user,dataId:25,text: '请假方式')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '天',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '小时',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(26,user) == null){
            new DataDict(user:user,dataId:26,text: '加班类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '普通加班',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '周末加班',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '节假日加班',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(27,user) == null){
            new DataDict(user:user,dataId:27,text: '加班地点')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '公司',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '非加班',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(28,user) == null){
            new DataDict(user:user,dataId:28,text: '考勤类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '上班',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '下班',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(29,user) == null){
            new DataDict(user:user,dataId:29,text: '市场活动类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '促销活动',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '品牌活动',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '会议销售',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '搜索引擎',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '互联网广告',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '平面媒体广告',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '电视媒体广告',seq: 7))
                    .addToItems(new DataDictItem(user:user,itemId:8,text: '关系公关',seq: 8))
                    .addToItems(new DataDictItem(user:user,itemId:9,text: '电话营销',seq: 9))
                    .addToItems(new DataDictItem(user:user,itemId:10,text: '短信营销',seq: 10))
                    .addToItems(new DataDictItem(user:user,itemId:11,text: '邮件营销',seq: 11))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(30,user) == null){
            new DataDict(user:user,dataId:30,text: '竞争对手规模')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '0-20人',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '20-50人',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '50-100人',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '100-200人',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '200-500人',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '500-1000人',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '大于1000人',seq: 7))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(31,user) == null){
            new DataDict(user:user,dataId:31,text: '对手竞争力')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '一般竞争力',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '潜在竞争力',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '弱竞争力',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '强竞争力',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(32,user) == null){
            new DataDict(user:user,dataId:32,text: '对手动态分类')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '新品发布',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '客户项目',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '内部活动',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '其他',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(33,user) == null){
            new DataDict(user:user,dataId:33,text: '客户状态')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '空闲中',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '申请中',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '已分配',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '回收空闲',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(34,user) == null){
            new DataDict(user:user,dataId:34,text: '客户分类',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '公海客户',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '我的客户',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(35,user) == null){
            new DataDict(user:user,dataId:35,text: '登录方式')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '电脑',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '手机',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(36,user) == null){
            new DataDict(user:user,dataId:36,text: '类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '登录成功',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '安全退出',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(37,user) == null){
            new DataDict(user:user,dataId:37,text: '时间模式',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '单时间模式',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '多时间模式',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(38,user) == null){
            new DataDict(user:user,dataId:38,text: '签到方式',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '手机',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: 'PC',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(39,user) == null){
            new DataDict(user:user,dataId:39,text: '是否',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '是',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '否',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(40,user) == null){
            new DataDict(user:user,dataId:40,text: '提醒消息类型',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '业务触发',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '自动提醒',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(41,user) == null){
            new DataDict(user:user,dataId:41,text: '内部公告种类')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '重要通知',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '公司新闻',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '业界动态',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '其他',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(42,user) == null){
            new DataDict(user:user,dataId:42,text: '付款方式')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '现金',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '支票',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '转账',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '托收',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(43,user) == null){
            new DataDict(user:user,dataId:43,text: '快递公司')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '顺丰',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '申通',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '圆通',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: 'EMS',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(44,user) == null){
            new DataDict(user:user,dataId:44,text: '订单状态')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '款待排产',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '正生产',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '排产待发货',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '已发货待开船',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '已开船等退单',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '退单已到',seq: 6))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(45,user) == null){
            new DataDict(user:user,dataId:45,text: '订单发货状态')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '无需发货',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '待发货',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '发货审核',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '部分发货',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '全部发货',seq: 5))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(46,user) == null){
            new DataDict(user:user,dataId:46,text: '是否全公司',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:0,text: '否',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '是',seq: 2))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(47,user) == null){
            new DataDict(user:user,dataId:47,text: '商机跟进方式')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '电话',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '上门拜访',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '邮件',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '会议',seq: 4))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(48,user) == null){
            new DataDict(user:user,dataId:48,text: '商机阶段')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '初期沟通',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '立项评估',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '需求分析',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '方案制定',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '招标投标/竞争',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '商务谈判',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '合同签约',seq: 7))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(49,user) == null){
            new DataDict(user:user,dataId:49,text: '商机分类')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '分类一',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '分类二',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '分类三',seq: 3))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(50,user) == null){
            new DataDict(user:user,dataId:50,text: '商机状态')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '跟踪',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '成功',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '失败',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '搁置',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '失效',seq: 5))
                    .save()
        }


    }
    /**
     * 代码太长，从 initUser(user) 分离出一部分
     * @param user
     * @return
     */
    @Transactional
    def initUser2(user) {
        if(DataDict.findByDataIdAndUser(51,user) == null){
            new DataDict(user:user,dataId:51,text: '商机来源')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '电话来访',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '客户介绍',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '独立开发',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '媒体宣传',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '老客户',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '代理商',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '合作伙伴',seq: 7))
                    .addToItems(new DataDictItem(user:user,itemId:8,text: '公开招标',seq: 8))
                    .addToItems(new DataDictItem(user:user,itemId:9,text: '互联网',seq: 9))
                    .addToItems(new DataDictItem(user:user,itemId:10,text: '其他',seq: 10))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(52,user) == null){
            new DataDict(user:user,dataId:52,text: '可能性')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '0%',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '10%',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '20%',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '30%',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '40%',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '50%',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '60%',seq: 7))
                    .addToItems(new DataDictItem(user:user,itemId:8,text: '70%',seq: 8))
                    .addToItems(new DataDictItem(user:user,itemId:9,text: '80%',seq: 9))
                    .addToItems(new DataDictItem(user:user,itemId:10,text: '90%',seq: 10))
                    .addToItems(new DataDictItem(user:user,itemId:11,text: '100%',seq: 11))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(53,user) == null){
            new DataDict(user:user,dataId:53,text: '付款方式')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '现金',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '支票',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '转账',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '托收',seq: 4))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(54,user) == null){
            new DataDict(user:user,dataId:54,text: '付款条件')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '月结',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '支票',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '款到发货',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '货到付款',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '预付定金30%',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '其他',seq: 6))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(55,user) == null){
            new DataDict(user:user,dataId:55,text: '信心指数')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '0%',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '10%',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '20%',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '30%',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '40%',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '50%',seq: 6))
                    .addToItems(new DataDictItem(user:user,itemId:7,text: '60%',seq: 7))
                    .addToItems(new DataDictItem(user:user,itemId:8,text: '70%',seq: 8))
                    .addToItems(new DataDictItem(user:user,itemId:9,text: '80%',seq: 9))
                    .addToItems(new DataDictItem(user:user,itemId:10,text: '90%',seq: 10))
                    .addToItems(new DataDictItem(user:user,itemId:11,text: '100%',seq: 11))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(56,user) == null){
            new DataDict(user:user,dataId:56,text: '订单分类')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: 'A产品销售',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: 'B产品销售',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: 'C产品销售',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: 'D产品销售',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: 'E产品销售',seq: 5))
                    .save()
        }

        if(DataDict.findByDataIdAndUser(57,user) == null){
            new DataDict(user:user,dataId:57,text: '费用类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '日常办公',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '硬件投资',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '企业税金',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '餐旅报销',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '市场宣传',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '其他费用',seq: 6))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(58,user) == null){
            new DataDict(user:user,dataId:58,text: '报销类型',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '客户',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '销售商机',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '市场活动',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '外出',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '出差',seq: 5))
                    .addToItems(new DataDictItem(user:user,itemId:6,text: '其他',seq: 6))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(59,user) == null){
            new DataDict(user:user,dataId:59,text: '费用报销明细类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '日常报销',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '差旅费',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '交通费',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '市场宣传',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '其他',seq: 5))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(60,user) == null){
            new DataDict(user:user,dataId:60,text: '财务账户类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '银行账户',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '现金账户',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '虚拟账户',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '投资账户',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '其他',seq: 5))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(61,user) == null){
            new DataDict(user:user,dataId:61,text: '银行')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '中国银行',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '农业银行',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '工商银行',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '建设银行',seq: 4))
                    .addToItems(new DataDictItem(user:user,itemId:5,text: '交通银行',seq: 5))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(62,user) == null){
            new DataDict(user:user,dataId:62,text: '出入账类型',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '入账',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '出账',seq: 2))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(63,user) == null){
            new DataDict(user:user,dataId:63,text: '入账类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '订单收款',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '投资收益',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '退税',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '其他',seq: 4))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(64,user) == null){
            new DataDict(user:user,dataId:64,text: '出账类型')
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '财务费用',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '管理费用',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '企业税金',seq: 3))
                    .addToItems(new DataDictItem(user:user,itemId:4,text: '其他',seq: 4))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(65,user) == null){
            new DataDict(user:user,dataId:65,text: '红字更正',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '未更正',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '红字源纪录',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '红字更正记录',seq: 3))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(66,user) == null){
            new DataDict(user:user,dataId:66,text: '记账状态',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '待记账',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '已记账',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '已禁止',seq: 3))
                    .save()
        }
        // 日程安排
        if(DataDict.findByDataIdAndUser(67,user) == null){
            new DataDict(user:user,dataId:67,text: '任务状态',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '未开始',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '已完成',seq: 2))
                    .save()
        }
        // 日程安排
        if(DataDict.findByDataIdAndUser(68,user) == null){
            new DataDict(user:user,dataId:68,text: '任务状态',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '客户跟进',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '商机跟进',seq: 2))
                    .addToItems(new DataDictItem(user:user,itemId:3,text: '便签',seq: 3))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(69,user) == null){
            new DataDict(user:user,dataId:69,text: 'SFA状态',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '设计中',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '已开始',seq: 2))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(70,user) == null){
            new DataDict(user:user,dataId:70,text: '分享方式',issys: true)
                    .addToItems(new DataDictItem(user:user,itemId:1,text: '分享链接',seq: 1))
                    .addToItems(new DataDictItem(user:user,itemId:2,text: '原创分享',seq: 2))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(71,user) == null){
            new DataDict(user:user,dataId: 71,text: '出入账来源',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '销售订单',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '费用报销',seq: 2))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(72,user) == null){
            new DataDict(user:user,dataId: 72,text: '财务账户状态',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '待启用',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '已启用',seq: 2))
                    .addToItems(new DataDictItem(user: user,itemId: 3,text: '已禁用',seq: 3))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(73,user) == null){
            new DataDict(user:user,dataId: 73,text: '收款状态',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '未收款',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '部分收款',seq: 2))
                    .addToItems(new DataDictItem(user: user,itemId: 3,text: '已收款',seq: 3))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(74,user) == null){
            new DataDict(user:user,dataId: 74,text: '关闭结果')
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '成功签约',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '项目失败',seq: 2))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(75,user) == null){
            new DataDict(user:user,dataId: 75,text: '关闭类型',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '商机',seq: 1))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(76,user) == null){
            new DataDict(user:user,dataId: 76,text: '开票类型',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '增值',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '商业发票',seq: 2))
                    .addToItems(new DataDictItem(user: user,itemId: 3,text: '服务业发票',seq: 3))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(77,user) == null){
            new DataDict(user:user,dataId: 77,text: '开票状态',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '未开票',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '部分开票',seq: 2))
                    .addToItems(new DataDictItem(user: user,itemId: 3,text: '已开票',seq: 3))
                    .save()
        }
        if(DataDict.findByDataIdAndUser(78,user) == null){
            new DataDict(user:user,dataId: 78,text: '财务开票状态',issys: true)
                    .addToItems(new DataDictItem(user: user,itemId: 1,text: '未开票',seq: 1))
                    .addToItems(new DataDictItem(user: user,itemId: 2,text: '已开票',seq: 2))
                    .save()
        }
        if (DataDict.findByDataIdAndUser(79, user) == null) {
            new DataDict(user: user, dataId: 79, text: '公众号状态')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '禁用', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '已激活', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '待审查', seq: 3))
                    .save()
        }

        if (DataDict.findByDataIdAndUser(80, user) == null) {
            new DataDict(user: user, dataId: 80, text: '签到方式')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '手工签到', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '微信签到', seq: 2))
                    .save()
        }
        if (DataDict.findByDataIdAndUser(81, user) == null) {
            new DataDict(user: user, dataId: 81, text: '安装单状态',issys: true)
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '待派单', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '待处理', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '已处理', seq: 3))
                    .save()
        }
        if (DataDict.findByDataIdAndUser(82, user) == null) {
            new DataDict(user: user, dataId: 82, text: '派单模式',issys: true)
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '指派模式', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '预派模式', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '抢接模式', seq: 3))
                    .save()
        }
        //客户回访
        if (DataDict.findByDataIdAndUser(83, user) == null) {
            new DataDict(user: user, dataId: 83, text: '业务类型', issys: true)
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '产品', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '订单', seq: 2))
                    .save()
        }
        //客户回访
        if (DataDict.findByDataIdAndUser(84, user) == null) {
            new DataDict(user: user, dataId: 84, text: '回访方法')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '电话回访', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '实地回访', seq: 2))
                    .save()
        }
        //客户回访
        if (DataDict.findByDataIdAndUser(85, user) == null) {
            new DataDict(user: user, dataId: 85, text: '客户满意度')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '一般', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '满意', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '很满意', seq: 3))
                    .addToItems(new DataDictItem(user: user, itemId: 4, text: '不满意', seq: 4))
                    .save()
        }
        //客户关怀
        if (DataDict.findByDataIdAndUser(86, user) == null) {
            new DataDict(user: user, dataId: 86, text: '关怀方式')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '电话关怀', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '实地关怀', seq: 2))
                    .save()
        }
        //客户投诉
        if (DataDict.findByDataIdAndUser(87, user) == null) {
            new DataDict(user: user, dataId: 87, text: '投诉种类')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '电话投诉', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '上门投诉', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '邮件投诉', seq: 3))
                    .save()
        }
        //客户投诉
        if (DataDict.findByDataIdAndUser(88, user) == null) {
            new DataDict(user: user, dataId: 88, text: '处理结果')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '进行中', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '已解决', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '未解决', seq: 3))
                    .save()
        }
        //客户投诉
        if (DataDict.findByDataIdAndUser(89, user) == null) {
            new DataDict(user: user, dataId: 89, text: '投诉类型')
                    .addToItems(new DataDictItem(user: user, itemId: 1, text: '产品', seq: 1))
                    .addToItems(new DataDictItem(user: user, itemId: 2, text: '订单', seq: 2))
                    .addToItems(new DataDictItem(user: user, itemId: 3, text: '安装单', seq: 3))
                    .save()
        }

    }
}
