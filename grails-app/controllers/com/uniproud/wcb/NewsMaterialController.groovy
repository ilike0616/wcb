package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

@UserAuthAnnotation
class NewsMaterialController {

    def newsMaterialService
    def materialService

    def list(Long id){
        def data = []
        def news
        println params
        if(params.materId){
            println 'materId'
            def mater = Material.get(params.materId as Long)
            news = NewsMaterial.where {
                eq('material.id',mater.id)
            }.list([ sort: "idx", order: "asc"])
            news.each {
                if(it.doc){
                    println it
                    data << [id:it.id,type:'news',digest:it.digest,title:it.title,author:it.author,material:[id:it.material.id,title:it.material.title],content:it.content,idx:it.idx,weixin:[id:it.material.weixinId],dataCreated:it.dateCreated,url:it.doc.name,doc:it.doc]
                }
            }
        }else if(params.id){
            println 'id'
            def newsmater = NewsMaterial.get(params.id)
            news = NewsMaterial.where {
                eq('material.id',newsmater.material.id)
            }.list([ sort: "idx", order: "asc"])
            news.each {
                if(it.doc){
                    println it
                    data << [id:it.id,type:'news',digest:it.digest,title:it.title,author:it.author,material:[id:it.material.id,title:it.material.title],content:it.content,idx:it.idx,weixin:[id:it.material.weixinId],dataCreated:it.dateCreated,url:it.doc.name,doc:it.doc]
                }
            }
        }else{
            println 'else'
            news = NewsMaterial.where{
                eq('idx',1)
            }.list()
            news.each {
                if(it.material.weixinId == params.weixin as Long){
                    if(it.doc){
                        println it
                        data << [id:it.id,type:'news',digest:it.digest,title:it.title,author:it.author,material:[id:it.material.id,title:it.material.title],content:it.content,idx:it.idx,weixin:[id:it.material.weixinId],dataCreated:it.dateCreated,url:it.doc.name,doc:it.doc]//http://localhost:8080/wcb/
                    }
                }
            }
        }
        println data
        render ([success:true,data:data] as JSON)
    }

    def up_wx(Long id){
        def news = NewsMaterial.get(id)
        if(news.material.mediaId == null){
            materialService.create(news.material,news.material.weixin)
        }
        def errcode = newsMaterialService.create(news)
        if (errcode) {
            render([success:true] as JSON)
        }else{
            render([success:false] as JSON)
        }
    }

    def insert() {
        println params
        Material mater = new Material(params)
        mater.setType("image")
        mater.setIntroduction(params.content)
        mater.save()
        Material material
        NewsMaterial news = new NewsMaterial(params)
        if(params.newsType=='page'){
            material = new Material(params)
            material.properties["user"]= session.employee.user
            material.properties["employee"] = session.employee
            material.save flush:true
            news.setIdx(1)
            println news as JSON
        }else{
            material = NewsMaterial.get(params.pageId).getMaterial()
            def newsList = NewsMaterial.where { eq('material',material) }.list([ sort: "idx", order: "asc"])
            def idx = newsList.get((newsList.size()-1)).idx + 1
            news.setIdx(idx)
        }
        news.properties["user"]= session.employee.user
        news.properties["employee"] = session.employee
        news.setMaterial(material)
        news.save flush:true
        render ([success:true,materId:material.id] as JSON)
    }

    def update(Long id ){
        def news = NewsMaterial.get(id)
        news.properties = params
        if(news.hasErrors()){
            println news.errors
            render([success:false] as JSON)
        }else{
            news.save flush:true
            render ([success:true] as JSON)
        }
    }

    def destroy(Long id){
        NewsMaterial.get(id).delete flush: true
        render ([success:true] as JSON)
    }
}
