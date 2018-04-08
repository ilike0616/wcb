package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

@UserAuthAnnotation
class MaterialController {
    def materialService

    def list(Long id){
        def materlList
        if (params.weixin && params.type) {
            materlList = Material.where {
                eq('weixin.id',params.weixin as Long)
                eq('type',params.type)
            }.list()
        }
        def data = []
        if(params.type == 'text'){
            materlList.each{
                data << [id:it.id,weixin:[id:it.weixin.id],dataCreated:it.dateCreated,title:it.title,introduction:it.introduction,type:it.type,content:it.content]
            }
        }else{
            materlList.each{
                if(it.doc){
                    data << [id:it.id,url:it.doc.name,doc:[id:it.doc.id],orgName:it.doc.orgName,weixin:[id:it.weixin.id],dataCreated:it.dateCreated,title:it.title,introduction:it.introduction,type:it.type]
                }
            }
        }
        render ([success:true,data:data] as JSON)
    }

    def up_wx(Long id){
        println params
        def mater = Material.get(id)
        if(mater.type !='text'){
            def jsondata = materialService.create(mater,mater.weixin)
            if (jsondata) {
                render([success:true] as JSON)
            }else{
                render([success:false] as JSON)
            }
        }
        render([success:true] as JSON)
    }

    def insert() {
        def doc = Doc.get(params.doc)
        Material material = new Material(params)
        material.properties["user"]= session.employee.user
        material.properties["employee"] = session.employee
        material.save(flush:true)
        render ([success:true] as JSON)
    }

    def delete(Long id){
        println params
        def mater = Material.get(id)
        mater.delete flush: true
        render ([success:true] as JSON)
    }

    def update(Long id){
        println 'update'
        println params
        def material = Material.get(id)
        material.properties = params
        if(material.hasErrors()){
            println material.errors
            render([success:false] as JSON)
        }else{
            material.save flush:true
            render ([success:true] as JSON)
        }
    }
}
