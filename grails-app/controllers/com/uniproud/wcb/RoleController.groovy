package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

@Transactional(readOnly = true)
class RoleController {
/****
 *  def list(){
 RequestUtil.pageParamsConvert(params)
 if(session.employee){
 params.userId = session.employee.user?.id
 }
 def query = Role.where{
 if(params.userId){
 user{
 id == params.userId
 }
 }
 }

 def roles = []
 query.list(params).each {
 roles << [id:it.id,roleName:it.roleName,user:it?.user?.id,userName:it?.user?.userName,
 depts:it?.depts?.id,deptsNames:it?.depts?.name,employees:it?.employees?.id,employeesNames:it?.employees?.name,
 privileges:it?.privileges?.id,privilegesNames:it?.privileges?.name]
 }
 def json = [success:true,roles:roles,total:query.count()] as JSON
 log.info(json)
 if(params.callback) {
 render "${params.callback}($json)"
 }else{
 render(json)
 }
 }

 @Transactional
  def insert(Role role) {
  log.info params
  if (role == null) {
  render(successFalse)
  return
  }
  if(session.employee){
  role.user = session.employee.user
  }

  if (!role.validate()) {
  render([success:false,errors: errorsToResponse(role.errors)] as JSON)
  return
  }
  role.save flush: true
  render(successTrue)
  }

 @Transactional
  def update(Role role) {
  log.info(role)
  if (role == null) {
  render(successFalse)
  return
  }
  if (!role.validate()) {
  render([success:false,errors: errorsToResponse(role.errors)] as JSON)
  return
  }
  role.save flush: true
  render(successTrue)
  }

 @Transactional
  def delete() {
  def ids = JSON.parse(params.ids) as List
  Role.executeUpdate("delete Role where id in (:ids)",[ids:ids*.toLong()])
  render(successTrue )
  }

 @Transactional
  def save() {
  def data = JSON.parse(params.data)
  def role = Role.get(data["id"])
  role.properties = data
  if(!role.validate()) {
  render([success:false,errors: errorsToResponse(role.errors)] as JSON)
  return
  }
  role.save(flush: true)
  render(successTrue )
  }*/
}
