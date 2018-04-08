package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@Transactional(readOnly = true)


@AdminAuthAnnotation
class ViewOperationController {
    def list() {
        RequestUtil.pageParamsConvert(params)
        def query = ViewOperation.where{
            if(params.view){
                view{
                    id == params.view?.toLong()
                }
            }
        }
        def viewOperations = []
        query.list(params).each{
            viewOperations << [id:it.id,orderIndex:it.orderIndex,tempOrderIndex:it.orderIndex,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated,
                       view:[id:it.view?.id,title:it.view?.title],
                               userOperation:[id:it.userOperation?.id,text:it.userOperation?.text]
                    ]
        }
        def json = [success:true,data:viewOperations, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        data.each {
            def viewOperation = ViewOperation.get(it.id)
            viewOperation.properties = it
            if(!viewOperation.validate()) {
                render([success:false,errors: errorsToResponse(viewOperation.errors)] as JSON)
                return
            }
            viewOperation.save(flush:true)

            event('upumv',[user: viewOperation.view.user,module: viewOperation.view.module])
        }
        render([success:true] as JSON)
    }

    @Transactional
    def searchUserOperation(){
        def viewOperationIds = []
        if(params.viewId){
            ViewOperation.where {
                view{
                    id == params.viewId?.toLong()
                }
            }.list().each {
                viewOperationIds.push(it.userOperation?.id)
            }
        }
        def query = {
            if(params.userId){
                user{
                    eq("id",params.userId?.toLong())
                }
            }
            if(params.moduleId){
                module{
                    eq("id",params.moduleId?.toLong())
                }
            }
        }

        def operations = []
        UserOperation.createCriteria().list(query).each{
            def checked = false
            if(viewOperationIds.contains(it.id)){
                checked = true
            }
            operations << [
                    boxLabel: it.text, name: 'operation',inputValue: it.id,checked : checked
            ]
        }
        def json = [data:operations] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def enableUserOperation(){
        def view
        if(params.viewId){
            view = View.get(params.viewId?.toLong())
        }
        if(!view){
            render(successFalse)
            return
        }

        // 获取已经启用的操作按钮
        def enableOperationIds = []
        def enableOperations = ViewOperation.where {
            view == view
        }.list().each {
            enableOperationIds.push(it.userOperation?.id)
        }

        // 本次选中的启用按钮
        def paramsEnableOperationIds = params.data?.split(",") as List

        def newEnableOperationIds = []
        // 如果前台传来为空，则删除掉当前视图绑定的所有的操作
        if(!paramsEnableOperationIds || paramsEnableOperationIds.size() <= 0){
            ViewOperation.executeUpdate("delete ViewOperation where view.id = :viewId",[viewId:view.id])
            render(successFalse)
            return
        }
        // 如果以前该视图包含的操作，本次启用不包含，则应该删掉已启用的操作
        enableOperationIds.each {
            if(!paramsEnableOperationIds.contains(it.toString())){
                def viewOperation = ViewOperation.findByViewAndUserOperation(view,UserOperation.get(it.toLong()))
                delUserOperationAndReOrderIndex(viewOperation)
            }
        }
        // 如果本次启用的操作不包含在已启用的操作里面，则说明是新的启用的。
        paramsEnableOperationIds.each {
            if(!enableOperationIds.contains(it.toLong())){
                newEnableOperationIds.push(it.toLong())
            }
        }

        def maxOrderIndex = 0
        // 获取最大排序值
        maxOrderIndex = ViewOperation.executeQuery("select max(orderIndex) from ViewOperation where view.id = :viewId",[viewId: view?.id]).first()
        if(!maxOrderIndex){
            maxOrderIndex = 0
        }
        // 启用用户操作
        def userOperation
        newEnableOperationIds.each{
            maxOrderIndex = maxOrderIndex + 1
            userOperation = UserOperation.get(it.toLong())
            new ViewOperation(view:view,orderIndex: maxOrderIndex,userOperation: userOperation).save()
        }
        event('upumv',[user: view.user,module: view.module])
        render([success:true,msg:'启用成功！'] as JSON)
    }

    @Transactional
    def delUserOperationAndReOrderIndex(ViewOperation viewOperation){
        def orderIndex = viewOperation.orderIndex
        viewOperation.delete()
        // 查询出索引在删除索引之后的记录，然后把他们的索引减一再更新
        def viewOperations = ViewOperation.executeQuery("from ViewOperation where view.id=:viewId and orderIndex>:orderIndex",[viewId:viewOperation.view?.id,orderIndex: orderIndex])
        viewOperations.each {
            it.orderIndex = it.orderIndex - 1
            it.save()
            event('upumv',[user: it.view.user,module: it.view.module])
        }
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        ids.each {
            def viewOperation = ViewOperation.get(it)
            delUserOperationAndReOrderIndex(viewOperation)
            event('upumv',[user: viewOperation.view.user,module: viewOperation.view.module])
        }
        chain(controller:'base',action: 'success',params:params)
    }

}
