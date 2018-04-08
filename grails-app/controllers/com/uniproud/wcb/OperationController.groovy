package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@AdminAuthAnnotation
@Transactional(readOnly = true)
class OperationController {
    def list(){
        RequestUtil.pageParamsConvert(params)
        def searchClosure  =  {
            if(params.moduleId) {
                module {
                    eq('id', params.moduleId.toLong())
                }
            }
			if(params.clientType=='pc'){
				or{
					eq('clientType','pc')
					eq('clientType','all')
				}
			}else if(params.clientType=='mobile'){
				or{
					eq('clientType','mobile')
					eq('clientType','all')
				}
			}
        }

        def result = Operation.createCriteria().list (params, searchClosure)
        def operations = []
        result.each {
            operations << [id:it.id,operationId:it.operationId,text:it.text,iconCls:it.iconCls,type:it.type,showWin:it.showWin,
						   modal:it.modal,clientType:it.clientType,defSelected:it.defSelected,autodisabled:it.autodisabled,auto:it.auto,isCustom:it.isCustom,
                           vw:it.vw,module:it.module?.id,moduleName:it.module?.moduleName,targetEl:it.targetEl,optRecords:it.optRecords]
        }
        def json = [success:true,data:operations,total:result.totalCount] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(Operation operation) {
        if (operation == null) {
            render(successFalse)
            return
        }
        if (!operation.validate()) {
            render([success:false,errors: errorsToResponse(operation.errors)] as JSON)
            return
        }
        operation.save flush: true
        event('upallumv',[module: operation.module])
        render(successTrue)
    }

    @Transactional
    def update(Operation operation) {
        if (operation == null) {
            render(successFalse)
            return
        }
        if(!operation.validate()) {
            render([success:false,errors: errorsToResponse(operation.errors)] as JSON)
            return
        }
        operation.save flush: true
        event('upallumv',[module: operation.module])
        render(successTrue)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        Operation.executeUpdate("delete Operation where id in (:ids)",[ids:ids*.toLong()])
        event('upallumv',[module: operation.module])
        render(successTrue)
    }

    def save() {
        def data = JSON.parse(params.data)
        def privilegeInstance
        data.each {
            privilegeInstance = Module.get(it.id)
            privilegeInstance.properties = it
            if(!privilegeInstance.validate()) {
                render([success:false,errors: errorsToResponse(privilegeInstance.errors)] as JSON)
                return
            }
        }
        privilegeInstance.save(flush:true)
        render(successTrue )
    }
}
