package com.uniproud.wcb

import cn.jpush.api.JPushClient
import cn.jpush.api.common.resp.APIConnectionException
import cn.jpush.api.common.resp.APIRequestException
import cn.jpush.api.push.PushResult
import cn.jpush.api.push.model.Message
import cn.jpush.api.push.model.Platform
import cn.jpush.api.push.model.PushPayload
import cn.jpush.api.push.model.audience.Audience
import cn.jpush.api.push.model.audience.AudienceTarget
import com.sun.org.apache.xpath.internal.operations.Bool
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.sql.Insert
import sun.misc.BASE64Encoder

@Transactional
class NotifyModelService {

    def grailsApplication
    def baseService

    def final static INSERT = 1;
    def final static UPDATE = 2;
    def final static DATE_VALUE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    def final static TRUE_TEXT = "是"
    def final static FALSE_TEXT = "否"
    /**
     * 新增提醒
     * @param domain 操作记录
     * @param moduleId 模块
     * @return
     */
    def insertNotify(domain, moduleId) {
        def employee = WebUtilTools.getSession().employee
        //查询该模块配置的新增时提醒的消息模型
        def notifyModels = NotifyModel.createCriteria().list {
            eq('user', employee?.user)
            module {
                eq('moduleId', moduleId)
            }
            eq('isAuto', false)                 //操作事件触发
            eq('insertNotify', true)            //新增时提醒
            eq('deleteFlag', false)
            //isNotNull('notifyModelFilter')      //对应的条件组不为空
        }
        checkNotifyModel(notifyModels, domain, moduleId, INSERT, [])
    }

    /**
     * 修改提醒
     * @param domain 操作记录
     * @param moduleId 模块
     * @param modifyValue 修改的字段值记录
     * @return
     */
    def updateNotify(domain, moduleId, modifyValue) {
        def employee = WebUtilTools.getSession().employee
        //查询该模块配置的新增时提醒的消息模型
        def notifyModels = NotifyModel.createCriteria().list {
            eq('user', employee?.user)
            module {
                eq('moduleId', moduleId)
            }
            eq('isAuto', false)                 //操作事件触发
            eq('updateNotify', true)            //修改时提醒
            eq('deleteFlag', false)
//            isNotNull('notifyModelFilter')      //对应的条件组不为空
        }
        checkNotifyModel(notifyModels, domain, moduleId, UPDATE, modifyValue)
    }

    /**
     * 对根据moduleId和操作类型初选后的消息模型，进一步根据触发跳进判断是否提醒
     * @param notifyModels 模块对应的新增或修改时的消息模型
     * @param domain 新增或修改的对象
     * @param moduleId 模块
     * @param operate 新增或者修改
     * @param modifyValue 可选形参，修改时的修改的字段值记录
     * @return
     */
    def checkNotifyModel(notifyModels, domain, moduleId, operate, def modifyValue) {
        def module = Module.findByModuleId(moduleId)
        if(module==null){
            return
        }
        def object = grailsApplication.getClassForName(module.model?.modelClass)
        //遍历消息模型
        notifyModels.each { notifyModel ->
            def filter = notifyModel.notifyModelFilter
            if (checkModifyField(filter, operate, modifyValue)) {
                //解析消息模型获得的触发条件
                def searchCondition = parseNofifyModelFilter(filter)
                def record = object.createCriteria().list() {
                    searchCondition.delegate = delegate
                    searchCondition()
                    eq('id', domain.id as Long)
                }
                //符合条件，触发消息模型notifyModel
                if (record) {
                    fireNotifyModel(notifyModel, domain, module)
                }
            }
        }
    }

    def checkModifyField(filter, operate, modifyValue) {
        def modifyFlag = false          //是否修改了条件对应的字段，没有则不提醒
        if (operate == INSERT)
            modifyFlag = true
        else if (operate == UPDATE) {        //如果是修改操作，需要判断条件对应的字段是否有被修改过的
            def filterFieldNames = getFilterFieldNames(filter)
            modifyValue?.each { mfv ->
                if (mfv.fieldName in filterFieldNames) {
                    modifyFlag = true
                }
            }
        }
        modifyFlag
    }
    /**
     * 递归，返回模型的所有条件明细的字段名称
     * @param notifyModelFilter 最上级条件组
     */
    def getFilterFieldNames(notifyModelFilter) {
        def filterFieldNames = notifyModelFilter?.filterDetail?.fieldName
        notifyModelFilter?.children?.each { childFilter ->
            getFilterFieldNames(childFilter).each { field ->
                filterFieldNames << field
            }
        }
        filterFieldNames
    }

    /**
     * 解析NofifyModelFilte为查询条件
     * @param filter 消息模型条件组
     */
    def parseNofifyModelFilter(filter) {
        if (!filter) {
            return {}
        }
        //子条件或条件组之间的关系
        def childRelation = filter.childRelation == 1 ? "and" : "or"
        def searchCondition = {
            "$childRelation" {
                //该组的条件明细
                filter?.filterDetail?.each { detail ->      //条件明细
                    def operator = detail.operator
                    def dbType = detail?.dbType
                    def expectValue = detail?.expectValue
                    switch (operator) {
                        case '==':
                            eq(detail.fieldName, convertDbType(dbType, expectValue))
                            break
                        case '!=':
                            ne(detail.fieldName, convertDbType(dbType, expectValue))
                            break
                        case '>':
                            gt(detail.fieldName, convertDbType(dbType, expectValue))
                            break
                        case '>=':
                            ge(detail.fieldName, convertDbType(dbType, expectValue))
                            break
                        case '<':
                            lt(detail.fieldName, convertDbType(dbType, expectValue))
                            break
                        case '<=':
                            le(detail.fieldName, convertDbType(dbType, expectValue))
                            break
                        case 'in':          //包含关系
                            if (detail.isDict) {
                                def values = []
                                detail?.expectValue?.tokenize(',')?.each { v ->
                                    values << (v as Integer)
                                }
                                'in'(detail.fieldName, values)
                            } else if (dbType == 'java.lang.String') {
                                ilike(detail.fieldName, "%$expectValue%")
                            }
                            break
                        case 'notin':       //不包含
                            if (detail.isDict) {
                                def values = []
                                detail?.expectValue?.tokenize(',')?.each { v ->
                                    values << (v as Integer)
                                }
                                not { 'in'(detail.fieldName, values) }
                            } else if (dbType == 'java.lang.String') {
                                not { ilike(detail.fieldName, "%$expectValue%") }
                            }
                            break
                    }
                }
                //子条件组
                filter?.children?.each { child ->
                    def childCondition = parseNofifyModelFilter(child)          //递归调用解析子条件组
                    childCondition.delegate = delegate
                    childCondition()
                }
            }
        }
        searchCondition
    }

    def convertDbType(dbType, value) {
        def expectValue
        switch (dbType) {
            case 'java.lang.Integer':
                expectValue = value as Integer
                break
            case 'java.lang.Long':
                expectValue = value as Long
                break
            case 'java.lang.Double':
                expectValue = value as Double
                break
            case 'java.math.BigDecimal':
                expectValue = value as BigDecimal
                break
            case 'java.util.Date':
                expectValue = DateUtil.toDate(value, DATE_VALUE_FORMAT)
                break
            case 'java.lang.Boolean':
                expectValue = value as Boolean
                break
            default:
                expectValue = value
        }
        expectValue
    }

    /**
     * 触发一个消息模型，获取接收者并分别判断是否接收消息
     * @param notifyModel
     * @param domain
     */
    def fireNotifyModel(notifyModel, domain, module) {
        def notifyField = notifyModel?.notifyField      //消息接收者字段
        def isAllowForbid = notifyModel?.isAllowForbid  //是否允许禁止
        def isDefaultRecv = notifyModel?.isDefaultRecv  //是否默认接收
        def receivers = domain.properties[notifyField]  //消息接受者，目前只支持员工Employee
        if (isDefaultRecv) {      //允许禁止
            //员工的是否接收设置
            receivers.each { emp ->
                def empSet = EmployeeNotifyModel.findByEmployeeAndNotifyModel(emp, notifyModel)  //员工的个人设置
                if (empSet && empSet.isRecv) {       //有个人设置并且设置接收
                    generateNotify(emp, notifyModel, domain, module, empSet.subjectTemplate, empSet.contentTemplate)
                } else if (!empSet && isDefaultRecv) {     //无个人设置并且默认接收
                    generateNotify(emp, notifyModel, domain, module, notifyModel.subjectTemplate, notifyModel.contentTemplate)
                }
            }
        } else {      //必须接收消息
            receivers.each { emp ->
                generateNotify(emp, notifyModel, domain, module, notifyModel.subjectTemplate, notifyModel.contentTemplate)
            }
        }
    }

    /**
     * 产生提醒消息
     * @param emp 接受者
     * @param notifyModel 消息模型
     * @param domain 操作记录
     * @param module 模块Module
     * @param subject 主题模板
     * @param content 内容模板
     * @return
     */
    def generateNotify(emp, notifyModel, domain, module, subjectTemplate, contentTemplate) {
        def subject = parseTemplate(emp.user, module, domain, subjectTemplate)
        def content = parseTemplate(emp.user, module, domain, contentTemplate)
        def dataCode = Math.round(Math.random()*100000000)  //8位随机数
        def notify = new Notify(user: emp.user, employee: emp, type: 1, module: module, objectId: domain.id, subject: subject, content: content, notifyModel: notifyModel,dataCode: dataCode)
        notify.save flush: true
        //推送消息
        def extra = [:]
        extra.put('moduleId', module?.moduleId)
        extra.put('objectId', domain?.id as String)
        if(domain.getProperties().containsKey("dataCode")) {
            domain.dataCode = dataCode
            domain.save()
            extra.put('dataCode', dataCode as String)
        }
        baseService.jpush(JPushTool.buildPushByAlias(emp.jpushAlias, subject, content, extra))
    }

    /**
     *  根据模板字符替换产生所发送的消息
     * @param domain 触发模型的数据记录
     * @param template 模板字符串
     * @return
     */
    def parseTemplate(user, module, domain, template) {
        user = User.findById(user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def regex = /\{([^\{\}]+?)#([^\{\}]+?)\}/
        template?.eachMatch(regex, {
            def fieldName = it[2]
            def value = ''      //替换的目标值
            if (fieldName.indexOf('.') != -1) { //关联表的取值
                def obj = domain
                fieldName.tokenize('.').each { name ->
                    obj = obj?."$name"
                }
                value = obj
            } else {
                value = domain."$fieldName"
            }
            def field = UserField.findByUserAndModuleAndFieldName(useUser, module, fieldName)
            //数据字典
            if (field.dict) {
                field.dict?.items?.each { item ->
                    if (item.itemId == value) {
                        value = item.text
                    }
                }
            } else if (field.dbType == 'java.util.Date') {
                value = DateUtil.format(value, DATE_VALUE_FORMAT)
            } else if (field.dbType == 'java.lang.Boolean') {
                if (value) {
                    value = TRUE_TEXT
                } else {
                    value = FALSE_TEXT
                }
            }
            template = template.replace(it[0], value ?: '' as String)
        })
        template
    }

}
