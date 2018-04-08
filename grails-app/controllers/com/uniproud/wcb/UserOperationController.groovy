package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import javax.validation.constraints.Null

import static com.uniproud.wcb.ErrorUtil.*

@Transactional(readOnly = true)
@AdminAuthAnnotation
@UserAuthAnnotation
class UserOperationController {
    @AdminAuthAnnotation
    def list(){
        //RequestUtil.pageParamsConvert(params)
        def userOperationClosure  =  {
            if(params.moduleId) {
                eq('module.id', params.moduleId?.toLong())
            }
            if(params.userId){
                eq('user.id',params.userId?.toLong())
            }
        }

        // 查询用户操作有哪些
        def userOperations = [:]
        UserOperation.createCriteria().list(userOperationClosure).each {
            userOperations.put(it.operation?.id,it)
        }
        // 查询符合条件的操作有哪些,如果用户操作里面已经有的，则用用户的。否则用默认的
        def operationClosure  =  {
            if(params.moduleId) {
                eq('module.id', params.moduleId?.toLong())
            }
        }
        def operations = []
        def userOperateId
        def module
        def isEnable = false
        def result = Operation.createCriteria().list(operationClosure).each {
            isEnable = false
            userOperateId = null
            module = null
            def text = it.text
            def iconCls = it.iconCls
            if(userOperations.containsKey(it.id)){
                def userOperation = userOperations.get(it.id)
                text = userOperation.text
                iconCls = userOperation.iconCls
                userOperateId = userOperation.id
                module = userOperation.module.id
                isEnable = true
            }

            operations << [id:it.id,text:text,iconCls:iconCls,userOperationId:userOperateId,module:module,isEnable: isEnable]
        }
        def json = [success:true,data:operations] as JSON
        log.info json
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def save() {
        def data = JSON.parse(params.data)
        def otherParams = JSON.parse(params.otherParams)
        def operationIds = []
        def user = User.get(otherParams.userId?.toLong())
        def module = Module.get(otherParams.moduleId?.toLong())
        UserOperation.findAllByUserAndModule(user,module).each {
            operationIds.push(it.operation.id)
        }

        def privilegeList = Privilege.findAllByUser(user)

        def uo
        def operation
        data.each{
            if(!it.isEnable){
                if(!operationIds.contains(it.id.toLong())) {
                    return    // 没有选中，并且没有包含在用户操作里面
                }else{
                    if(it.userOperationId){
                        def userOperation = UserOperation.get(it.userOperationId?.toLong())
                        ViewOperation.executeUpdate("delete ViewOperation where userOperation.id = :userOperationId",[userOperationId: it.userOperationId?.toLong()])
                        if(privilegeList){
                            privilegeList.each {
                                if(it.userOperation?.contains(userOperation)){
                                    it.userOperation.remove(userOperation)
                                }
                            }
                        }
                        userOperation.delete()
                    }
                    return
                }
            }
            if(it.userOperationId){
                uo = UserOperation.get(it.userOperationId?.toLong())
            }else{
                uo = new UserOperation()
            }
            operation = Operation.get(it.id?.toLong())
            uo.properties['operation'] = operation
            uo.properties['text'] = it.text
            uo.properties['iconCls'] = it.iconCls
            uo.properties['module'] = otherParams.moduleId?.toLong()
            uo.properties['user'] = otherParams.userId?.toLong()
            uo.save flush: true
        }
        //更新模块版本号
        event('upumv',[user: user,module: module])
        render(successTrue)
    }
}
