package com.uniproud.wcb

import com.uniproud.wcb.annotation.AdminAuthAnnotation
import com.uniproud.wcb.annotation.AgentAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.getSuccessFalse
import static com.uniproud.wcb.ErrorUtil.getSuccessTrue

@AdminAuthAnnotation
@AgentAuthAnnotation
@Transactional(readOnly = true)
class UserOptConditionDetailController {
    def baseService
    def list(){
        RequestUtil.pageParamsConvert(params)
        def module = Module.get(params.module?.toLong())
        def userFields = [:]
        UserField.where {
            user{
                id == params.user?.toLong()
            }
            module == module
        }.each {
            userFields << [(it.fieldName):it]
        }
        def employees = [:]
        Employee.where {
            user{
                id == params.user?.toLong()
            }
        }.list().each {
            employees << [(it.id):it.name]
        }
        def query = UserOptConditionDetail.where{
            userOptCondition {
                id == params.userOptCondition?.toLong()
            }
        }
        def userOptConditionDetails = []
        query.list(params).each {
            def valueText = ""
            def userField = userFields.get(it.fieldName)
            if(userField.dbType == 'com.uniproud.wcb.Employee' && it.value != 'me'){
                it.value?.tokenize(",").each {v->
                    if(v != 'me'){
                        valueText += employees.get(v.toLong())+","
                    }else{
                        valueText += 'me,'
                    }
                }
                if(valueText){
                    valueText = valueText.substring(0,valueText.length()-1)
                }
            }else if(userField.dict){
                def dictItems = userField.dict.items
                it.value?.tokenize(",").each {v->
                    dictItems.each{item->
                        if(item.itemId == v.toLong()){
                            valueText += item.text+","
                        }
                    }
                }
                if(valueText){
                    valueText = valueText.substring(0,valueText.length()-1)
                }
            }else{
                valueText = it.value
            }
            userOptConditionDetails << [
                id:it.id,fieldName:it.fieldName,fieldNameText:userFields.get(it.fieldName).text,operator:it.operator,userOptCondition:it.userOptCondition.id,value:it.value,
                valueText:valueText,valueFlag:it.valueFlag,userOptConditionName:it.userOptCondition.name,dateCreated:it.dateCreated,lastUpdated:it.lastUpdated
            ]
        }
        def json = [success:true,data:userOptConditionDetails, total: query.count()] as JSON
        if(params.callback) {
            render "${params.callback}($json)"
        }else{
            render(json)
        }
    }

    @Transactional
    def insert(UserOptConditionDetail userOptConditionDetail) {
        if (userOptConditionDetail == null) {
            render(successFalse)
            return
        }
        if(params.valueMe){
            if(userOptConditionDetail.value){
                userOptConditionDetail.properties["value"] = userOptConditionDetail.value + "," + params.valueMe
            }else{
                userOptConditionDetail.properties["value"] = params.valueMe
            }
        }
        event('upumv',[user: userOptConditionDetail.userOptCondition.user,module: userOptConditionDetail.userOptCondition.userOperation.module])
        render baseService.save(params, userOptConditionDetail)
    }
    @Transactional
    def update(UserOptConditionDetail userOptConditionDetail) {
        if(params.valueMe && userOptConditionDetail.value?.indexOf(params.valueMe) < 0){
            if(userOptConditionDetail.value){
                userOptConditionDetail.properties["value"] = userOptConditionDetail.value + "," + params.valueMe
            }else{
                userOptConditionDetail.properties["value"] = params.valueMe
            }
        }
        event('upumv',[user: userOptConditionDetail.userOptCondition.user,module: userOptConditionDetail.userOptCondition.userOperation.module])
        render baseService.save(params, userOptConditionDetail)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        UserOptConditionDetail.executeUpdate("delete UserOptConditionDetail where id in (:ids)", [ids: ids*.toLong()])
        def first = UserOptConditionDetail.get(ids[0] as Long)
        if(first){
            event('upumv',[user: first.userOptCondition.user,module: first.userOptCondition.userOperation.module])
        }
        render(successTrue )
    }

    def loadFieldList() {
        def module = Module.get(params.module.toLong())
        def fields = UserField.createCriteria().list {
            eq("user.id", params.user?.toLong())
            eq("module.id", module.id)
            or{
                and{
                    not{like("fieldName","%.id")}
                    isNull("relation")
                }
                and{
                    isNotNull("relation")
                    eq("dbType","com.uniproud.wcb.Employee")
                }
            }
        }
        def list = []
        fields.each {it->
            def fieldName = it.fieldName?.tokenize(".")
            if(it.fieldName.contains(".")){
                list << [fieldName: it.fieldName, fieldText: it.text, dbType: it.dbType, dict: it.dict?.id]
            }else{
                if((it.fieldName == 'id' && it.dbType == 'com.uniproud.wcb.Employee') || it.fieldName != 'id'){
                    if(it.dbType == 'com.uniproud.wcb.Employee'){
                        def toMany = false
                        if(it.relation == 'OneToMany' || it.relation == 'ManyToMany'){
                            toMany = true
                        }else{
                            toMany = false
                        }
                        list << [fieldName: it.fieldName, fieldText: it.text, dbType: it.dbType, dict: it.dict?.id,toMany: toMany]
                    }else{
                        list << [fieldName: it.fieldName, fieldText: it.text, dbType: it.dbType, dict: it.dict?.id]
                    }
                }
            }
        }
        def json = [success: true, data: list] as JSON
        render json
    }

}
