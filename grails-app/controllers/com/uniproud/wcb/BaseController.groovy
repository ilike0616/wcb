package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.NormalAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

import java.text.SimpleDateFormat;

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@UserAuthAnnotation
class BaseController {
    def baseService
    def modelService
    def validationService
    def privilegeService
	/**
	 * 持久化操作，
	 * 调用方式：chain(controller:'base',action: 'save',params:params,model:[domain:customer])
	 * @return
	 */
	@Transactional
	def save(){
		def domain = chainModel.domain
		if (domain.hasErrors()) {
			log.info domain.errors
			def json = [success:false,errors: errorsToResponse(domain.errors)] as JSON
			chain(action: 'validate',model:[json:json],params:params)
			return
		}
		domain.save flush: true
		chain(action: 'success',params:params)
	}
	
	/**
	 * 返回未通过验证的所有errors JSON
	 * 格式为：
	 * [success:false,errors: xxxxxxxxxx]
	 * @return
	 */
	def validate(){
		def json = chainModel.json
		if(params.callback) {
			render "${params.callback}($json)"
		}else{
			render(json)
		}
	}
	/**
	 * 公用返回错误的提示的JSON 
	 * 格式为：[success:false]
	 * @return
	 */
	def error(){
		if(params.callback) {
			render "${params.callback}($successFalse)"
		}else{
			render(successFalse)
		}
	}
	/**
	 * 返回错误的提示的JSON
	 * 格式为：[success:true]
	 * @return
	 */
	def success(){
        if(params.callback) {
            render "${params.callback}($successTrue)"
        }else{
            render(successTrue)
        }
	}

    /**
     * 查找固定字段
     * @return
     */
    @Transactional
    def searchExportField() {
        def user = User.get(session.employee?.user?.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def module = Module.findByModuleId(params.moduleId)
        def type = 'isSearchView'
        if(params.importOrExport && params.importOrExport == 'import'){
            type = "isImportOrExportView"
        }
        def view = View.createCriteria().list{
            eq("$type",true)
            eq('module',module)
            eq('user',useUser)
        }[0]

        // 屏蔽的id
        def emp = Employee.get(session.employee?.id)
        def privileges = emp?.privileges
        def privilegeViewDetail = []
        if(privileges){
            PrivilegeViewDetail.where{
                unShow == true
                privilege in privileges
                viewDetail{
                    view == view
                }
            }.list().each {
                privilegeViewDetail << it.viewDetail.id
            }
        }
        def q = ViewDetail.where {
            view == view
            if(privilegeViewDetail){
                not {'in'("id",privilegeViewDetail)}
            }
            ne("pageType","splitter")
        }
        def fields = []
        def list = q.list([sort: 'orderIndex',order: 'ASC'])
        def bitianList = list.findAll{
            it.userField.bitian == true
        }
        def notBitianList = list.findAll{
            !it.userField.bitian
        }
        def xh = "<span style='color:red;font-weight:bold'>*</span>"
        bitianList.each {detail->
            def it = detail.userField
            if(!it?.relation && !it?.fieldName?.endsWith(".id") && !it.fieldName.equalsIgnoreCase("id")){
                def text = it?.text
                if(text.length() > 10) text = text.substring(0,10)+"..."
                fields << [
                        boxLabel: xh + text, name: 'field',inputValue: it?.fieldName,checked:it.bitian,readOnly:it.bitian
                ]
            }
        }
        notBitianList.each {detail->
            def it = detail.userField
            if(!it?.relation && !it?.fieldName?.endsWith(".id") && !it.fieldName.equalsIgnoreCase("id")){
                def text = it?.text
                if(text.length() > 10) text = text.substring(0,10)+"..."
                fields << [
                        boxLabel: text, name: 'field',inputValue: it?.fieldName
                ]
            }
        }
        def json = [data:fields] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 生成模板文件
     * @return
     */
    def makeTemplateFile(){
        def objects = []
        render baseService.export(objects,true)
    }

    /**
     * 导入数据
     * @return
     */
    def importObject(){
        response.setContentType("text/html;charset=utf-8")
        render baseService.importObject()
    }

    /**
     * 统计对象信息
     * @return
     */
    def statisticEmployeeSomeObjectRanking(){
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        def empIds = []
        emps?.each{e->
            empIds << e.id
        }
        def employees = [:]
        Employee.createCriteria().list {
            eq('user',emp.user)
            or {
                eq("id", emp.id)
                if (empIds) {
                    inList('id', empIds)
                }
            }
        }.each {
            employees << [(it.id):it.name]
        }
        def items = []
        def module = Module.findByModuleId(params.moduleId)
        def fns = Field.where{
            model == module.model
        }.list()?.fieldName
        def empOrOwnerName = "employee"
        def empOrOwnerNameWithId = 'employee.id'
        if(fns.contains('owner')){
            empOrOwnerName = "owner"
            empOrOwnerNameWithId = "owner.id"
        }
        def mainDomain = grailsApplication.getClassForName(module?.getModel()?.modelClass)
        mainDomain.createCriteria().list {
            eq("user",emp.user)
            if(module.moduleId == 'employee'){
                or {
                    eq("id", emp.id)
                    if (empIds) {
                        inList('id', empIds)
                    }
                }
            }else {
                or {
                    eq("$empOrOwnerName", emp)
                    if (emps) {
                        inList("$empOrOwnerName", emps)
                    }
                }
            }
            if(module.moduleId == 'public_customer' && fns.contains('customerType')){
                eq("customerType", 1)
            }else if(module.moduleId == 'customer' && fns.contains('customerType')){
                eq("customerType", 2)
            }
            if(fns.contains('deleteFlag')){
                eq("deleteFlag",false)
            }
            projections {
                groupProperty("$empOrOwnerNameWithId")
                if(params.statType && params.statType == 'sum'){
                    sum(params.statField,'totalNum')
                }else{
                    count(params.statField,'totalNum')
                }
            }
            if(params.searchCondition){
                def searchCondition = JSON.parse(params.searchCondition) as List
                searchCondition.each {condition ->
                    def item = condition?.split("#@#")//查询字符串分割
                    def searchFieldValue = item[0] //获取查询条件值
                    def startDateValue,endDateValue
                    if (searchFieldValue == 'CUSTOM') { //如果是自定义时间格式
                        startDateValue = item[1]
                        endDateValue = item[2]
                    }
                    def dateMap = SysTool.getSearchDate(searchFieldValue, startDateValue, endDateValue)
                    between("dateCreated", dateMap.getAt("startDate"), dateMap.getAt("endDate"))
                }
            }
            maxResults(10)
            order('totalNum', "desc")
        }.each {cf->
            items << [data:cf[1],name:employees.get(cf[0]),empOrOwnerNameWithId:"$empOrOwnerNameWithId",empOrOwnerId:cf[0]]
            //items << [data:cf[1],name:cf[0]]
        }
        def json = [success:true,data:items] as JSON
        if(WebUtilTools.params.callback) {
            render "${params.callback}($json)"
        }else{
            render json
        }
    }

    /**
     * 统计漏斗图数据
     * @return
     */
    def statisticFunnelRanking(){
        def emp = session.employee
        def module = Module.findByModuleId(params.moduleId)
        def object = grailsApplication.getClassForName(module.model.modelClass)
        def userField = UserField.findByUserAndModuleAndFieldName(emp.user,module,params.groupField);
        def emps = [emp]
        if(params.meAndLower && params.meAndLower.toBoolean() == true){ // 我和我的下级
            emps = modelService.getEmployeeChildrens(session.employee?.id)
        }
        def fns = Field.where{
            model{
                modelClass == object.name
            }
        }.list()?.fieldName
        def empOrOwner = 'employee'
        if(fns.contains('owner')){
            empOrOwner = 'owner'
        }
        // 获取总的记录数
        def totalCount = object.createCriteria().list(){
            or {
                eq("$empOrOwner", emp)
                if (emps) {
                    inList("$empOrOwner", emps)
                }
            }
            if (fns?.contains('deleteFlag')) {
                eq('deleteFlag', false)
            }
        }.size()
        // 没有数据
        if(totalCount == 0 || totalCount == null){
            render(successFalse)
            return
        }
        def result = object.createCriteria().list(){
            or {
                eq("$empOrOwner", emp)
                if (emps) {
                    inList("$empOrOwner", emps)
                }
            }
            if (fns?.contains('deleteFlag')) {
                eq('deleteFlag', false)
            }
            projections {
                //count(params.groupField)
                if(params.statType && params.statType == 'sum'){
                    sum(params.statField)
                }else{
                    rowCount()
                }
                groupProperty(params.groupField)
            }
            order(params.groupField,'asc')
        }

        def resultContainsNull = false // 结果集中是否包含空值
        def count = 0 // 统计所有符合条目的值
        def sumFieldVal = 0 // 统计出来所有汇总过的值
        def itemsValue = [:] // 汇总的值
        def itemsRealValue = [:] // 实际的值
        DataDictItem.createCriteria().list(){
            eq('dict',userField.dict)
            order('seq','desc')
        }.eachWithIndex {item,i ->
            def key = "${item.itemId}"
            def exist = false
            result?.each {rs->
                def rs0 = rs[0]==null?0:rs[0]
                if(i == 0){
                    sumFieldVal += rs0
                }
                if(item.itemId == rs[1]){
                    count += rs0
                    if(rs0 != 0){
                        itemsValue << ["$key":count]
                    }else{
                        itemsValue << ["$key":0]
                    }
                    itemsRealValue << ["$key":rs0]
                    exist = true
                }else if(!rs[1]){
                    resultContainsNull = true
                }
            }
            if(!exist) {
                itemsValue << ["$key":count]
                itemsRealValue << ["$key":0]
            }
        }
        // count != sumFieldVal:说明存在状态为空的情况
        if(resultContainsNull == true) {
            itemsValue << ["0":sumFieldVal]
            itemsRealValue << ["0":sumFieldVal - count]
        }

        def data = []
        def legend = []
        if(itemsValue.containsKey("0")){
            data << [value:100,name:'无',realValue: itemsRealValue.get("0"),itemId:0]
            legend << ['无']
        }
        userField.dict?.items?.each{item->
            def value = itemsValue.get("${item.itemId}")
            def realValue = itemsRealValue.get("${item.itemId}")
            data << [value:sumFieldVal==0?0:Math.round(value/sumFieldVal * 100),name:item.text,realValue:realValue,itemId: item.itemId]
            legend << [item.text]
        }
        def json = [success:true,data:[legend:legend,seriesObj:data]] as JSON
        render json
    }

    /**
     * 上传文件
     * @return
     */
    @Transactional
    def uploadFile() {
        User user = session.employee?.user
        Employee employee = session.employee

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 文件列表
        Map<String,MultipartFile> fileMap = multipartRequest.getFileMap()
        MultipartFile mf = null
        def ext = null
        //def basePath = servletContext.getRealPath("/")
        fileMap.each {
            mf = it.getValue()
            def fileObj = baseService.generateFileAndSaveDoc(employee,mf,true)
            def doc = fileObj.doc
            def msg = [success:true,orgName:doc.orgName,docSeed:doc.id,serverFile:doc.name,httpFilePath:fileObj.httpAccessFilePath] as JSON
            if(params.callback) {
                render "${params.callback}($msg)"
            }else{
                render(msg)
            }
        }
    }

    /**
     * 文件下载
     * @return
     */
    def downloadFile(){
        def doc = Doc.get(params.id?.toLong())
        def saveFileDir = grailsApplication.config.SAVE_FILE_DIR // 文件路径
        def file = new File(saveFileDir + "/" + doc.name) // 获取指定路径下指定的文件
        if(doc && file.exists()){
            response.setContentType("application/octet-stream")
            response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(doc.orgName, "UTF-8"))
            response.outputStream << file.newInputStream()
        }else{
            render("文件不存在！")
        }
    }

    def getConfigParams(){
        def configParams = [:]
        def SAVE_FILE_DIR = grailsApplication.config.SAVE_FILE_DIR // 文件路径
        def HTTP_FILE_URL = grailsApplication.config.HTTP_FILE_URL // HTTP访问文件的目录
        configParams << [paramName:'SAVE_FILE_DIR',paramValue:SAVE_FILE_DIR]
        configParams << [paramName:'HTTP_FILE_URL',paramValue:HTTP_FILE_URL]
        render(configParams as JSON)
    }

    /**
     * 验证字段输入值是否唯一
     * 页面字段增加：vtype: 'unique'
     * 如果form表单中没有moduleId属性（非自定页面），字段属性中增加属性domainClass,例如:domainClass:'com.uniproud.wcb.User'
     * allUser:true-系统唯一（默认）；false：用户唯一
     * @return
     */
    @NormalAuthAnnotation
    def uniqueVerify(){
        if((!params.domainClass && !params.moduleId) || !params.fieldName || !params.value){
            render (successFalse)
            return
        }
        def domainClass = params.domainClass;
        if(!domainClass && params.moduleId){
            def module = Module.findByModuleId(params.moduleId)
            domainClass = Module.findByModuleId(params.moduleId)?.model?.modelClass
        }
        def fieldName = params.fieldName
        def weiyi = validationService.weiyi(params,domainClass,params.fieldName,params.value,params.allUser)
        if(weiyi){//唯一
            render(successTrue)
        }else{//不唯一
            render (successFalse)
        }
    }

    /**
     * 统计未读数据数量
     */
    @Transactional
    def statUnReadInfoNum(){
        if(!session.employee){
            render(successFalse)
            return
        }
        def employee = Employee.get(session.employee?.id)
        def statModuleIds = ['customer','public_customer','contract_order','work_audit','my_task']
        def modulesNum = []
        def ids = modelService.getChildrenIds(employee.children)
        if(ids){
            statModuleIds.each {moduleId->
                def module = Module.findByModuleId(moduleId)
                def moduleQuery = ModuleQuery.findByEmployeeAndModule(employee,module)
                def object = grailsApplication.getClassForName(module.model.modelClass)
                def fns = Field.where{
                    model{
                        modelClass == object.name
                    }
                }.list()?.fieldName
                def num = object.createCriteria().list(){
                    eq('user',employee.user)
                    if(moduleId == 'public_customer'){
                        eq("customerType", 1)
                    }else if(moduleId == 'customer'){
                        eq("customerType", 2)
                    }else if(moduleId == 'work_audit'){
                        eq('auditor', employee)
                        inList("auditState",[1, 2])
                    }
                    if(moduleId != 'public_customer' && moduleId != 'work_audit'){ // 公害客户不用加该条件
                        or{
                            'in'('employee.id',ids)
                            if(fns?.contains('owner')){
                                'in'('owner.id',ids)
                            }
                        }
                    }
                    if(moduleQuery) {
                        ge('dateCreated',moduleQuery.lastQueryDate)
                    }
                }?.size()
                if(num && num != 0){
                    modulesNum << [moduleId:moduleId,num:num]
                }
                if(!moduleQuery) {
                    moduleQuery = new ModuleQuery(employee: employee,module: module,lastQueryDate: new Date())
                    moduleQuery.save(flush: true)
                }
            }
        }
        render([success:true,data:modulesNum] as JSON)
    }

    /**
     * 更新当前模块上次访问时间
     */
    @Transactional
    def updateModuleLastQueryDate(){
        if(!session.employee || !params.moduleId){
            render(successFalse)
            return
        }
        def statModuleIds = ['customer','public_customer','contract_order','work_audit','my_task']
        if(statModuleIds.contains(params.moduleId)){
            def employee = session.employee
            def module = Module.findByModuleId(params.moduleId)
            def moduleQuery = ModuleQuery.findByEmployeeAndModule(employee,module)
            if(!moduleQuery){
                moduleQuery = new ModuleQuery(employee: employee,module: module,lastQueryDate: new Date())
            }else{
                moduleQuery.lastQueryDate = new Date()
            }
            moduleQuery.save(flush: true)
        }
        render([success:true] as JSON)
    }

    def validateDetailRecord(){
        def details = JSON.parse(params.detail) as List
        def view = View.findByViewIdAndUser(params.viewId,session.employee?.user)
        def moduleId = view.module.moduleId
        def validFields = ViewDetail.createCriteria().list(){
            eq("user",session.employee.user)
            eq("view",view)
        }
        def validFieldsMap = [:]
        validFields.each {
            validFieldsMap << ["$it.userField.fieldName":it.label]
        }
        def errorList = []
        if(validFields.size() > 0){
            details.eachWithIndex {detail,index->
                def obj = ["moduleId":moduleId]
                validFields.each {viewDetail->
                    def fieldName = viewDetail.userField.fieldName
                    obj << ["$fieldName":detail."$fieldName"]
                }
                def errors = validationService.formValidation(obj)
                if(errors.size() > 0){
                    def el = ["index":"$index"]
                    def elList = []
                    errors.each {key,value->
                        elList << validFieldsMap.get("$key") + value
                    }
                    el << ["elList":elList]
                    errorList << el
                }
            }
        }
        render ([success:true,errorList:errorList] as JSON)
    }

    @UserAuthAnnotation
    @Transactional
    def transfer(){
        if(!params.transferIds || !params.owner || !params.transferModule){
            render baseService.error(params)
            return
        }

        // 转移客户
        def owner = params.owner as Long
        def transferIds = JSON.parse(params.transferIds) as List
        def ids = []
        transferIds.each {
            ids << it.toLong()
        }
        def module = Module.findByModuleId(params.transferModule)
        def mainDomain = grailsApplication.getClassForName(module?.getModel()?.modelClass)
        def fns = Field.where{
            model{
                modelClass == mainDomain.name
            }
        }.list()?.fieldName
        def isContainsOwner = fns.contains('owner')?true:false
        def setValue = "employee"
        if(isContainsOwner == true){
            setValue = "owner"
        }
        mainDomain.executeUpdate("update ${module.model.modelName} set $setValue=${params.owner} where id in (:ids)",[ids:ids])

        def result = ([success:true]) as JSON
        if(params.callback) {
            render "${params.callback}($result)"
        }else{
            render result
        }
    }

    /**
     * 判断模块权限
     * @return
     */
    def judgeModulePrivilege(){
        if(!params.privilege){
            render([success:false] as JSON)
            return
        }
        def index = params.privilege.lastIndexOf("_")
        def operate = params.privilege.substring(index+1)
        def employee = Employee.get(session.employee.id)
        def userOperations = privilegeService.getEmployeePrivilegeOperations(employee,operate)
        def allow = false
        def operation = params.privilege
        if(userOperations && userOperations.contains(operation)){
            allow = true
        }
        render([success:allow] as JSON)
    }
}
