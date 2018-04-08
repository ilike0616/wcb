package com.uniproud.wcb

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import org.hibernate.Criteria
import org.hibernate.criterion.CriteriaSpecification
import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@Transactional
class UserService {
    def dataDictService
    def viewService
	def grailsApplication
    /**
     * 初始化所有用户的字段
     * @return`
     */
    def init(){
        //获取所有企业
        def users = User.findAll()
        //初始化，数据字典的数据
//        dataDictService.init(users)
        users.each {//遍历企业
            initUser(it)
            //初始化，用户的视图
            viewService.initUser(it)
        }
    }
    /**
     * 初始化用户的字段
     * @param user
     * @return
     */
    @Transactional
    def initUser(user){
        //初始化，数据字典的数据
        dataDictService.initUser(user)
        //初始化企业菜单
        initUserMenu(user)
        //获取企业，所拥有的模块
        def modules = user.modules
        //遍历企业所拥有的模块
        modules.each { module->
            def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${module.moduleId}.json")
            if(file.exists()) {
                initUserModuleNew(user,module)
            }else {
                initUserModule(user, module)
            }
        }
    }

    /**
     * 初始化所有用户的字段
     * @return
     */
    def initUserModule(module){
        log.info "初始化模块：${module.moduleId}(${module.moduleName}),开始。。。"
        def start = Calendar.instance.timeInMillis
        //获取所有企业
        def users = User.findAll()
        users.each { // 遍历企业
            log.info "初始化，模块：${module.moduleId}，企业：${it.name},开始。"
            def start_user = Calendar.instance.timeInMillis
            def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${module.moduleId}.json")
            if(file.exists()) {
                initUserModuleNew(it,module)
                //初始化，用户的视图
                viewService.initUserViewNew(it,module)
            }else {
                initUserModule(it, module)
                //初始化，用户的视图
                viewService.initUserView(it,module)
            }

            log.info "初始化，模块：${module.moduleId}，企业：${it.name},结束,耗时${(Calendar.instance.timeInMillis - start_user)/1000}秒。"
        }
        def end = Calendar.instance.timeInMillis
        log.info "初始化模块：${module.moduleId}(${module.moduleName}),结束。耗时${(end-start)/1000}秒"
    }
    /**
     * 初始化用户的字段
     * @param user
     * @return
     */
    @Transactional
    def initUserModule(user,module){
        //初始化，企业所拥有的操作
        initUserOperation(user,module)

        //获取该模块对应的数据模型
        def model = module.model
        //获取对应模块所有的字段集合
        def fields = model?.fields
        //得到对应模块的domain全路径
        def modelClass = module.model?.modelClass
        //通过反射获取，domain对象
        def domain = grailsApplication.getClassForName(modelClass)
        //获取需要必填的字段集合
        def bts = domain?.FE?.bitian
        //针对初始化视图中的所有字段，进行去重操作
        def allfields = domain?.allfields?.unique()
        //获取所有字段中，需要关联其他数据模型的字段，需要进行特殊处理
        def values = allfields.findAll{element->element.contains('.')}
        //获取所有带“.”字段的，关联模型字段
        def glField = []
        allfields.each{element->
            if(element.contains('.')){
                glField << element.tokenize('.')[0]
            }
        }
        //如果把关联模型这些字段增加到，需要插入UserField的字段集合中。如果有重复的，会被排除掉
        if(glField){
            allfields = allfields + glField
            allfields = allfields.unique()
        }
        //遍历所有关联的字段
        values.each {value->
            //对字段名进行按“.”分割，成为数组，只考虑带1个点的情况
            def t1 = value.tokenize('.')
            //获取实际关联数据模型的信息
            def f = Field.where{
                model == model
                fieldName == t1[0]
            }.find()
            //获取关联模型中的字段，在实际存在的数据模型的信息
            def uf = Field.where{
                model==Model.findByModelClass(f.dbType)
                fieldName == t1[1]
            }.find()
            if(uf){
                def pageType = getPageType(uf.dict,uf.dbType,uf.fieldName,uf)
                def dict = DataDict.findByUserAndDataId(user,uf.dict)
                def field = new UserField(user:user,dict: dict)
                field.properties = uf
                if(!uf.text){
                    field.text = uf.fieldName
                }
                field.pageType = pageType
                field.fieldName = value
                field.model = model
                field.module = module
                field.properties["dict"]=dict
                if(bts?.contains(uf.fieldName)){
                    field.bitian = true
                }
                if (field.hasErrors()) {
                    println field.errors
                }
                field.save()
                initUserFieldExtend(field,domain?.FE,value)
            }
        }
        fields.each {
            if (it.must||allfields?.contains(it.fieldName)){
                def pageType = getPageType(it.dict,it.dbType,it.fieldName,it)
                def dict = DataDict.findByUserAndDataId(user,it.dict)
                def field = new UserField(user:user,dict: dict)
                field.pageType = pageType
                field.properties = it
                if(!it.text){
                    field.text = it.fieldName
                }
                if(bts?.contains(it.fieldName)){
                    field.bitian = true
                }
                field.properties["dict"]=dict
                field.module = module
                field.save()
                initUserFieldExtend(field,domain?.FE,it.fieldName)
            }
        }
    }

    /**
     * 初始化用户的字段
     * @param user
     * @return
     */
    @Transactional
    def initUserModuleNew(user,module){
        //初始化，企业所拥有的操作
        initUserOperation(user,module)
        //获取该模块对应的数据模型
        def model = module.model
        //获取对应模块所有的字段集合
        def fields = model?.fields
        //得到对应模块的domain全路径
//        def modelClass = module.model?.modelClass
        //通过反射获取，domain对象
//        def domain = grailsApplication.getClassForName(modelClass)
        //获取自定义配置文本
        def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${module.moduleId}.json")
        if(file.exists()) {
            def json = new JsonSlurper().parseText(file.text)
            if (json?.F) {
                //把配置项FE保存到FIELD中
                json?.F?.each { fieldName, props ->
                    def field = Field.findByModelAndFieldName(model, fieldName)
                    if (field) {
                        field.properties = props
                        field.save()
                    } else {
                        //目前modelService已创建，除非domian中不存在字段目前不会执行此处
                    }
                }
            }
            if (json?.V) {
                //针对初始化视图中的所有字段，进行去重操作
                def allfields = json?.V?.collect{viewId,props->
                    def detailPropes = props?.viewDetail
                    if(detailPropes.getClass().getName()?.endsWith("Map")) {
                        props?.viewDetail?.collect { detailName, detailProp ->
                            detailName
                        }
                    }else{
                        detailPropes
                    }
                }?.flatten().unique()
                //获取所有字段中，需要关联其他数据模型的字段，需要进行特殊处理
                def values = allfields.findAll{element->element.contains('.')}
                //获取所有带“.”字段的，关联模型字段
                def glField = []
                allfields.each{element->
                    if(element.contains('.')){
                        glField << element.tokenize('.')[0]
                    }
                }
                //如果把关联模型这些字段增加到，需要插入UserField的字段集合中。如果有重复的，会被排除掉
                if(glField){
                    allfields = allfields + glField
                    allfields = allfields.unique()
                }
                //遍历所有关联的字段
                values.each { value ->
                    if(!UserField.findByUserAndModuleAndFieldName(user,module,value)) {
                        //对字段名进行按“.”分割，成为数组，只考虑带1个点的情况
                        def t1 = value.tokenize('.')
                        //获取实际关联数据模型的信息
                        def f = Field.where {
                            model == model
                            fieldName == t1[0]
                        }.find()
                        //获取关联模型中的字段，在实际存在的数据模型的信息
                        def uf = Field.where {
                            model == Model.findByModelClass(f.dbType)
                            fieldName == t1[1]
                        }.find()
                        if (uf) {
                            def pageType = getPageType(uf.dict, uf.dbType, uf.fieldName, uf)
                            def dict = DataDict.findByUserAndDataId(user, uf.dict)
                            def field = new UserField(user: user, module: module, model: model, dict: dict)
                            field.text = uf.text ?: uf.fieldName
                            field.dbType = uf.dbType
                            field.relation = uf.relation
                            field.pageType = pageType
                            field.fieldName = value
                            field.defValue = uf.defValue
                            if (json?.F?."$value") {
                                field.properties = json?.F?."$value"
                            }
                            //数据字典写在后边
                            field.properties["dict"] = dict
                            if (field.hasErrors()) {
                                println field.errors
                            }
                            field.save()
                            initUserFieldExtend(field, json?.F?."$value"?.FE, value)
                        }
                    }
                }
                fields.each {
                    if ((it.must||allfields?.contains(it.fieldName)) && !UserField.findByUserAndModuleAndFieldName(user,module,it.fieldName)){
                        def pageType = getPageType(it.dict,it.dbType,it.fieldName,it)
                        def dict = DataDict.findByUserAndDataId(user,it.dict)
                        def field = new UserField(user:user,dict: dict)
                        field.pageType = json?.F?."${it.fieldName}"?.pageType?:pageType
                        field.properties = it
                        if(!it.text){
                            field.text = it.fieldName
                        }
                        field.properties["dict"]=dict
                        field.module = module
                        if (field.hasErrors()) {
                            println field.errors
                        }
                        field.save()
                        initUserFieldExtend(field,json?.F?."${it.fieldName}"?.FE,it.fieldName)
                    }
                }
            }
        }
    }

	/**
	 * 初始化用户菜单
	 * @param user
	 * @return
	 */
    @Transactional
	def initUserMenu(user){
		def modules = queryModule(user,null)
		def i = 0
		modules.each {parent->
			def menu = new UserMenu(user:user,module:parent,text:parent.moduleName,idx:i).save()
			i++
			queryModule(user,parent).each{children->
				new UserMenu(user:user,module:children,text:children.moduleName,idx:i,parentUserMenu:menu).save()
				i++
			}
		}
	}
	/**
	 * 初始化用户菜单
	 * @param user
	 * @param parentModule
	 * @return
	 */
	def queryModule(user,parentModule){
		def list = Module.where {
			if(parentModule){
				parentModule == parentModule
			}else{
				parentModule == null
			}
			users{
				id == user.id
			}
		}.list(sort:'id',order:'ASC')
        return list
	}
	/**
	 * 初始化选中操作按钮
	 * @param user
	 * @param module
	 * @return
	 */
    @Transactional
	def initUserOperation(user,module){
		def privilege = Privilege.findByUserAndName(user,'系统管理员')
		if(!privilege){
			privilege = new Privilege(user:user,name:'系统管理员').save()
		}
		def list = Operation.where{
			module == module
			defSelected == true
		}.list()
		list.each {opt->
            if(!UserOperation.findByUserAndModuleAndOperation(user,module,opt)) {
                def up = new UserOperation(user: user, operation: opt, module: module, text: opt.text, iconCls: opt.iconCls).save()
                privilege?.addToUserOperation(up);
            }
		}
		privilege?.save()
	}
	/**
	 * 初始化 字段的扩展属性
	 * @param field     字段对象
	 * @param FE        扩展属性的map
	 * @param fieldName 字段名称
	 * @return
	 */
	@Transactional
	def initUserFieldExtend(field,FE,fieldName){
		def kinds = ['pub','pc','mobile']
		kinds.each{kind->
			FE?.get(kind)?.get(fieldName)?.each {
				def paramType = "String"
				switch (it.value?.class.name) {
					case 'java.lang.Integer':
					case 'java.lang.Long':
						paramType = 'Integer'
						break
					case 'java.lang.String':
						paramType = 'String'
						break
					case 'java.util.Date':
						paramType = 'Date'
						break
					case 'java.lang.Boolean':
						paramType = 'Boolean'
						break
					case 'java.lang.Double':
					case 'java.lang.Float':
						paramType = 'Float'
						break
				}
				new UserFieldExtend(userField:field,paramName:it.key,paramValue:it.value,paramType:paramType,kind:kind).save()
			}
		}
	}
	
	
    /**
     * 得到页面类型
     * @param dict 数据字典
     * @param dbTyp 数据库类型
     * @param fieldName 字段名称
     * @return
     */
    def static getPageType(dict,dbType,fieldName,field){
        def pageType
        if(!dict) {
            switch (dbType) {
                case 'java.lang.Integer':
                case 'java.lang.Long':
                    pageType = 'numberfield'
                    break
                case 'java.lang.String':
                    pageType = 'textfield'
                    break
                case 'java.util.Date':
                    pageType = 'datetimefield'
                    break
                case 'java.lang.Boolean':
                    pageType = 'checkbox'
                    break
                case 'java.lang.Double':
                case 'java.math.BigDecimal':
                    pageType = 'numberfield'
                    break
                case 'com.uniproud.wcb.Doc':
					if(field.relation?.contains('ToMany')){
						pageType = 'baseUploadField'
					}else if(field.relation?.contains('ToOne')){
						pageType = 'baseImageField'
					}
                    break
            }
        }else{
            pageType = 'combo'
        }
		if(fieldName.contains('remark')||fieldName.contains('content')||fieldName.contains('plans')||fieldName.contains('summarys')){
			pageType = 'textarea'
		}
		if(fieldName=='birthday'){
			pageType = 'datefield'
		}
        if(fieldName=='id'|| fieldName.contains('.id')
           || fieldName=='longtitude' || fieldName=='latitude'){
            pageType = 'hidden'
        }
        return pageType
    }

    def copyVersionInfo(Long id){
        def user = User.get(id)
        if(user){
            // 删除以前用户信息
            deleteUserInfo(user)
            println "-------------------------------------------------------"
            // 重新按照新版本拷贝用户信息
            def userEdition = Edition.get(user.edition.id)
            if(userEdition?.templateUser){
                def copyUser = User.get(userEdition?.templateUser?.id)
                this.copyUserInfo(copyUser,user)
            }
        }
        log.info("版本切换成功！")
    }

    /**
     * 拷贝用户所有的信息，包括：
     * 1、module     2、userMenu      3、userField
     * 4、view       5、viewDetail    6、viewOperation
     * 7、userOperation 8、生成管理权限 9、权限绑定所有操作
     * 10、dataDict     11、userPortal
     * @param copyUser 需要拷贝的对象
     * @param newUser 新的对象
     */
    def copyUserInfo(copyUser,newUser){
        newUser = User.findByUserId(newUser.userId)
        // 拷贝module
        if(newUser.useSysTpl==false) {//如果不使用系统自定义模板，则copy
            log.info('拷贝module开始...')
            copyUser.modules.each { m ->
                newUser.addToModules(m)
            }
            newUser.save(flush: true)
            log.info('拷贝module结束...')
            // 拷贝userMenu
            log.info('拷贝userMenu开始...')
            copyUserMenu(copyUser,newUser)
            log.info('拷贝userMenu结束...')
            // 拷贝userField
            log.info('拷贝userField开始...')
            copyUserField(copyUser,newUser)
            log.info('拷贝userField结束...')
            // 拷贝userOperation
            log.info('拷贝AssociatedRequired开始...')
            copyAssociatedRequired(copyUser,newUser)
            log.info('拷贝AssociatedRequired结束...')
            // 拷贝userOperation
            log.info('拷贝userOperation开始...')
            copyUserOperation(copyUser,newUser)
            log.info('拷贝userOperation结束...')
            // 拷贝view,viewDetail,viewOperation
            log.info('拷贝view开始...')
            copyUserView(copyUser,newUser)
            log.info('拷贝view结束...')
            // 拷贝userPortal
            log.info('拷贝userPortal开始...')
            copyUserPortal(copyUser,newUser)
            log.info('拷贝userPortal结束...')
        }
        // 新增管理员及管理员权限,管理员权限绑定所有操作
        log.info('拷贝权限开始...')
        copyUserPrivilege(newUser)
        log.info('拷贝权限结束...')
        if(newUser.useSysTpl==false) {//如果不使用系统自定义模板，则copy
            // 拷贝dataDict
            log.info('拷贝dataDict开始...')
            copyUserDataDict(copyUser, newUser)
            log.info('拷贝dataDict完成')
        }
        log.info('拷贝消息提醒开始...')
        copyUserNotifyModel(copyUser,newUser)
        log.info('拷贝消息提醒结束...')
//        log.info('拷贝SFA开始...')
//        copyUserSfa(copyUser,newUser)
//        log.info('拷贝SFA结束...')
        if(newUser) {
            newUser.versionCopyState = 2 // copy状态
            newUser.save flush: true
        }
    }

    /**
     * 清除用户信息
     * @param user
     */
    def deleteUserInfo(user){
        // 删除用户模块
        log.info('删除userModule开始...')
        def modules = []
        user.modules?.each {
            modules << it
        }
        modules.each {
            user.removeFromModules(it)
        }
        log.info('删除userModule结束...')
        // 删除用户菜单
        log.info('删除userMenu开始...')
        deleteUserMenu(user)
        log.info('删除userMenu结束...')
        // 删除AssociatedRequired
        log.info('删除AssociatedRequired开始...')
        deleteAssociatedRequired(user)
        log.info('删除AssociatedRequired结束...')
        // 删除用户字段
        log.info('删除userField开始...')
        deleteUserField(user)
        log.info('删除userField结束...')
        // 删除用户视图
        log.info('删除view开始...')
        deleteUserView(user)
        log.info('删除view结束...')
        // 删除权限关系
        log.info('删除privilege开始...')
        deleteUserPrivilegeRelation(user)
        log.info('删除privilege结束...')
        // 删除用户操作
        log.info('删除userOperation开始...')
        deleteUserOperation(user)
        log.info('删除userOperation结束...')
        // 删除数据字典
        log.info('删除dataDict开始...')
        deleteUserDataDict(user)
        log.info('删除dataDict结束...')
        // 删除用户Portal
        log.info('删除userPortal开始...')
        deleteUserPortal(user)
        log.info('删除userPortal结束...')
        // 删除消息模型
        log.info('删除消息模型开始...')
        deleteNotifyModel(user)
        log.info('删除消息模型结束...')
        // 删除SFA
        log.info('删除SFA开始...')
        deleteUserSfa(user)
        log.info('删除SFA结束...')
        user.save(flush: true)
    }

    /**
     * 拷贝用户菜单
     * @param copyUser
     * @param newUser
     */
    def copyUserMenu(copyUser,newUser){
        def userMenuQuery = UserMenu.where {
            user == copyUser
            parentUserMenu == null
        }
        userMenuQuery.list().each {um->
            def userMenu = new UserMenu(um.properties)
            userMenu.user = newUser
            userMenu.children = null
            um.children?.each {umc->
                def userMenuChild = new UserMenu(umc.properties)
                userMenuChild.user = newUser
                userMenuChild.parentUserMenu = userMenu
                userMenuChild.children = null
                userMenu.addToChildren(userMenuChild)
            }
            userMenu.save(flush: true)
        }
    }

    /**
     * 拷贝用户字段
     * @param copyUser
     * @param newUser
     */
    def copyUserField(copyUser,newUser){
        def userFieldQuery = UserField.where {
            user == copyUser
        }
        userFieldQuery.list().each {uf->
            def userField = new UserField(uf.properties)
            userField.user = newUser
            userField.userFieldExtend = null
            uf.userFieldExtend?.each {ufe->
                def userFieldExtend = new UserFieldExtend(ufe.properties)
                userField.addToUserFieldExtend(userFieldExtend)
            }
            userField.save()
        }
    }

    /**
     * 拷贝视图、视图明细、视图操作
     * @param copyUser
     * @param newUser
     */
    def copyUserView(copyUser,newUser){
        def newUserFields = [:]
        UserField.where {
            user == newUser
        }.each {
            def key = it.module.moduleName + "_" + it.fieldName
            newUserFields << ["$key":it]
        }
        def viewQuery = View.where {
            user == copyUser
        }
        viewQuery.list().each {v->
            def view = new View(v.properties)
            view.user = newUser
            view.viewExtend = null
            view.viewDetails = null
            // viewExtend
            v.viewExtend?.each {ve->
                view.addToViewExtend(new ViewExtend(ve.properties))
            }
            // viewDetail
            v.viewDetails?.each {vd->
                def viewDetail = new ViewDetail(vd.properties)
                viewDetail.user = newUser
                def key = v.module.moduleName + "_" + vd.userField?.fieldName
                def field = newUserFields.get("$key")
                viewDetail.userField = field
                viewDetail.viewDetailExtend = null
                // viewDetailExtend
                vd.viewDetailExtend?.each {vde->
                    viewDetail.addToViewDetailExtend(new ViewDetailExtend(vde.properties))
                }
                view.addToViewDetails(viewDetail)
            }
            view.save()
            def voQuery = ViewOperation.where {
                view == v
            }
            voQuery.list()?.each {vo->
                def viewOperation = new ViewOperation(vo.properties)
                viewOperation.view = view
                def uo = UserOperation.where {
                    user == newUser
                    operation == vo.userOperation.operation
                }.find()
                viewOperation.userOperation = uo
                viewOperation.save()
            }
        }
    }

    /**
     * 拷贝管理员权限,管理员权限绑定所有用户操作
     * @param newUser
     */
    def copyUserPrivilege(newUser){
        def privilege = Privilege.findByNameAndUser('系统管理员',newUser)
        if(!privilege){
            privilege = new Privilege(user:newUser,name:'系统管理员').save()
        }
        def useUser = newUser
        if(newUser.useSysTpl==true) {//如果不使用系统自定义模板，则copy
            useUser = newUser.edition.templateUser
        }
        def userOperationQuery = UserOperation.where {
            user == useUser
        }
        def userOperationIds = []
        userOperationQuery.list()?.each {uo->
            userOperationIds << uo.id
        }
        privilege.properties['userOperation'] = userOperationIds
        //工作台
        UserPortal.findAllByUser(useUser).each {up->
            new PrivilegeUserPortal(privilege: privilege,userPortal: up,user: newUser).save()
        }
        privilege.save(flush: true)
    }

    /**
     * 拷贝用户操作
     * @param copyUser
     * @param newUser
     */
    def copyUserOperation(copyUser,newUser){
        def userOperationQuery = UserOperation.where {
            user == copyUser
        }
        userOperationQuery.list()?.each {up->
            // 用户操作
            def userOperation = new UserOperation(up.properties)
            userOperation.user = newUser
            userOperation.userOptCondition = null
            // 用户操作关联的条件
            up.userOptCondition?.each {uoc->
                def userOptCondition = new UserOptCondition(uoc.properties)
                userOptCondition.user = newUser
                userOptCondition.userOptConditionDetail = null
                // 条件明细
                uoc.userOptConditionDetail?.each {uocd->
                    userOptCondition.addToUserOptConditionDetail(new UserOptConditionDetail(uocd.properties))
                }
                userOperation.addToUserOptCondition(userOptCondition)
            }
            userOperation.save()
        }
    }

    /**
     * 拷贝AssociatedRequired
     * @param copyUser
     * @param newUser
     */
    def copyAssociatedRequired(copyUser,newUser){
        def associatedRequiredQuery = AssociatedRequired.where {
            user == copyUser
        }
        associatedRequiredQuery.list().each {ar->
            def associatedRequired = new AssociatedRequired(ar.properties)
            associatedRequired.user = newUser
            associatedRequired.userFields = null
            ar.userFields?.each {uf->
                def userField = UserField.findByUserAndModuleAndFieldName(newUser,uf.module,uf.fieldName)
                associatedRequired.addToUserFields(userField)
            }
            associatedRequired.save()
        }
    }

    /**
     * 拷贝用户字段
     * @param copyUser
     * @param newUser
     */
    def copyUserDataDict(copyUser,newUser){
        def dataDictQuery = DataDict.where {
            user == copyUser
        }
        dataDictQuery.list().each {dd->
            def dataDict = new DataDict(dd.properties)
            dataDict.user = newUser
            dataDict.fields = null
            dataDict.items = null
            dd.items?.each {ddi->
                def dataDictItem = new DataDictItem(ddi?.properties)
                dataDictItem.dict = null
                dataDictItem.user = newUser
                dataDict.addToItems(dataDictItem)
            }
            dataDict.save()
        }
    }

    /**
     * 拷贝工作台
     * @param copyUser
     * @param newUser
     */
    def copyUserPortal(copyUser,newUser){
        def userPortalQuery = UserPortal.where {
            user == copyUser
        }
        userPortalQuery.list()?.each {up->
            def userPortal = new UserPortal(up.properties)
            userPortal.user = newUser
            userPortal.save()
        }
    }

    /**
     * 拷贝消息提醒
     * @param copyUser
     * @param newUser
     */
    def copyUserNotifyModel(copyUser,newUser){
        def notifyModelQuery = NotifyModel.where {
            user == copyUser
            isAllowForbid == false
            deleteFlag == false
        }
        notifyModelQuery.list().each {nm->
            // 消息模型
            def notifyModel = new NotifyModel(nm.properties)
            notifyModel.user = newUser
            notifyModel.notifyModelFilter = null
            notifyModel.employee = null
            notifyModel.save()
            if(nm.notifyModelFilter){
                def newNotifyModelFilter = new NotifyModelFilter(nm.notifyModelFilter.properties)
                newNotifyModelFilter.user = newUser
                newNotifyModelFilter.employee = null
                newNotifyModelFilter.parentNotifyModelFilter = null
                newNotifyModelFilter.children = null
                newNotifyModelFilter.filterDetail = null
                newNotifyModelFilter.notifyModel = notifyModel
                newNotifyModelFilter.save(flush: true)
                NotifyModelFilterDetail.where {
                    notifyModelFilter == nm.notifyModelFilter
                }.list().each {nmfd->
                    def notifyModelFilterDetail = new NotifyModelFilterDetail(nmfd.properties)
                    notifyModelFilterDetail.user = notifyModel.user
                    notifyModelFilterDetail.employee = null
                    notifyModelFilterDetail.notifyModelFilter = newNotifyModelFilter
                    notifyModelFilterDetail.save()
                }
                copyNotifyModelFilterChildren(nm.notifyModelFilter.children,newNotifyModelFilter)
                notifyModel.notifyModelFilter = newNotifyModelFilter
                notifyModel.save(flush: true)
            }
        }
    }

    def copyUserSfa(copyUser,newUser){
        Sfa.findAllByUser(copyUser).each {s->
            def sfa = new Sfa(s.properties)
            sfa.user = newUser
            sfa.detail = []     //拷贝属性时新实体和旧实体不能引用同一个集合
            sfa.save()
            def detail = []
            SfaEvent.findAllByUserAndSfa(copyUser,s).each {se->
                def sfaEvent = new SfaEvent(se.properties)
                sfaEvent.user = newUser
                sfaEvent.sfa = sfa
                sfa.save()
                detail << sfaEvent
            }
            sfa.detail = detail
            sfa.save()
        }
    }

    def copyNotifyModelFilterChildren(notifyModelFilters,parentNotifyModelFilter){
        notifyModelFilters.each {nmf->
            def newNotifyModelFilter = new NotifyModelFilter(nmf.properties)
            newNotifyModelFilter.user = parentNotifyModelFilter.user
            newNotifyModelFilter.parentNotifyModelFilter = parentNotifyModelFilter
            newNotifyModelFilter.children = null
            newNotifyModelFilter.filterDetail = null
            newNotifyModelFilter.notifyModel = parentNotifyModelFilter.notifyModel
            newNotifyModelFilter.save()
            NotifyModelFilterDetail.where {
                notifyModelFilter == nmf
            }.list().each {nmfd->
                def notifyModelFilterDetail = new NotifyModelFilterDetail(nmfd.properties)
                notifyModelFilterDetail.user = parentNotifyModelFilter.user
                notifyModelFilterDetail.notifyModelFilter = newNotifyModelFilter
                notifyModelFilterDetail.save()
            }

            def childs = []
            nmf.getChildren().each{
                if(it.deleteFlag == false){
                    childs << it
                }
            }
            if(childs && childs.size() > 0){
                copyNotifyModelFilterChildren(childs,newNotifyModelFilter)
            }
        }
    }

    /**
     * 删除用户字段
     * @param user
     */
    def deleteUserField(user){
        def userFieldQuery = UserField.where {
            user == user
        }
        userFieldQuery.list().each {uf->
            uf.delete()
        }
    }

    /**
     * 删除AssociatedRequired
     * @param user
     */
    def deleteAssociatedRequired(user){
        def associatedRequiredQuery = AssociatedRequired.where {
            user == user
        }
        associatedRequiredQuery.list().each {
            it.delete()
        }
    }

    /**
     * 删除用户菜单
     * @param user
     */
    def deleteUserMenu(user){
        def userMenuQuery = UserMenu.where {
            user == user
            parentUserMenu == null
        }
        userMenuQuery.list().each {um->
            UserMenu.where {
                parentUserMenu == um
            }.list().each {
                it?.delete()
            }
            um?.delete()
        }
    }

    /**
     * 删除视图信息
     * @param user
     * @return
     */
    def deleteUserView(user){
        def viewQuery = View.where {
            user == user
        }
        viewQuery.list().each {view->
            // 删除视图对应的操作
            ViewOperation.executeUpdate('delete ViewOperation where view = :view',[view:view])
            view.delete()
        }
    }

    /**
     * 删除权限
     * @param user
     * @return
     */
    def deleteUserPrivilegeRelation(user){
        def privilegeQuery = Privilege.where {
            user == user
        }
        privilegeQuery.list().each {privilege->
            def userOperations = []
            privilege.userOperation?.each {uo->
                userOperations << uo
            }
            userOperations.each {
                privilege.removeFromUserOperation(it)
            }
        }
    }

    /**
     * 删除用户字典
     * @param user
     */
    def deleteUserDataDict(user){
        def dataDictQuery = DataDict.where {
            user == user
        }
        dataDictQuery.list().each {dd->
            dd.delete()
        }
    }

    /**
     * 删除用户portal
     * @param user
     */
    def deleteUserPortal(user){
        PrivilegeUserPortal.findAllByUser(user)*.delete()
        EmployeePortal.findAllByUser(user)*.delete()
        UserPortal.findAllByUser(user)*.delete()
    }

    /**
     * 删除用户portal
     * @param user
     */
    def deleteNotifyModel(user){
        NotifyModelFilter.executeUpdate("update NotifyModelFilter set parentNotifyModelFilter=null where user=:user",[user:user])
        Notify.executeUpdate("delete from Notify where user=:user",[user:user])
        EmployeeNotifyModel.executeUpdate("delete from EmployeeNotifyModel where user=:user",[user:user])
        NotifyModel.executeUpdate("delete from NotifyModel where user=:user",[user:user])
    }
    /**
     * 删除用户SFA
     * @param user
     */
    def deleteUserSfa(user){
        SfaEventExecute.executeUpdate("delete from SfaEventExecute where user=:user",[user:user])
        SfaExecute.executeUpdate("delete from SfaExecute where user=:user",[user:user])
        SfaEvent.executeUpdate("delete from SfaEvent where user=:user",[user:user])
        Sfa.executeUpdate("delete from Sfa where user=:user",[user:user])
    }

    /**
     * 删除用户操作
     * @param user
     */
    def deleteUserOperation(user){
        def userOperationQuery = UserOperation.where {
            user == user
        }
        userOperationQuery.list().each {uo->
            uo.delete()
        }
    }

    /**
     * 处理当前用户里面不包含新的集合里面的对象，说明以前有现在没有了
     * @param user
     * @param userModules
     * @param newModules
     * @return
     */
    def deleteUserOperationSomeRelation(user,userModules,newModules){
        // 获取该用户的权限
        def userPrivileges = Privilege.where {
            user == user
            userOperation != null
        }.list()
        userModules.each {
            if(!newModules.contains(it.toInteger())){
                def tempModuleId = it.toLong()
                def tempModule = Module.get(tempModuleId)
                // 获取该用户某模块的用户操作
                def uos = UserOperation.where {
                    user == user
                    module == tempModule
                }.list()
                // 删除所有的视图操作绑定用户操作的关系
                uos.each {uo->
                    ViewOperation.where {
                        userOperation == uo
                    }.list().each {vo->
                        vo.delete()
                    }
                }

                // 删除用户Portal
                def userPortals = UserPortal.where {
                    user == user
                    portal.module.id == tempModuleId
                }.list()
                if(userPortals){
                    EmployeePortal.executeUpdate("delete from EmployeePortal where userPortal in (:userPortals)",[userPortals:userPortals])
                }
                userPortals.each {up->
                    PrivilegeUserPortal.executeUpdate("delete from PrivilegeUserPortal where userPortal=:userPortal",[userPortal: up])
                    up.delete()
                }

                //ViewOperation.executeUpdate("delete ViewOperation where userOperation in :uos",[uos:uos])
                // 删除用户操作时，判断权限是否绑定，如果绑定，应该先解除绑定再删除
                userPrivileges.each {privilege->
                    uos.each {uo->
                        if(privilege.userOperation.contains(uo)){
                            privilege.removeFromUserOperation(uo)
                        }
                    }
                    privilege.save(flush: true)
                }
                uos.each {uo->
                    uo.delete()
                }
                // 删除用户菜单
                def menu = UserMenu.findByUserAndModule(user,tempModule)
                if(menu?.parentUserMenu == null){
                    UserMenu.where {
                        parentUserMenu == menu
                    }.list().each {
                        it?.delete()
                    }
                    menu?.delete()
                }else{
                    menu?.delete()
                }
            }
        }
    }

}
