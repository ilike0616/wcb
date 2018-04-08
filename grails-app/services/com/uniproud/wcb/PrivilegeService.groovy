package com.uniproud.wcb

import grails.transaction.Transactional

@Transactional
class PrivilegeService {
    def getEmployeePrivilegeOperations(employee,type){
        def userOperations = [];
        def value
        employee?.privileges.each{privilege->
            privilege.userOperation.each{
                if(type){
                    value = it.module?.moduleId + "_" + type
                    if(!userOperations.contains(value) && value == it.operation?.operationId){
                        userOperations.add(value)
                    }
                }else{
                    if(!userOperations.contains(it.operationId)){
                        userOperations.add(it.operationId)
                    }
                }
            }
        }
        return userOperations
    }

    def getUserOperationId(user,operationId){
        user = User.findById(user.id)
        def useUser = user
        if(user.useSysTpl==true){
            useUser = user.edition.templateUser
        }
        def uo = UserOperation.where {
            user == useUser
            operation.operationId == operationId
        }.list()[0]
        uo
    }

    def copyPrivilege(privilege,targetPrivilege){
        // 1、拷贝用户操作
        def targetUserOperationPri = targetPrivilege.userOperation
        if(targetUserOperationPri){
            targetUserOperationPri.each{
                privilege.addToUserOperation(it)
            }
        }
        // 2、拷贝字段
        def targetPrivilegeViewDetail = PrivilegeViewDetail.findAllByPrivilegeAndDeleteFlag(targetPrivilege,false)
        if(targetPrivilegeViewDetail){
            targetPrivilegeViewDetail.each {
                def privilegeViewDetail = new PrivilegeViewDetail(it.properties)
                privilegeViewDetail.privilege = privilege
                privilegeViewDetail.save(flush: true)
            }
        }
        // 3、拷贝portal
        def targetPrivilegeUserPortal = PrivilegeUserPortal.findAllByPrivilege(targetPrivilege)
        if(targetPrivilegeUserPortal){
            targetPrivilegeUserPortal.each {
                def privilegeUserPortal = new PrivilegeUserPortal(it.properties)
                privilegeUserPortal.privilege = privilege
                privilegeUserPortal.save(flush: true)
            }
        }
    }
}
