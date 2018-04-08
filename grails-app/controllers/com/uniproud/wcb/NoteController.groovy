package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
@UserAuthAnnotation
class NoteController {

	def modelService
	def baseService
	def grailsApplication

	def list(){
		def emp = session.employee
		def emps = modelService.getEmployeeChildrens(emp.id)
		def fns = Field.where{
			model{ modelClass == Note.name }
		}.list()?.fieldName
		def extraCondition = {
			if(params.searchValue){
				or {
					ilike("content","%$params.searchValue%")
				}
			}
		}
		def isExcludeEmp = false
		if(params.isExcludeEmp && params.isExcludeEmp.toBoolean() == true){
			isExcludeEmp = true
		}
		def searchCondition = modelService.getSearchCondition(extraCondition,fns,params,emp,emps,isExcludeEmp)
		def result = Note.createCriteria().list(params){
			searchCondition.delegate = delegate
			searchCondition()
			if(params.customer){
				eq('customer.id',params.customer as Long)
			}
		}
		def fileUrl = grailsApplication.config.HTTP_FILE_URL
		def data = []
		result.each{rs->
			def ats= [],zs= [],files= [],voices= [],comments = []
			rs.ats?.each{at->
				ats << [id:at?.id,name:at?.name]
			}
			rs.zs?.each{z->
				zs << [id:z?.employee?.id,name:z?.employee?.name]
			}
			rs.files?.each {doc->
				files << [id:doc?.id,name:doc?.name,orgName:doc?.orgName,ext:doc?.ext,fileSize:doc?.fileSize,dateCreated:doc?.dateCreated]
			}
			rs.voices?.each {doc->
				voices << [id:doc?.id,name:doc?.name,orgName:doc?.orgName,ext:doc?.ext,fileSize:doc?.fileSize,dateCreated:doc?.dateCreated]
			}
			Comment.findAllByNote(Note.get(rs.id),[sort: "dateCreated", order: "asc"])?.each{comment->
				comments << [id:comment?.id,employee:[id:comment?.employee?.id,name:comment?.employee?.name,fileUrl:fileUrl,photo:comment?.employee?.photo?.name],content:comment?.content,dateCreated:comment?.dateCreated]
			}
			def employee = rs.employee
			data << [id:rs.id,employee:[id:employee.id,name:employee.name,fileUrl:fileUrl,photo:employee.photo?.name],
				tag:rs.tag,content:rs.content,location:rs.location,longtitude:rs.longtitude,latitude:rs.latitude,dateCreated:rs.dateCreated,
				ats:ats,zs:zs,zsNum:zs.size,files:files,voices:voices,comments:comments,commentsNum:comments.size]
		}
		def json = [success:true,data:data,total:result.totalCount] as JSON
		render json
	}

	@Transactional
	def insert(Note note) {
		if (note == null) {
			render baseService.error(params)
			return
		}
		note.properties["user"] = session.employee?.user
		note.properties["employee"] = session.employee
		render baseService.save(params,note)
	}

	@Transactional
	def zs() {
		def emp = session.employee
		def opt = 0
		if(params.note){
			def note = Note.get(params.note)
			if(note?.zs?.employee?.id.contains(emp.id)){
				def noteZs = NoteZs.findByNoteAndEmployee(note,emp)
				note.removeFromZs(noteZs)
				noteZs.delete()
				opt = 1
			}else{
				def noteZs = new NoteZs(note:note,employee:emp).save()
				note.addToZs(noteZs)
			}
			note.save flush:true
		}
		render ([success:true,msg:"操作成功！",type:opt] as JSON)
	}

	@Transactional
	def comment(Comment comment){
		if (comment == null) {
			render baseService.error(params)
			return
		}
		comment.properties["employee"] = session.employee
		render baseService.save(params,comment)
	}
}
