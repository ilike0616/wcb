package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import groovy.json.JsonSlurper

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue
import static com.uniproud.wcb.ErrorUtil.successFalse

@AdminAuthAnnotation
@Transactional(readOnly = true)
class ViewDetailController {
    ViewService viewService
    def baseService

	def list() {
		RequestUtil.pageParamsConvert(params)
		def views = []
        def  query = ViewDetail.createCriteria().list(){
            if(params.view){
                eq("view.id",params.view.toLong())
            }
            order("orderIndex", "asc")
        }
        query.each{
            def win
            def dbTypeName
			if(it.userField?.relation?.contains('ToMany')){
				win = 'viewGridField'
				dbTypeName = it.userField.dbType
			}else if(it.userField?.relation?.contains('ToOne')){
                win = 'viewHiddenField'
                dbTypeName = it.userField.dbType
            }else if(it.pageType == 'splitter'){
                win = 'viewSplitterField'
                dbTypeName = "分隔符"
            }
            if(!win){
                switch (it.userField?.dbType){
                    case 'java.lang.Integer':
                    case 'java.lang.Long':
                        if(it.view.viewType == 'form'){
                            win = 'viewIntFormField'
                        }else{
                            win = 'viewIntListField'
                        }
                        dbTypeName = '数字'
                        break
                    case 'java.lang.String' :
                        if(it.view.viewType == 'form'){
                            win = 'viewStringFormField'
                        }else{
                            win = 'viewStringListField'
                        }
                        dbTypeName = '字符'
                        break
                    case 'java.util.Date' :
                        if(it.view.viewType == 'form'){
                            win = 'viewDateFormField'
                        }else{
                            win = 'viewDateListField'
                        }
                        dbTypeName = '日期'
                        break
                    case 'java.lang.Boolean' :
                        if(it.view.viewType == 'form'){
                            win = 'viewBooleanFormField'
                        }else{
                            win = 'viewBooleanListField'
                        }
                        dbTypeName = '真假'
                        break
                    case 'java.lang.Double' :
                        if(it.view.viewType == 'form'){
                            win = 'viewFloatFormField'
                        }else{
                            win = 'viewFloatListField'
                        }
                        dbTypeName = '浮点'
                        break
                    case 'com.uniproud.wcb.Doc' :
                        if(it.view.viewType == 'form'){
                            win = 'viewFileFormField'
                        }else{
                            win = 'viewFileListField'
                        }
                        dbTypeName = '文件'
                        break
                }
            }

			views << [
					id:it.id,orderIndex:it.orderIndex,label:it.label,remark:it.remark,dbTypeName:dbTypeName,win:win,
                    width:it.width,locked:it.locked,sortable:it.sortable,sortName:it.sortName,
					pageType:it.pageType,defValue:it.defValue,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,
                    cols:it.cols,rows:it.rows,readOnly:it.readOnly,disabled:it.disabled,inputFormat:it.inputFormat,
                    paramStore:it.paramStore,paramViewId:it.paramViewId,initName:it.initName,targetIdName:it.targetIdName,
                    isHyperLink:it.isHyperLink,inputType: it.inputType,isCurrentDate:it.isCurrentDate,extraCondition:it.extraCondition,
                    isSubmitValue:it.isSubmitValue,defExpanded:it.defExpanded,isCollapsible:it.isCollapsible,
					user:[id:it.user?.id,name:it.user?.name],
					view:[id:it.view?.id,viewId:it.view?.viewId,title:it.view?.title,editable:it.view?.editable],
					userField:[fieldName:it.userField?.fieldName,text:it.userField?.text,bitian:it.userField?.bitian,
                                max:it.userField?.max,min:it.userField?.min,id:it.userField?.id,maxSize:it.userField?.maxSize,
                                dict:it.userField?.dict?.id,scale:it.userField?.scale,dateFormat:it.userField?.dateFormat,
                                trueText:it.userField?.trueText,falseText:it.userField?.falseText,isMoney: it.userField?.isMoney],
					listView:[id:it.listView?.id,title:it.listView?.title]
				]
		}
		def json = [success:true,data:views, total: query.size()] as JSON
        println(json)
		if(params.callback) {
			render "${params.callback}($json)"
		}else{
			render(json)
		}
	}

    @Transactional
    def insert(ViewDetail viewDetail) {
        if (viewDetail == null) {
            render(successFalse)
            return
        }
        // 获取最大排序值
        def maxOrderIndex = ViewDetail.executeQuery("select max(orderIndex) from ViewDetail where view.id = :viewId",[viewId: viewDetail.view.id]).first()
        if(maxOrderIndex == null){
            maxOrderIndex = 0
        }
        viewDetail.orderIndex = maxOrderIndex = maxOrderIndex + 1
        if (!viewDetail.validate()) {
            render([success:false,errors: errorsToResponse(viewDetail.errors)] as JSON)
            return
        }
        viewDetail.save flush: true
        render(successTrue)
    }
	
	@Transactional
	def save() {
		def data = JSON.parse(params.data)
		def viewInstance
		data.each {
			viewInstance = ViewDetail.get(it.id)
			viewInstance.properties = it
			if(!viewInstance.validate()) {
				render([success:false,errors: errorsToResponse(viewInstance.errors)] as JSON)
				return
			}
		}
		viewInstance.save(flush:true)
//        println viewInstance.view.module
        event('upumv',[user: viewInstance.user,module: viewInstance.view.module])
		render([success:true] as JSON )
	}

    /**
     * 查找固定字段
     * @return
     */
    @Transactional
    def searchEnableField() {
        // 1、首先查询出，视图字段表里面已经拥有哪些字段的名称
        def viewDetailQuery = ViewDetail.where{
            if(params.view){
                view{
                    id == params.view
                }
            }
        }
        def viewDetailIds = []
        viewDetailQuery.list().each{
            viewDetailIds.push(it.userField.id)
        }
        // 2、找出UserField里面排除过ViewDetail之后的字段
        def view = View.get(params.view.toLong())
        def query = {
            isNotNull("text")
            if(view){
                user{
                    eq("id",view.user?.id)
                }
                module{
                    eq("id",view.module?.id)
                }
            }
        }
        def fields = []
        UserField.createCriteria().list(query).each{
            def text = it.text
            if(text.length() > 10) text = text.substring(0,10)+"..."
            if(!viewDetailIds.contains(it.id)){
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
        def view
        def maxOrderIndex = 0
        if(params.viewId){
            view = View.get(params.viewId.toLong())
            // 获取最大排序值
            maxOrderIndex = ViewDetail.executeQuery("select max(orderIndex) from ViewDetail where view.id = :viewId",[viewId: view.id]).first()
            if(maxOrderIndex == null){
                maxOrderIndex = 0
            }
        }
        if(!params.data || !view){
            render(successFalse)
            return
        }
        def userField
        def enableFieldIds = params.data.split(",")
        enableFieldIds.each{
            maxOrderIndex = maxOrderIndex + 1
            userField = UserField.get(it.toLong())
            def vd = new ViewDetail(label: userField.text,orderIndex: maxOrderIndex,userField: userField,view: view,remark: userField.remark
                                    ,pageType: userField.pageType,inputType: userField.inputType,defValue: userField.defValue,user: userField.user)
            vd.save()
            //所属模块的自定义配置文件
            def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${view?.module?.moduleId}.json")
            if(file.exists()){
                def json = new JsonSlurper().parseText(file.text)
                def ved
                if(json?.V?."${view.viewId}"?.viewDetail?."${userField.fieldName}"){
                    ved = json?.V?."${view.viewId}"?.viewDetail?."${userField.fieldName}"?.ved
                }else{
                    ved = json?.F?."${userField.fieldName}"?.VE?."${view?.clientType}"?."${view?.viewType}"
                }
                if(ved){
                    viewService.initViewDetailExtendNew(vd,ved)
                }
            }

//            if(VE?.containsKey(userField.fieldName)){
//                viewService.initViewDetailExtend(vd,VE,userField.fieldName)
//            }
            /*def domain = grailsApplication.getClassForName(userField.model?.modelClass)
            viewService.initViewDetailExtend(vd,domain?.VE,userField.fieldName,)*/
        }
        event('upumv',[user: view.user,module: view.module])
        render([success:true,msg:'启用成功！'] as JSON)
    }

    @Transactional
    def update(ViewDetail viewDetail) {
        event('upumv',[user: viewDetail.user,module: viewDetail.view.module])
        render baseService.save(params, viewDetail)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        def first = ViewDetail.get(ids[0] as Long)
        // 先删除扩展属性
        ViewDetailExtend.executeUpdate("delete ViewDetailExtend where viewDetail.id in (:ids)",[ids:ids*.toLong()])
        // 再删除当前所选属性
        ViewDetail.executeUpdate("delete ViewDetail where id in (:ids)",[ids:ids*.toLong()])
        event('upumv',[user: first.user,module: first.view.module])
        chain(controller:'base',action: 'success',params:params)
    }

    @Transactional
    def insertProperty(ViewDetailExtend viewDetailExtend) {
        if(!params.viewDetail){
            render(successFalse)
            return
        }
        ViewDetail vd = ViewDetail.get(params.viewDetail.toLong())
        def tempViewDetailExtend = ViewDetailExtend.findByParamNameAndViewDetailAndIsBelongToEditor(params.paramName,vd,params.isBelongToEditor.toBoolean())
        if(tempViewDetailExtend != null){
            render ([success:false,msg:"该数据已存在，请重新输入!"] as JSON)
            return
        }
        viewDetailExtend.save(flush : true)
        //更新模块版本号
        event('upumv',[user: vd.user,module: vd.view.module])
        render([success:true,msg:"操作成功！"] as JSON)
    }

    @Transactional
    def updateProperty(ViewDetailExtend viewDetailExtend) {
        if(!viewDetailExtend){
            render successFalse
            return
        }
        viewDetailExtend.save()
        ViewDetail vd = ViewDetail.get(viewDetailExtend.viewDetail.id)
        //更新模块版本号
        event('upumv',[user: vd.user,module: vd.view.module])
        render([success:true,msg:"操作成功！"] as JSON)
    }

    @Transactional
    def delProperty() {
        def ids = JSON.parse(params.ids) as List
        if(ids){
            def viewDetailExtend = ViewDetailExtend.get(ids[0])
            ViewDetail vd = ViewDetail.get(viewDetailExtend.viewDetail.id)

            ViewDetailExtend.executeUpdate("delete ViewDetailExtend where id in (:ids)",[ids:ids*.toLong()])

            //更新模块版本号
            event('upumv',[user: vd.user,module: vd.view.module])
        }
        render([success:true,msg:"操作成功！"] as JSON)
    }

    /**
     * 字段关联属性
     * @return
     */
    @Transactional
    def searchViewDetailExtend(){
        def query = ViewDetailExtend.where{
            viewDetail{
                id  == params.viewDetail
            }
        }
        def fields = []
        query.list().each{
            fields << [
                    id:it.id,paramName:it.paramName,paramValue : it.paramValue,paramType : it.paramType
                    ,isBelongToEditor:it.isBelongToEditor,viewDetail:it.viewDetail?.id
            ]
        }
        def json = [success: true,data:fields] as JSON
        println(json)
        render json
    }

    @AdminAuthAnnotation
    def viewDetailCondField(){
        if(!params.user || !params.module){
            render successFalse
            return
        }
        def q = UserField.where {
            module.id == params.module.toLong()
            user.id == params.user.toLong()
        }
        def items = []
        q.list().each {
            if(!it.fieldName.equalsIgnoreCase("id") && it.pageType != 'hidden' && !it.relation){
                def attr = [fieldLabel:it.text,name:it.fieldName,pageType:it.pageType,dbType:it.dbType]
                if(it.pageType=='combo' || it.pageType=='checkbox'){
                    if(it.dict){
                        def store = []
                        it.dict.items.each {
                            store << [it.itemId,it.text]
                        }
                        attr << [store:store]
                    }
                }
                items << attr
            }
        }
        render items as JSON
    }
}
