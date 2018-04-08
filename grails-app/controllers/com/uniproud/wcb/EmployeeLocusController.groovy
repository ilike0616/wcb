package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
@UserAuthAnnotation
class EmployeeLocusController {
    def modelService
    def baseService

    def list(){
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        def fns = Field.where{
            model{ modelClass == Employee.name }
        }.list()?.fieldName
        def extraCondition = {
            if(params.searchValue){
                or {
                    ilike("name","%$params.searchValue%")
                }
            }
            isNotNull("location")
            isNotNull("longtitude")
            isNotNull("latitude")
        }
        def searchCondition = modelService.getSearchCondition(extraCondition,fns,params,emp,emps,false,"employee")
        def result = Employee.createCriteria().list(params){
            searchCondition.delegate = delegate
            searchCondition()
        }
        def data = []
        result.each { rs ->
            data << [id:rs.id,name:rs.name,photo:rs.photo?.name,location:rs.location,longtitude:rs.longtitude,latitude:rs.latitude,locusDate:rs.locusDate]
        }
        def json = [success:true,data:data,total:result.totalCount] as JSON
        log.info(json)
        render json
    }
}
