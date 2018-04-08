package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import groovy.json.JsonSlurper

import java.lang.reflect.Array

import static com.uniproud.wcb.ErrorUtil.errorsToResponse
import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue
@Transactional(readOnly = true)
class ViewController {

    def viewService
    def moduleVersionService

    @AdminAuthAnnotation
    @UserAuthAnnotation
    def list() {
        def userId = params.user
        if(params.specialParam){
            userId = params.specialParam
        }else if(session.employee && params.useCurrentEmp && params.useCurrentEmp.toBoolean() == true){
            userId = session.employee.user?.id
        }

        RequestUtil.pageParamsConvert(params)
        def query = View.where{
            if(params.model){
                model{
                    id == params.model?.toLong()
                }
            }
			if(params.module){
				module{
					id  == params.module?.toLong()
				}
			}
			if(userId){
				user{
					id  == userId.toLong()
				}
			}
            if(params.view){
                view{
                    id  == params.view.toLong()
                }
            }
            if(params.clientType){
                clientType == params.clientType
            }
            if(params.viewType){
                viewType == params.viewType
            }
            if(params.viewPage && params.viewPage.toBoolean() == true){
                ilike("viewId","%View")
            }
            if(params.searchValue){ // 快速搜索
                or {
                    ilike("title","%$params.searchValue%")
                    module{
                        ilike("moduleName","%$params.searchValue%")
                    }
                }
            }
        }
        def views = []
        query.list(params).each{
            views << [
                       id:it.id,title:it.title,viewId:it.viewId,clientType:it.clientType,viewType:it.viewType,
                       user:[id:it.user?.id,name:it.user.name],
                       model:[id:it.model?.id,modelName:it.model?.modelName],
                       module:[id:it.module?.id,moduleName:it.module.moduleName],
                       remark:it.remark,dateCreated:it.dateCreated,columns:it.columns,isSearchView:it.isSearchView,
                       isImportOrExportView:it.isImportOrExportView,
                       lastUpdated:it.lastUpdated,forceFit: it.forceFit,editable:it.editable
                     ]
        }
        def json = [success:true,data:views, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @AdminAuthAnnotation
    @Transactional
    def insert(View viewInstance) {
        if (viewInstance == null) {
            render([success:false] as JSON)
            return
        }
        if (!viewInstance.validate()) {
            render([success:false,errors: errorsToResponse(viewInstance.errors)] as JSON)
            return
        }

        // 如果设定查询视图，则先把原有的置空
        if(viewInstance.isSearchView == true){
            def searchView = View.findByIsSearchViewAndModuleAndUser(true,viewInstance.module,viewInstance.user)
            if(searchView){
                searchView.isSearchView = null
                searchView.save()
            }
        }
        // 如果设定导入视图，则先把原有的置空
        if(viewInstance.isImportOrExportView == true){
            def importOrExportView = View.findByIsImportOrExportViewAndModuleAndUser(true,viewInstance.module,viewInstance.user)
            if(importOrExportView){
                importOrExportView.isImportOrExportView = null
                importOrExportView.save()
            }
        }

        viewInstance.save flush: true
        //如果配置文件中已有该视图，则初始化该视图
        def file = new File("${grailsApplication.config.MODULE_FILE_PATH}/${viewInstance.module.moduleId}.json")
        if(file.exists()){
            def json = new JsonSlurper().parseText(file.text)
            def v = json?.V
            if (v && v."${viewInstance.viewId}") {
                def vw = v."${viewInstance.viewId}"
                viewService.initViewDetaiNew(viewInstance.user,viewInstance.module,viewInstance,vw?.viewDetail,json?.F)
            }
        }
        event('upumv',[user: viewInstance.user,module: viewInstance.module])
        render([success:true] as JSON)
    }

    @AdminAuthAnnotation
    @Transactional
    def update(View viewInstance) {
        if (viewInstance == null) {
            render(successFalse)
            return
        }
        if (!viewInstance.validate()) {
            render([success:false,errors: errorsToResponse(viewInstance.errors)] as JSON)
            return
        }

        // 如果设定查询视图，则先把原有的置空
        if(viewInstance.isSearchView == true){
            def searchView = View.findByIsSearchViewAndModuleAndUserAndIdNotEqual(true,viewInstance.module,viewInstance.user,viewInstance.id)
            if(searchView){
                searchView.isSearchView = null
                searchView.save()
            }
        }
        // 如果设定导入视图，则先把原有的置空
        if(viewInstance.isImportOrExportView == true){
            def importOrExportView = View.findByIsImportOrExportViewAndModuleAndUserAndIdNotEqual(true,viewInstance.module,viewInstance.user,viewInstance.id)
            if(importOrExportView){
                importOrExportView.isImportOrExportView = null
                importOrExportView.save()
            }
        }
        viewInstance.save flush: true
        event('upumv',[user: viewInstance.user,module: viewInstance.module])
        render(successTrue)
    }

    @UserAuthAnnotation
    @AdminAuthAnnotation
    def index(){
        def json = moduleVersionService.listView(session.employee,params.viewId,params.showRowNumber) as JSON
        render json
    }

    /**
     * admin层 视图明细预览
     * @return
     */
    @AdminAuthAnnotation
    def previewList(){
        def user = User.get(params.userId?.toLong())
        def view = View.findByViewIdAndUser(params.viewId,user)
        def q = ViewDetail.where {
            user == user
            view == view
        }
        def columns = []
        // 树状列表不需要这个rownumber，只需要在树状里面配置showRowNumber:false
        if(params.showRowNumber != 'false'){
            columns << [xtype: 'rownumberer',width: 40,sortable: false]
        }
        q.list([sort: 'orderIndex',order: 'ASC']).each {vd->
            def attr = []
            def editor = [:]
            attr = [text:vd.label,dataIndex:vd.userField.fieldName,orderIndex:vd.orderIndex]
            editor << [xtype:vd.pageType]
            if(vd.userField.bitian){
                editor << [allowBlank:false]
            }
            if(vd.defValue){
                editor << [value:vd.defValue]
            }
            if(vd.pageType=='combo'||vd.pageType=='radio'){
                attr << [xtype:'rowselecter']
                def items = []
                items << ['','']
                vd.userField.dict.items.each {
                    items << [it.itemId,it.text]
                }
                attr << [arry:items]
                def data = []
                data << [id:null,text:'']
                vd.userField.dict.items.each {
                    data << [id:it.itemId,text:it.text]
                }
                editor << [xtype:'baseCombo',data:data]
            }else if(vd.pageType=='baseUploadField'){
                attr << [unShow:true]
            }else if(vd.pageType=='baseImageField'){
                attr << [unShow:true]
            }else if(vd.pageType=='multichoice'){
                attr << [xtype:'multiselecter']
                def items = []
                vd.userField.dict.items.each {
                    items << ["${it.dict.dataId}-${it.itemId}",it.text]
                }
                attr << [arry:items]
            }else if(vd.pageType=='hidden'){
                attr << [unShow:true]
            }

            if(vd.pageType=='numberfield'){
                if(vd.userField.max) editor << [maxValue:it.userField.max]
                if(vd.userField.min) editor << [minValue:it.userField.min]
            }else{
                if(vd.userField.max) editor << [maxLength:it.userField.max]
                if(vd.userField.min) editor << [minLength:it.userField.min]
            }

            if(vd.userField.dbType=='java.util.Date'){
                attr << [xtype:'datecolumn']
                if(vd.pageType=='datefield'){
                    attr << [format : 'Y-m-d']
                    editor <<  [format : 'Y-m-d']
                }else if(vd.pageType=='datetimefield'){
                    attr << [format : 'Y-m-d H:i:s']
                    editor << [format : 'Y-m-d H:i:s']
                }else if(vd.pageType=='timefield'){
                    attr << [format : 'H:i:s']
                    editor << [format : 'H:i:s']
                }
                if(vd.userField.dateFormat){
                    attr << [format : vd.userField.dateFormat]
                }
                editor << [xtype:vd.pageType]
            }
            vd.viewDetailExtend?.each{ext->
                if(ext.paramValue=='treecolumn'){
                    attr << ["$ext.paramName":ext.paramValue]
                }else{
                    editor << ["$ext.paramName":ext.paramValue]
                }
            }
            attr<<[editor:editor]
            if(!attr?.unShow){
                columns << attr
            }
        }
        //页面中操作按钮处理
        def tbar = []
        def vo = ViewOperation.where{
            view == view
        }.list([sort:'orderIndex',order:'ASC'])
        vo.each {opt->
            def btn = [xtype:'button',text:opt.userOperation.text,operationId:opt.userOperation.operation.operationId]
            if(opt.userOperation.operation.auto
                    &&(opt.userOperation.operation?.type=='add'
                    ||opt.userOperation.operation?.type=='update'
                    ||opt.userOperation.operation?.type=='view'
                    ||opt.userOperation.operation?.type=='del')
            ){
                btn << [auto:opt.userOperation.operation.auto]
            }
            btn << [optType:opt.userOperation.operation.type]
            if(opt.userOperation.operation.showWin){
                btn << [showWin:opt.userOperation.operation.showWin]
            }
            if(opt.userOperation.operation.vw){
                btn << [vw:opt.userOperation.operation.vw]
            }
            if(opt.userOperation.iconCls||opt.userOperation.operation.iconCls){
                btn << [iconCls:opt.userOperation.iconCls?opt.userOperation.iconCls:opt.userOperation.operation.iconCls]
            }else{
                switch(opt.userOperation.operation?.type){
                    case 'add' : btn << [iconCls:'table_add']
                        break
                    case 'update' : btn << [iconCls:'table_save']
                        break
                    case 'view' : btn << [iconCls:'table_view']
                        break
                    case 'del' : btn << [iconCls:'table_remove']
                        break
                }
            }
            if(opt.userOperation?.operation?.autodisabled){
                btn << [disabled:true,autodisabled:true]
            }
            tbar << btn
        }
        def json = [columns:columns,tbar:tbar,title:view?.title] as JSON
        render json
    }

    @UserAuthAnnotation
    def getSearchViewId(){
        def user = session.employee?.user
        def module = params.id ? Module.get(params.id as Long) : Module.findByModuleId(params.moduleId)
        def searchView
        if(module?.moduleId == 'plan_summary'){
            def object = PlanSummary.get(params.objectId as Long)
            if(object.type == 1){
                searchView = View.findByUserAndModuleAndViewId(user,module,'PlanSummaryDayView')
            }else if(object.type == 2){
                searchView = View.findByUserAndModuleAndViewId(user,module,'PlanSummaryWeekView')
            }else if(object.type == 3){
                searchView = View.findByUserAndModuleAndViewId(user,module,'PlanSummaryMonthView')
            }
        }
        if(!searchView){
            searchView = View.findByModuleAndUserAndIsSearchView(module, user, true)
        }
        render ([searchView:searchView?.viewId,modelName: searchView?.model?.modelName] as JSON)
    }

    @UserAuthAnnotation
    def form(){
        def json = moduleVersionService.formView(session.employee,params.viewId)
        render json as JSON
    }

    /**
     * admin层视图明细预览
     * @return
     */
    @AdminAuthAnnotation
    def previewView(){
        def user = User.get(params.userId?.toLong())
        def view = View.findByViewIdAndUser(params.viewId,user)
        def q = ViewDetail.where {
            user == user
            view == view
        }
        def items = []
        q.list([sort: 'orderIndex',order: 'ASC']).each {
            def attr = [fieldLabel:it.userField.text,name:it.userField.fieldName,xtype:it.pageType,note:it.userField.note]
            if(it.userField.bitian){
                attr << [allowBlank:false]
                attr << [beforeLabelTextTpl:'<span style="color:red;font-weight:bold" data-qtip="必填">*</span>']
            }
            if(it.defValue){
                attr << [value:it.defValue]
            }

            if(it.pageType=='combo'){
                attr << [xtype : 'combo',autoSelect:true,forceSelection:true,typeAhead : true,emptyText:'-- 请选择 --']
                if(it.userField.dict){
                    def store = []
                    it.userField.dict.items.each {
                        store << [it.itemId,it.text]
                    }
                    attr << [store:store]
                }
            }else if(it.pageType=='grid'){
                if(it.listView?.viewId){
                    attr <<
                            [
                                    colspan:2,xtype:'baseEditList',viewId:it.listView.viewId,enableToolbar:false,title:it.label,
                                    overflowY:'auto', autoScroll:true,frame: true,store:"${it.listView.model.modelName}Store"
                                    /*maxHeight:200,*/,enableBasePaging:false
                            ]
                }else{
                    attr << [del:true]
                }
            }else if(it.pageType=='textfield2'){
                if(view.columns>1){
                    attr << [colspan:2,width:500]
                }
                attr << [xtype : 'textfield']
            }else if(it.pageType=='textarea'){
                if(view.columns>1){
                    attr << [colspan:2,width:500,grow:true]
                }
                attr << [grow:true]
            }else if(it.pageType=='baseUploadField'){
                if(view.columns>1){
                    attr << [colspan:2,width:500]
                }
                attr << [hiddenName:it.userField.fieldName]
            }else if(it.pageType=='baseImageField'){
                attr << [rowspan:3,hiddenName:it.userField.fieldName/*,xtype:'baseImageField2'*/]
            }else if(it.pageType=='radio'){
                attr << [xtype : 'radiogroup',columns:1]
                if(it.userField.dict){
                    def store = []
                    def userField = it.userField
                    it.userField.dict.items.each {
                        store << [inputValue:it.itemId,boxLabel:it.text,name:userField.fieldName,note:userField.note]
                    }
                    if(it.userField.dict.items){
                        attr << [rowspan : it.userField.dict.items.size()]
                    }
                    attr << [items:store]
                }
            }else if(it.pageType=='multichoice'){
                attr << [xtype : 'checkboxgroup',columns:1]
                if(it.userField.dict){
                    def store = []
                    def userField = it.userField
                    it.userField.dict.items.each {
                        store << [inputValue:"${it.dict.dataId}-${it.itemId}",boxLabel:it.text,name:userField.fieldName]
                    }
                    if(it.userField.dict.items){
                        attr << [rowspan : it.userField.dict.items.size()]
                    }
                    attr << [items:store]
                }
            }else if(it.pageType=='htmleditor'){
                if(view.columns>1){
                    attr << [colspan:2,width:500]
                }
            }else if(it.pageType=='email'){
                attr << [xtype : 'textfield', vtype:'email']
            }else if(it.pageType=='url'){
                attr << [xtype : 'textfield', vtype:'url']
            }else if(it.userField.dbType=='java.util.Date'){
                if(it.pageType=='datefield'){
                    attr << [format : 'Y-m-d']
                }else if(it.pageType=='datetimefield'){
                    attr << [format : 'Y-m-d H:i:s']
                }else if(it.pageType=='timefield'){
                    attr << [format : 'H:i:s']
                }
                if(it.userField.dateFormat){
                    attr << [format : it.userField.dateFormat]
                }
                attr << [submitFormat:'Y-m-d H:i:s']
            }else if(it.pageType=='hidden'){
                attr << [xtype : 'hidden']
            }
            if(it.pageType=='numberfield'){
                if(it.userField.max) attr << [maxValue:it.userField.max]
                if(it.userField.min) attr << [minValue:it.userField.min]
            }else{
                if(it.userField.max) attr << [maxLength:it.userField.max]
                if(it.userField.min) attr << [minLength:it.userField.min]
            }
            if(view.clientType != 'mobile'){
                it.viewDetailExtend?.each{ext->
                    if(ext.paramType == 'Integer'){
                        attr << ["$ext.paramName": Integer.parseInt(ext.paramValue)]
                    }else if(ext.paramType == 'Boolean'){
                        attr << ["$ext.paramName": Boolean.parseBoolean(ext.paramValue)]
                    }else{
                        attr << ["$ext.paramName":ext.paramValue]
                    }
                }
            }
            if(it.userField.fieldName=='dateCreated'||it.userField.fieldName=='lastUpdated'){
                attr << [disabled:true]
            }
            if(!attr.del)  items << attr
        }
        def json = [items:items,title:view?.title,paramColumns:view?.columns]
        render json as JSON
    }
    /**
     * 列表页面中的查询功能中下拉查询项数据封装
     * @return
     */
    @UserAuthAnnotation
    @AdminAuthAnnotation
    def searchField(){
        def user = User.get(session.employee?.user?.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def module = View.findByViewIdAndUser(params.viewId,useUser).module
        def view = View.createCriteria().list{
            eq('isSearchView',true)
            eq('module',module)
            eq('user',useUser)
        }[0]
        def q = ViewDetail.where {
            view == view
            ne("pageType","splitter")
        }
        def items = []
        q.list([sort: 'orderIndex',order: 'ASC']).each {
            if(!it.userField.fieldName.equalsIgnoreCase("id")&&it.userField.pageType!='hidden'&&
               !it.userField.relation){
                def attr = [fieldLabel:it.userField.text,name:it.userField.fieldName,pageType:it.pageType,dbType:it.userField.dbType]
                if(it.userField.pageType=='combo' || it.userField.pageType=='checkbox'){
                    if(it.userField.dict){
                        def store = []
                        it.userField.dict?.items?.each {
                            store << [it?.itemId,it?.text]
                        }
                        attr << [store:store]
                    }
                }
                items << attr
            }
        }
        render items as JSON
    }
	
	/**
	 * 统计分析页面的统计类型
	 * @return
	 */
    @UserAuthAnnotation
	def statisticGroupField(){
        def user = User.get(session.employee?.user?.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
		def module = View.findByViewIdAndUser(params.viewId,useUser).module
		def view = View.createCriteria().list{
			eq('isSearchView',true)
			eq('module',module)
			eq('user',useUser)
		}[0]
		def q = ViewDetail.where {
            view == view
        }
		def items = []
		q.list([sort: 'orderIndex',order: 'ASC']).each {
			if(!it.userField.fieldName.equalsIgnoreCase("id")){
				if(it.userField.pageType=='combo' || it.userField.pageType=='checkbox'){
					items <<  [fieldLabel:it.label,name:it.userField.fieldName,pageType:it.pageType,dbType:it.userField.dbType]
				}
			}
		}
		render items as JSON
	}

}
