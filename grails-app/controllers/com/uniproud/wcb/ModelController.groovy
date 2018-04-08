package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import org.omg.CORBA.FieldNameHelper;

import grails.converters.JSON
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@Transactional(readOnly = true)
class ModelController {
   	def modelService
	def moduleVersionService

   def list(){
        RequestUtil.pageParamsConvert(params)
        def list = Model.list(params)
        def models = []
        list.each{
        	models << [id:it.id,modelClass:it.modelClass,modelName:it.modelName,remark:it.remark]
        } 
        def json = [success:true,data:models, total: Model.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }


    def init(){
        modelService.init()
    	/*modelService.initAll()
        def json = [success:true,msg:'初始化成功!'] as JSON
        log.info json 
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }*/
    }

    @Transactional
    def insert(Model modelInstance) {
        if (modelInstance == null) {
            render([success:false] as JSON)
            return
        }
        if (!modelInstance.validate()) {
            render([success:false,errors: errorsToResponse(modelInstance.errors)] as JSON)
            return
        }
        modelInstance.save flush: true
        render([success:true] as JSON)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Model.executeUpdate("delete Model where id in (:ids)",[ids:ids*.toLong()])
        render([success:true] as JSON )
    }


    @Transactional
    def update(Long id) {
        def modelInstance = Model.get(id)
        modelInstance.properties = params
        if(!modelInstance.validate()) {
            render([success:false,errors: errorsToResponse(modelInstance.errors)] as JSON)
            return
        }
        modelInstance.save flush: true
        render([success:true] as JSON)
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def modelInstance
        data.each {
            modelInstance = Model.get(it.id)
            modelInstance.properties = it
            if(!modelInstance.validate()) {
                render([success:false,errors: errorsToResponse(modelInstance.errors)] as JSON)
                return
            }
        }
        modelInstance.save(flush:true)
        render([success:true] as JSON )
    }

	/**
	 * 生成PC端model
	 * 需要登陆状态，并且传递参数 modelName
	 * @return
	 */
    @UserAuthAnnotation
    def index(){
        def modelAndModule = modelService.getModuleAndModelForModuleId(params.moduleId)
		def user = User.findById(session.employee?.user.id)
		def useUser = user
		if(user.useSysTpl==true){
			useUser = user.edition.templateUser
		}
        def q = UserField.where {
            module == modelAndModule.module
            user == useUser
        }
        def fields = []
        q.list().each {
            def attr = [:]
            attr.put('name',it.fieldName)
//			attr.put('allowNull',false)
            switch (it.dbType){
                case 'java.lang.Integer':
                case 'java.lang.Long':
                    //attr.put('type','int')
                    break
                case 'java.lang.String' :
                    attr.put('type','string')
                    break
                case 'java.util.Date' :
                    attr.put('type','date')
//					attr.put('dateFormat','Y-m-d h:m:s')
                    break
                case 'java.lang.Boolean' :
                    attr.put('type','boolean')
                    break
                case 'java.lang.Double' :
                    attr.put('type','float')
                    break
            }
            if(it.defValue) {
                attr.put('defaultValue', it.defValue)
            }
			//读取扩展属性，kind为pub的在前
			it.userFieldExtend?.each{ext->
				if(ext.kind=='pub'){
					attr << ["$ext.paramName":ext.paramValue]
				}
			}
			//读取扩展属性，kind为pc的在后，如果有重复属性，，kind为pc的应覆盖kind为pub的
			it.userFieldExtend?.each{ext->
				if(ext.kind=='pc'){
					attr << ["$ext.paramName":ext.paramValue]
				}
			}
            fields << attr
        }
        def json = [success:true,fields:fields]
        render fields as JSON
    }

	@UserAuthAnnotation
	def all(){
		def user = User.findById(session.employee?.user.id)
		def useUser = user
		if(user.useSysTpl==true){
			useUser = user.edition.templateUser
		}
		def model = [:]
		useUser.modules.each {module->
			def q = UserField.where {
				module == module
				user == useUser
			}
			def fields = []
			q.list().each {
				def attr = [:]
				attr.put('name',it.fieldName)
//			attr.put('allowNull',false)
				switch (it.dbType){
					case 'java.lang.Integer':
					case 'java.lang.Long':
						//attr.put('type','int')
						break
					case 'java.lang.String' :
						attr.put('type','string')
						break
					case 'java.util.Date' :
						attr.put('type','date')
//					attr.put('dateFormat','Y-m-d h:m:s')
						break
					case 'java.lang.Boolean' :
						attr.put('type','boolean')
						break
					case 'java.lang.Double' :
						attr.put('type','float')
						break
				}
				if(it.defValue) {
					attr.put('defaultValue', it.defValue)
				}
				//读取扩展属性，kind为pub的在前
				it.userFieldExtend?.each{ext->
					if(ext.kind=='pub'){
						attr << ["$ext.paramName":ext.paramValue]
					}
				}
				//读取扩展属性，kind为pc的在后，如果有重复属性，，kind为pc的应覆盖kind为pub的
				it.userFieldExtend?.each{ext->
					if(ext.kind=='pc'){
						attr << ["$ext.paramName":ext.paramValue]
					}
				}
				fields << attr
			}
			if(fields) {
				model << ["${module.moduleId}": fields]
			}
		}
		def json = [success:true,models:model]
		render json as JSON
	}
    /**
     * 获取模块版本号的信息
     * @return
     */
    @UserAuthAnnotation
    def moduleVersion(){
        def userModuleVersion = UserModuleVersion.findAllByUser(session.employee.user)
        def data = []
        userModuleVersion.each {
            data << [moduleId:it.module.moduleId,version:it.ver]
        }
        render ([success:true,data:data,empId:session.employee.id] as JSON)
    }
	/**
	 * pc端获取模块的自定义配置信息
	 * @return
     */
	@UserAuthAnnotation
	def pc(){
		def employee = session.employee
		def user = User.findById(session.employee.user.id)
		def useUser = user
		if(user.useSysTpl==true){
			useUser = user.edition.templateUser
		}
		def module = Module.findByModuleId(params.moduleId)
		def fields = moduleVersionService.modelFields(useUser,params.moduleId)
		def views = [:]
		View.findAllByUserAndModule(useUser,module)?.each {v->
			if(v.clientType == "pc" || v.clientType == "all"){
				if(v.viewType == "list"){
					views << ["${v.viewId}":moduleVersionService.listView(employee,v.viewId,params.showRowNumber)]
				}else if(v.viewType == "form"){
					views << ["${v.viewId}":moduleVersionService.formView(employee,v.viewId)]
				}
			}
		}
		render ([success:true,data:[fields:fields,views:views]] as JSON)
	}

	/**
	 * 生成手机端model
	 * 需要登陆状态，并且传递参数 modelName
	 * @return
	 */
    @UserAuthAnnotation
	def phone(){
		def module = Module.findByModuleId(params.moduleId)
		def model = module?.model
		def user = User.findById(session.employee?.user.id)
		def useUser = user
		if(user.useSysTpl==true){
			useUser = user.edition.templateUser
		}
		def q = UserField.where {
            module == module
			user == useUser
		}
		// 	按钮处理  begin
		def opts = UserOperation.where{
			user==useUser
			operation{
				clientType == 'mobile'||clientType == 'all' 
			}
			module==module
		}.list()
		def userFields = [:]
		q.list().each {
			userFields << [(it.fieldName):it]
		}
		def emp = Employee.get(session.employee.id)
		def privileges = emp.privileges
		def operations = []
		opts.each{opt->
			if(privileges.userOperation.id.flatten().contains(opt.id)){
				def btnOpts = [id:opt.operation.operationId,text:opt.text]
				// 操作条件,存放格式：[name:name,remark:remark,userOptConditionDetail:[[xxxx:xxxx],[aaaa:aaaa]]]
				if(opt.userOptCondition){
					def userOptConditions = []
					// 条件
					opt.userOptCondition?.each {
						if(it.userOptConditionDetail){
							def userOptCondition = [name:it.name,remark: it.remark]
							// 条件明细
							def userOptConditionDetail = []
							it.userOptConditionDetail?.each {detail->
								userOptConditionDetail = [fieldName:detail.fieldName,operator:detail.operator,
														  value: detail.value,valueFlag:detail.valueFlag]
								if(userFields.containsKey(detail.fieldName)){
									def userField = userFields.get(detail.fieldName)
									userOptConditionDetail << [dbType: userField.dbType,dict:userField.dict]
									if(userField.dbType == 'com.uniproud.wcb.Employee'){
										if(userField.relation == 'OneToMany' || userField.relation == 'ManyToMany'){
											userOptConditionDetail << [toMany: true]
										}else if(userField.relation){
											userOptConditionDetail << [toMany: false]
										}
									}
								}
							}
							userOptCondition << [userOptConditionDetail:userOptConditionDetail]
							userOptConditions << userOptCondition
						}
					}
					if(userOptConditions) {
						btnOpts << [userOptConditions:userOptConditions,hasCondition:true]
					}
				}
				operations << btnOpts
			}
		}
		// 	按钮处理  end
		
		// 	视图处理 begin
		def views = View.where{
			user == useUser
			module == module
			model == model
			clientType == 'mobile'
		}.list()
		def v = []
		views.each{view->
			def detail = ViewDetail.where {
				user == useUser
				view == view
			}
			def privilegeViewDetail = PrivilegeViewDetail.where{
				privilege in privileges
				viewDetail{
					view == view
				}
			}.list()
			def items = []
			detail.list([sort: 'orderIndex',order: 'ASC']).each {
				def attr = [label:it.userField.text,name:it.userField.fieldName,xtype:'textfield']
				privilegeViewDetail.each{pv->
					if(pv.viewDetail==it){
						if(pv.unShow){
						  attr << [hidden:true]
						}
						if(pv.unEdit){
						  attr << [disabled:true,readOnly:true]
						}
					}
				}
				if(it.userField.bitian){
					attr << [required:true]
				}
                if(it.userField.weiyi){
                    attr << [unique:true]
                }
				if(it.pageType=='combo'){
					attr << [xtype:'selectField']
					if(it.userField.dict){
						def store = []
						store << [value:'',text:'']
						it.userField.dict?.items.each {
							store << [value:it?.itemId,text:it?.text]
						}
						attr << [options:store]
					}
				}else if(it.pageType=='textarea'){
				    attr << [xtype:'textareafield']
				}else if(it.pageType=='textfield2'){
					attr << [xtype:'textfield']
				}else if(it.userField.fieldName=='location'){
					attr << [xtype : 'locationField',location:false]
				}else if(it.pageType=='baseUploadField'){
					attr << [xtype : 'photoPanel',maxNum : 4]
				}else if(it.pageType=='baseImageField'){
					attr << [xtype : 'photoPanel',maxNum : 1]
				}else if(it.userField.dbType=='java.util.Date') {
					attr << [xtype: 'timefield']
					if (it.pageType == 'datefield') {
						attr << [dateFormat: 'Y-m-d']
					} else if (it.pageType == 'datetimefield') {
						attr << [dateFormat: 'Y-m-d H:i:s']
					} else if (it.pageType == 'timefield') {
						attr << [dateFormat: 'H:i:s']
					}
					if (it.userField.dateFormat) {
						attr << [dateFormat: it.userField.dateFormat]
					}
				}else if(it.userField.dbType == 'java.lang.Boolean'){
					attr << [xtype: 'togglefield']
				}else if(it.userField.pageType=='hidden'){
					attr << [xtype : 'hiddenfield']
				}
				if(it.pageType=='numberfield'){
					if(it.userField.max) attr << [maxValue:it.userField.max]
					if(it.userField.min) attr << [minValue:it.userField.min]
				}else{
					if(it.userField.max) attr << [maxLength:it.userField.max]
				}
				it.viewDetailExtend?.each{ext->
					if(ext.paramType == 'number'){
						attr << ["$ext.paramName": Integer.parseInt(ext.paramValue)]
					}else if(ext.paramType == 'Boolean'){
						attr << ["$ext.paramName": Boolean.parseBoolean(ext.paramValue)]
					}else{
						attr << ["$ext.paramName":ext.paramValue]
					}
				}
				if(it.userField.fieldName=='dateCreated'||it.userField.fieldName=='lastUpdated'){
					attr << [submitValue:false,dateFormat:'Y-m-d H:i:s']
					attr << [disabled:true]
				}
				if(it.defValue) {
					attr.put('defValue', it.defValue)
				}
				items << attr
			}
			v << [viewId:view.viewId,isSearchView:view.isSearchView,fieldset:items,title:view?.title]
		}

		def searchView = View.findByModuleAndUserAndIsSearchView(module, useUser, true)
		def dictFields = []
		if(searchView){
			def detail = ViewDetail.where {
				user == useUser
				view == searchView
			}
			detail.list([sort: 'orderIndex',order: 'ASC']).each {
				if (it.pageType == 'combo') {
					def attr = [label:it.userField.text,name:it.userField.fieldName,xtype:'textfield']
					attr << [xtype: 'selectField']
					if (it.userField.dict) {
						def store = []
						store << [value: '', text: '']
						it.userField.dict?.items.each {
							store << [value: it?.itemId, text: it?.text]
						}
						attr << [options: store]
					}
					dictFields << attr
				}
			}
		}

		def fields = []
		q.list().each {
			def attr = [:]
			attr.put('name',it.fieldName)
			attr.put('allowNull',false)
			attr.put('text',it.text)
			switch (it.dbType){
				case 'java.lang.Integer':
				case 'java.lang.Long':
					attr.put('type','int')
					break
				case 'java.lang.String' :
					attr.put('type','string')
					break
				case 'java.util.Date' :
					attr.put('type','date')
					if(it.pageType=='datefield'){
						attr << [format : 'Y-m-d']
					}else if(it.pageType=='datetimefield'){
						attr << [format : 'Y-m-d H:i:s']
					}else if(it.pageType=='timefield'){
						attr << [format : 'H:i:s']
					}
					if(it.dateFormat){
						attr << [format : it.dateFormat]
					}
//					if(it.dateFormat){
//						attr.put('format',it.dateFormat)
//					}
					break
				case 'java.lang.Boolean' :
					attr.put('type','boolean')
					break
				case 'java.lang.Double' :
					attr.put('type','float')
					break
			}
			if(it.defValue) {
				attr.put('defaultValue', it.defValue)
			}
			//读取扩展属性，kind为pub的在前
			it.userFieldExtend?.each{ext->
				if(ext.kind=='pub'){
					attr << ["$ext.paramName":ext.paramValue]
				}
			}
			//读取扩展属性，kind为phone的在后，如果有重复属性，，kind为phone的应覆盖kind为pub的
			it.userFieldExtend?.each{ext->
				if(ext.kind=='phone'){ 
					attr << ["$ext.paramName":ext.paramValue]
				}
			}
			if(it.dict){
				def options = []
				it.dict.items.each{item->
					options << ["text":item?.text,"value":item?.itemId]
				}
				attr.put('options',options)
			} 
			fields << attr
		}
		def data = [fields:fields,operation:operations,view:v,dictFields:dictFields]
		
		def json = [success:true,data:data] as JSON
		render json
	}
}
