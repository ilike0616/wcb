package com.uniproud.wcb

import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

@Transactional
class NewsMaterialService {

    def create(NewsMaterial news) {
        def data
        def weixin = news.material.weixin
        def newsList = NewsMaterial.where {
            eq('material.id',news.material.id)
        }.list([ sort: "idx", order: "asc"])
        println newsList
        def m = []
        newsList.each {
            def map = [:]
            map.put("title", it.title)
            map.put("author", it.author)
            map.put("digest", it.digest)
            map.put("show_cover_pic", 1)
            map.put("content", it.content)
            map.put("content_source_url", it.content_source_url)
            map.put("thumb_media_id", news.material.mediaId)
            m << map
        }
        println m
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/material/add_news?access_token="+weixin.accessToken)
        http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
            body = [
                    "articles":m
            ]
            response.success = { resp, json ->
                println "success"
                println resp
                println json
                data = json
            }
        }
        if (data.errcode==null||data.errcode==0) {
            if (data.media_id) {
                news.material.setMediaId(data.media_id)
                println true
                return true
            }
        }else{
            return false
        }
    }
}
