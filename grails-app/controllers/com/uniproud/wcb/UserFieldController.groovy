package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import groovy.json.JsonSlurper

import javax.validation.constraints.Null

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@AdminAuthAnnotation
@Transactional(readOnly = true)
class UserFieldController {
    UserService userService
    def baseService
    def moduleService

    def list() {
        if(!params.isExcludePaging)
            RequestUtil.pageParamsConvert(params)
        def query = UserField.where{
            if(params.user){
                user{
                    id  == params.user
                }
            }
            if(params.module){
                module{
                    id  == params.module
                }
            }
            if(params.dbtype){
                if(params.dbtype == 'int'){
                    dbType == 'java.lang.Long' || dbType == 'java.lang.Integer'
                }else{
                    dbType == getDBType(params.dbtype)
                }
            }
        }
        def fields = []
        query.list(params).each{
            def win
            def dbTypeName
            switch (it.dbType){
                case 'java.lang.Integer':
                case 'java.lang.Long':
                    win = 'intField'
                    dbTypeName = '数字'
                    break
                case 'java.lang.String' :
                    win = 'stringField'
                    dbTypeName = '字符'
                    break
                case 'java.util.Date' :
                    win = 'dateField'
                    dbTypeName = '日期'
                    break
                case 'java.lang.Boolean' :
                    win = 'booleanField'
                    dbTypeName = '真假'
                    break
                case 'java.lang.Double' :
                case 'java.math.BigDecimal':
                    win = 'floatField'
                    dbTypeName = '浮点'
                    break
                case 'com.uniproud.wcb.Doc' :
                    win = 'fileField'
                    dbTypeName = '文件'
                    break
                case 'com.uniproud.wcb.Comment' :
                    win = 'fileField'
                    dbTypeName = '评论'
                    break

            }
            fields << [
                        id:it.id,fieldName:it.fieldName,
                        dbType:it.dbType,relation:it.relation,dbTypeName:dbTypeName,
                        max:it.max,min:it.min,bitian:it.bitian,weiyi:it.weiyi,mohu:it.mohu,heji:it.heji,inputType:it.inputType,
                        text: it.text,defValue:it.defValue,note:it.note,scale:it.scale,maxSize:it.maxSize,
                        remark:it.remark,dateFormat:it.dateFormat,regex:it.regex,must:it.must,
                        dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,isMoney:it.isMoney,
                        user:[id:it.user?.id,name:it.user?.name],
                        module:[moduleName:it.module?.moduleName,id:it.module?.id,moduleId:it.module?.moduleId],
                        dict:[id: it.dict?.id,text:it.dict?.text],win:win,pageType:it.pageType
                      ]
        }
        def json = [success:true,data:fields, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @UserAuthAnnotation
    def listForTemplate(){
        def query = UserField.where{
            user == session.employee?.user
            if(params.moduleId){
                module{
                    moduleId  == params.moduleId
                }
            }
            if(params.searchTemplate){
                or{
                    ilike('text',"%"+params.searchTemplate+"%")
                    ilike('fieldName',"%"+params.searchTemplate+"%")
                }
            }
            if(params.dbType){
                eq("dbType",params.dbType)
            }
            if (params.fieldName){
                eq("fieldName",params.fieldName)
            }
        }
        def fields = []
        query.list(params).each{
            if(!it.dbType?.startsWith('com.uniproud.wcb') && !it.fieldName?.endsWith('id') && it.fieldName != 'deleteFlag' ){
                fields << [id:it.id,fieldName:it.fieldName,text: it.text,dbType: it.dbType,dict:it.dict?.id]
            }
        }
        def json = [success:true,data:fields, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def update(UserField userFieldInstance) {
        if (userFieldInstance == null) {
            render(successFalse)
            return
        }
        // 因为前台该字段对应的名字是userFieldPageType,所以要把该值赋值给pageType
        userFieldInstance.properties['pageType'] = params.userFieldPageType
        if (!userFieldInstance.validate()) {
            render([success:false,errors: errorsToResponse(userFieldInstance.errors)] as JSON)
            return
        }
        userFieldInstance.save flush: true
        //更新模块版本号
        event('upumv',[user: userFieldInstance.user,module: userFieldInstance.module])
        render(successTrue)
    }

    /**
     * 更新或新增属性
     * @return
     */
    @Transactional
    def updateProperty() {
        def paramsObj = JSON.parse(params.data);
        if(!paramsObj.objectFieldId){
            render(successFalse)
            return
        }
        UserField uf = UserField.get(paramsObj.objectFieldId.toLong())
        def userFieldExtend = UserFieldExtend.findByParamNameAndUserField(paramsObj.paramName,uf)
        // 新增，如果根据用户字段和属性名称可以找到，则不允许新增
        if("insert".equals(paramsObj.insertOrUpd)){
            if(userFieldExtend != null){
                render ([success:false,msg:"名称重复!"] as JSON)
                return
            }else{
                userFieldExtend = new UserFieldExtend()
            }
        }else{
            userFieldExtend = UserFieldExtend.get(userFieldExtend.getId());
        }
        userFieldExtend.properties = paramsObj
        userFieldExtend.userField = uf
        userFieldExtend.save(flush : true)
        //更新模块版本号
        event('upumv',[user: uf.user,module: uf.module])
        render([success:true,msg:"操作成功！"] as JSON)
    }

    /**
     * 删除属性
     * @return
     */
    @Transactional
    def delProperty() {
        def paramsObj = JSON.parse(params.data);
        if(!paramsObj.userField){
            render(successFalse)
            return
        }
        UserField uf = UserField.get(paramsObj.userField.toLong())
        def userFieldExtend = UserFieldExtend.findByParamNameAndUserField(paramsObj.paramName,uf)
        userFieldExtend.delete();
        event('upumv',[user:uf.user,module:uf.module])
        render([success:true,msg:"操作成功！"] as JSON)
    }

    /**
     * 字段关联属性
     * @return
     */
    @Transactional
    def searchUserFieldExtend(){
        def query = UserFieldExtend.where{
            if(params.userField){
                userField{
                    id  == params.userField
                }
            }
        }
        def fields = []
        query.list().each{
            fields << [
                    id:it.id,paramName:it.paramName,paramValue : it.paramValue,paramType : it.paramType
            ]
        }
        def json = [data:fields] as JSON
        render json
    }

    /**
     * 删除用户字段
     * @return
     */
    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        def userField
        ids.each{
            userField = UserField.get(it.toLong())
            if(userField.must == true && !userField.fieldName.contains(".")){ // 如果must为true，则不允许删除
                render (successFalse)
				return
            }
            // 删除此字段关联的属性数据
            UserFieldExtend.executeUpdate("delete UserFieldExtend where userField.id = :ufid",[ufid: it.toLong()])
            def vds = ViewDetail.findAllByUserField(userField)
            if(vds) {
                ViewDetailExtend.executeUpdate("delete ViewDetailExtend where viewDetail.id in (:ids)", [ids: vds*.id])
            }
            // 删除该字段关联的视图数据
            ViewDetail.executeUpdate("delete ViewDetail where userField.id = :ufid",[ufid:it.toLong()])
            userField.delete()
        }
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
    def searchEnableField() {
        def moduleObj = Module.get(params.module?.toLong())
        // 1、首先查询出，用户字段表里面已经拥有哪些字段的名称
        def userFieldQuery = {
            if(params.user){
                eq('user.id',params.user?.toLong())
            }
            if(moduleObj){
                eq('module.id',moduleObj.id)
            }
            if(params.dbtype){
                if(params.dbtype == 'int'){
                    or{
                        eq('dbType','java.lang.Long')
                        eq('dbType','java.lang.Integer')
                    }
                }else{
                    eq('dbType',getDBType(params.dbtype))
                }
            }
        }
        def userFieldIds = []
        UserField.createCriteria().list(userFieldQuery).each{
            userFieldIds.push(it.fieldName)
        }

        // 2、找出Field里面排除过UserField之后的字段
        def query = {
            or{
                isNull("relation")
                'in'("dbType",["com.uniproud.wcb.Doc","com.uniproud.wcb.Comment"])
            }
            if(moduleObj){
                eq("model.id",moduleObj.model?.id)
            }
            if(params.dbtype){
                if(params.dbtype == 'int'){
                    or{
                        eq("dbType",'java.lang.Long')
                        eq("dbType",'java.lang.Integer')
                    }
                }else{
                    eq("dbType",getDBType(params.dbtype))
                }
            }
            ne("fieldName","version")
            not{like('fieldName','extend%')}
        }
        def fields = []
        Field.createCriteria().list(query).each{
            if(!userFieldIds.contains(it.fieldName)){
                def text = ""
                if('OneToOne'.equals(it.relation)){
                    text = "文件(" + it.fieldName + ")"
                }else if('OneToMany'.equals(it.relation)){
                    text = "多文件(" + it.fieldName + ")"
                }else{
                    text = it.text + "(" + it.fieldName + ")"
                }

                fields << [
                        boxLabel: text, name: 'field',inputValue: it.id
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
     * 启用固定字段
     * @return
     */
    @Transactional
    def enableField(){
        if(!params.data){
            render(successFalse)
            return
        }
        def enableFieldIds = params.data.split(",");
        def user = User.get(params.user.toLong());
        def module = Module.get(params.module?.toLong())
        def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${module.moduleId}.json")
        def json
        if(file.exists()){
            json = new JsonSlurper().parseText(file.text)
        }
        def relatedModelName
        if(params.relatedModelValue) {  // 如果关联对象不为空，说明启用关联字段
            def tempRelatedModelValue = params.relatedModelValue.split("_")
            relatedModelName = tempRelatedModelValue[0]
            if(relatedModelName == "user" || relatedModelName == "module"){
                enableFieldIds.each {
                    def field = Field.get(it.toLong())
                    def uf = new UserField(user:user,module: module,model: module.model)
                    uf.fieldName = relatedModelName + "." + field.fieldName
                    uf.text = field.text ?: field.fieldName
                    uf.dbType = field.dbType
                    uf.pageType = UserService.getPageType(field.dict,field.dbType,field.fieldName,field)
                    uf.relation = field.relation
                    uf.defValue = field.defValue
                    uf.dict = field.dict
                    uf.save(flush: true)
                    def relatedObj = UserField.findByUserAndModuleAndFieldName(user, module, relatedModelName)
                    if (!relatedObj) {
                        def relatedField = Field.findByModelAndFieldName(module.model, relatedModelName)
                        def fieldModel = Model.findByModelClass(relatedField.dbType)
                        def usf = new UserField()
                        usf.properties = relatedField
                        usf.text = fieldModel.remark
                        usf.pageType = UserService.getPageType(relatedField.dict, relatedField.dbType, relatedField.fieldName, relatedField)
                        usf.user = user
                        usf.module = module
                        usf.save(flush: true)
                    }
                    userService.initUserFieldExtend(uf, json?.F?."${uf.fieldName}"?.FE, uf.fieldName)
                }
            }else {
                enableFieldIds.each {
                    def userField = UserField.get(it.toLong())
                    def uf = new UserField(user: user, module: module, model: module.model)
//                uf.properties = userField         //关联字段唯一、必填等属性未必相同
                    uf.fieldName = relatedModelName + "." + userField.fieldName
                    uf.text = userField.text ?: userField.fieldName
                    uf.dbType = userField.dbType
                    uf.pageType = userField.pageType
                    uf.relation = userField.relation
                    uf.defValue = userField.defValue
                    uf.userFieldExtend = null
                    if (json?.F?."${uf.fieldName}") {
                        bindData(uf,json?.F?."${uf.fieldName}",[exclude:'dict'])
                    }
                    uf.dict = userField.dict
                    if (uf.hasErrors()) {
                        log.info uf.errors
                    }
                    uf.save(flush: true)
                    def relatedObj = UserField.findByUserAndModuleAndFieldName(user, module, relatedModelName)
                    if (!relatedObj) {
                        def field = Field.findByModelAndFieldName(module.model, relatedModelName)
                        def fieldModel = Model.findByModelClass(field.dbType)
                        def usf = new UserField()
                        usf.properties = field
                        usf.text = fieldModel.remark
                        usf.pageType = UserService.getPageType(field.dict, field.dbType, field.fieldName, field)
                        usf.user = user
                        usf.module = module
                        usf.save(flush: true)
                    }
                    userService.initUserFieldExtend(uf, json?.F?."${uf.fieldName}"?.FE, uf.fieldName)
                }
            }
        }else{ // 启用非关联字段
            enableFieldIds.each{
                def field = Field.get(it.toLong())
                def dict = DataDict.findByUserAndDataId(user,field.dict)
                def uf = new UserField()
                uf.properties = field
                if(!uf.text){
                    if(uf.dbType == 'com.uniproud.wcb.Doc')
                        uf.text = "文件"
                    else if(uf.dbType == 'com.uniproud.wcb.Comment')
                        uf.text = "评论"
                }
                uf.pageType = UserService.getPageType(field.dict,field.dbType,field.fieldName,field)
                uf.user = user
                uf.module = module
                if(json?.F?."${uf.fieldName}"){
                    uf.properties = json?.F?."${uf.fieldName}"
                }
                uf.properties["dict"]=dict
                uf.save(flush : true)
                userService.initUserFieldExtend(uf,json?.F?."${uf.fieldName}"?.FE,uf.fieldName)
            }
        }
        //更新模块版本号
        event('upumv',[user: user,module: module])
        render([success:true,msg: '启用成功！'] as JSON)
    }

    /**
     * 根据dbType找出java类型
     * @param dbType
     * @return
     */
    def getDBType(dbType){
        def finalDBType
        switch (dbType){
            case 'string':
                finalDBType = 'java.lang.String'
                break
            case 'int':
                finalDBType = 'java.lang.Integer'
                break
            case 'date':
                finalDBType = 'java.util.Date'
                break
            case 'float':
                finalDBType = 'java.lang.Double'
                break
            case 'boolean':
                finalDBType = 'java.lang.Boolean'
                break
            case 'doc':
                finalDBType = 'com.uniproud.wcb.Doc'
                break
        }
        return finalDBType
    }

    /**
     * 查询当前模块下关联的对象
     * 条件：查询关联字段(relation)不为空的记录
     * @return
     */
    @Transactional
    def searchRelatedObject(){
        def module = Module.get(params.module?.toLong())
        def query = {
            isNotNull("relation")
            ne("dbType","com.uniproud.wcb.Doc")
            if(module){
                eq("model.id",module.model?.id)
            }
        }
        def fields = []
        Field.createCriteria().list(query).each{
            def model = Model.findByModelClass(it.dbType)
            fields << [
                dbType: it.fieldName+"_"+it.dbType, fieldName: model?.remark + "(" + it.fieldName + ")"
            ]
        }
        def json = [data:fields] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 查找关联模块包含的字段
     * @return
     */
    @Transactional
    def searchRelatedObjectField() {
        if(!params.relatedModelValue){
            render(successFalse)
        }
        def relatedModelArr = params.relatedModelValue?.split("_")
        def relatedModelName = relatedModelArr[0]
        def relatedModel = Model.findByModelClass(relatedModelArr[1])
        def moduleObj = Module.findByModel(relatedModel)
        // 1、首先查询出主对象里面已经拥有的关联字段的名称
        def userFieldQuery = {
            if(params.user){
                eq("user.id",params.user?.toLong())
            }
            if(params.module){
                eq("module.id",params.module?.toLong())
            }
            if(params.dbtype){
                if(params.dbtype == 'int'){
                    or{
                        eq("dbType",'java.lang.Long')
                        eq("dbType",'java.lang.Integer')
                    }
                }else{
                    eq("dbType",getDBType(params.dbtype))
                }
            }
            like('fieldName',"$relatedModelName.%")
        }

        def userFields = []
        UserField.createCriteria().list(userFieldQuery).each{
            userFields.push(it.fieldName)
        }
        def fields = []
        if(moduleObj) {
            def query = {
                or {
                    isNull("relation")
                    eq("dbType", "com.uniproud.wcb.Doc")
                }
                if (params.user) {
                    eq("user.id", params.user?.toLong())
                }
                if (relatedModelName == 'user') {
                    eq("model", relatedModel)
                } else {
                    eq("module.id", moduleObj?.id)
                }
                if (params.dbtype) {
                    if (params.dbtype == 'int') {
                        or {
                            eq("dbType", 'java.lang.Long')
                            eq("dbType", 'java.lang.Integer')
                        }
                    } else {
                        eq("dbType", getDBType(params.dbtype))
                    }
                }
                not { like('fieldName', "%.%") }
            }
            def fieldName
            UserField.createCriteria().list(query).each {
                fieldName = relatedModelName + "." + it.fieldName
                if (!userFields.contains(fieldName)) {
                    def text = ""
                    if ('OneToOne'.equals(it.relation)) {
                        text = "文件(" + it.fieldName + ")"
                    } else if ('OneToMany'.equals(it.relation)) {
                        text = "多文件(" + it.fieldName + ")"
                    } else {
                        text = it.text + "(" + it.fieldName + ")"
                    }
                    fields << [
                            boxLabel: text, name: 'field', inputValue: it.id
                    ]
                }
            }
        }else{
            def fieldName
            Field.createCriteria().list {
                or {
                    isNull("relation")
                    eq("dbType", "com.uniproud.wcb.Doc")
                }
                eq("model", relatedModel)
                if (params.dbtype) {
                    if (params.dbtype == 'int') {
                        or {
                            eq("dbType", 'java.lang.Long')
                            eq("dbType", 'java.lang.Integer')
                        }
                    } else {
                        eq("dbType", getDBType(params.dbtype))
                    }
                }
                not { like('fieldName', "%.%") }
            }.each{
                fieldName = relatedModelName + "." + it.fieldName
                if (!userFields.contains(fieldName)) {
                    def text = ""
                    if ('OneToOne'.equals(it.relation)) {
                        text = "文件(" + it.fieldName + ")"
                    } else if ('OneToMany'.equals(it.relation)) {
                        text = "多文件(" + it.fieldName + ")"
                    } else if(it.text){
                        text = it.text + "(" + it.fieldName + ")"
                    } else {
                        text = it.fieldName
                    }
                    fields << [
                            boxLabel: text, name: 'field', inputValue: it.id
                    ]
                }
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
     * 查找扩展字段
     * @return
     */
    @Transactional
    def searchExtField() {
        def moduleObj = Module.get(params.module?.toLong())
        // 统计UserField扩展字段各种类型的数量
        def userFieldQuery = {
            if(params.user){
                eq('user.id',params.user?.toLong())
            }
            if(moduleObj){
                eq('module.id',moduleObj.id)
            }
            like('fieldName','extend%')
        }
        def userFields = [:] // 存放扩展字段各种类型的数量
        UserField.createCriteria().list(userFieldQuery).each{
            if(userFields.containsKey(it.dbType)){
                userFields << [(it.dbType):userFields.get(it.dbType) + 1]
            }else{
                userFields << [(it.dbType):1]
            }
        }
        // 统计Field扩展字段各种类型的数量
        def query = {
            if(moduleObj){
                eq("model.id",moduleObj.model?.id)
            }
            like('fieldName','extend%')
        }
        def fields = [:]
        Field.createCriteria().list(query).each{
            if(fields.containsKey(it.dbType)){
                fields << [(it.dbType): fields.get(it.dbType) + 1]
            }else{
                fields << [(it.dbType): 1]
            }
        }
        def extFields = []
        fields.each {dbType,count->
            def ufCount = userFields.get(dbType)
            if(!ufCount) ufCount = 0
            extFields << [dbType:dbType,restCount:count-ufCount]
        }
        def json = [data:extFields] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 启用扩展字段
     */
    @AdminAuthAnnotation
    @Transactional
    def enableExtField(){
        if(!params.user || !params.module){
            render([success:false] as JSON)
            return
        }
        def moduleObj = Module.get(params.module?.toLong())
        // 统计UserField扩展字段各种类型的数量
        def userFieldQuery = {
            if(params.user){
                eq('user.id',params.user?.toLong())
            }
            if(moduleObj){
                eq('module.id',moduleObj.id)
            }
            eq('dbType',params.dbType)
            like('fieldName','extend%')
        }
        def userFields = []
        UserField.createCriteria().list(userFieldQuery).each{
            userFields << it.fieldName
        }
        // 统计Field扩展字段各种类型的数量
        def query = {
            if(moduleObj){
                eq("model.id",moduleObj.model?.id)
            }
            eq('dbType',params.dbType)
            like('fieldName','extend%')
        }
        def field
        Field.createCriteria().list(query).each{
            if(!userFields.contains(it.fieldName)){
                field = it
            }
        }

        def model = Module.get(params.module?.toLong())?.model
        def userField = new UserField()
        userField.properties = field // 先把Field里面的信息拷贝到UserField
        userField.properties = params // 再把本次修改的属性覆盖掉
        userField.pageType = params.userFieldPageType
        if (!userField.validate()) {
            render([success:false,errors: errorsToResponse(userField.errors)] as JSON)
            return
        }
        userField.save flush: true

        def domain = grailsApplication.getClassForName(userField.model?.modelClass)
        userService.initUserFieldExtend(userField,domain?.FE,userField.fieldName)
        //更新模块版本号
        event('upumv',[user: userField.user,module: moduleObj])
        render([success:true] as JSON)
    }

    /**
     * 查找可模糊查询的字段
     * @return
     */
    @Transactional
    def searchMohuSearchField(){
        def moduleObj = Module.get(params.module?.toLong())
        def userFieldQuery = {
            if(params.user){
                eq('user.id',params.user?.toLong())
            }
            if(moduleObj){
                eq('module.id',moduleObj.id)
            }
            eq('dbType',getDBType('string'))
            ne('fieldName','password')
            isNull('dict')
        }
        def fields = []
        UserField.createCriteria().list(userFieldQuery).each{
            def text = it.text + "(" + it.fieldName + ")"
            fields << [boxLabel: text, name: 'field',inputValue: it.id,checked:it.mohu]
        }

        def json = [data:fields] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 设置模糊查询的字段
     */
    @Transactional
    def enableSearchField(){
        def enableFieldIds = params.data?.tokenize(",");
        def user = User.get(params.user.toLong());
        def module = Module.get(params.module?.toLong())
        UserField.findAllByUserAndModuleAndMohu(user,module,true)?.each {
            if(!enableFieldIds?.contains(it.id as String)){//设置为false
                it.mohu=false
                it.save flush: true
            }
            enableFieldIds?.remove(it.id as String)
        }
        enableFieldIds?.each {//剩下的置为true
            def field = UserField.get(it as int)
            field.mohu= true
            field.save flush: true
        }
        //更新模块版本号
        event('upumv',[user: user,module: module])
        render([success:true,msg: '启用成功！'] as JSON)
    }

    /**
     * 查找列表汇总的字段
     * @return
     */
    @Transactional
    def searchHejiField(){
        def moduleObj = Module.get(params.module?.toLong())
        def userFieldQuery = {
            if(params.user){
                eq('user.id',params.user?.toLong())
            }
            if(moduleObj){
                eq('module.id',moduleObj.id)
            }
            or{
                eq('dbType','java.lang.Integer')
                eq('dbType','java.lang.Long')
                eq('dbType','java.lang.Double')
                eq('dbType','java.math.BigDecimal')
            }
            isNull('dict')
            ne('fieldName','id')
            not{ilike('fieldName','%.id')}
        }
        def fields = []
        UserField.createCriteria().list(userFieldQuery).each{
            def text = it.text + "(" + it.fieldName + ")"
            fields << [boxLabel: text, name: 'field',inputValue: it.id,checked:it.heji]
        }

        def json = [data:fields] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    /**
     * 设置模糊查询的字段
     */
    @Transactional
    def enableHejiField(){
        def enableFieldIds = params.data?.tokenize(",");
        def user = User.get(params.user.toLong());
        def module = Module.get(params.module?.toLong())
        UserField.findAllByUserAndModuleAndHeji(user,module,true)?.each {
            if(!enableFieldIds?.contains(it.id as String)){//设置为false
                it.heji=false
                it.save flush: true
            }
            enableFieldIds?.remove(it.id as String)
        }
        enableFieldIds?.each {//剩下的置为true
            def field = UserField.get(it as int)
            field.heji= true
            field.save flush: true
        }
        //更新模块版本号
        event('upumv',[user: user,module: module])
        render([success:true,msg: '启用成功！'] as JSON)
    }
}