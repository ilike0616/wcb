package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
//import org.apache.http.entity.mime.HttpMultipartMode
//import org.apache.http.entity.mime.MultipartEntity
//import org.apache.http.entity.mime.content.InputStreamBody
import org.mozilla.javascript.tools.idswitch.FileBody

import static groovyx.net.http.ContentType.*

@Transactional
class MaterialService {
    def grailsApplication
    def create1(Material mater,Weixin weixin) {
        String uploadMediaUrl
        String fileExt = mater.doc.ext
        if (mater.type=='video') {
            def desjson = [title:mater.title,introductions:mater.introduction] as JSON
            uploadMediaUrl ="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=${weixin.accessToken}&&type=${mater.type}&&description="+desjson+""
        }else{
            uploadMediaUrl ="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=${weixin.accessToken}&type=${mater.type}"
        }
        def http = new HTTPBuilder(uploadMediaUrl)

        http.request (Method.POST, JSON) { multipartRequest ->

            requestContentType = 'multipart/form-data'

//            MultipartEntity mpe = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
//
//            mpe.addPart( "jpeg", new File('H:/EXT/xswy/uniproud/201601/20160123141431781.jpg') , 'image/jpeg' )
//            mpe.addPart("imageFile", new InputStreamBody(multipartImageFile.inputStream, multipartImageFile.contentType, multipartImageFile.originalFilename))
////            multipartRequest.setEntity(mpe)
//
//
//            response.success = { resp, json->
//
//                println "POST response status: ${resp.statusLine}"
//
//                println "Query response: ${json}"
//
//            }
//            response.failure = {  resp ->
//
//                println "POST response statusline: ${resp.statusLine}"
//
//            }
        }
/*        def reader = http.post( body: new File('H:/EXT/xswy/uniproud/201601/20160123141431781.jpg').bytes, requestContentType: BINARY)
        println "============================================="
        println reader
        def out = new ByteArrayOutputStream()
        out << reader
        println "lllllllllllllllllllllllllllllllllllllllll"
        println out*/
//        def http = new HTTPBuilder(uploadMediaUrl)
//        def result = null
//
//        // perform a ${method} request, expecting TEXT response
//        http.request(bytes,Method.POST, ContentType.BINARY) {
////                uri.path = path
//            body=new File('H:/EXT/xswy/uniproud/201601/20160123141431781.jpg')
//            response.success = { resp, reader ->
//                result = reader
//                log.debug(result as JSON)
//                println(result as JSON)
//            }
//        }
    }
    def create(Material mater,Weixin weixin) {
        String uploadMediaUrl
        String fileExt = mater.doc.ext
        println fileExt
        if (mater.type=='video') {
            println "video"
            def description = [:]
            description.put("title", mater.title)
            description.put("introduction", mater.introduction)
            def desjson = description as JSON
            uploadMediaUrl ="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=${weixin.accessToken}&&type=${mater.type}&&description="+desjson+""
        }else{
            uploadMediaUrl ="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=${weixin.accessToken}&type=${mater.type}"
        }
        try {
            String boundary = "------------7da2e536604c8"
            URL uploadUrl = new URL(uploadMediaUrl)
            HttpURLConnection uploadconn = (HttpURLConnection)uploadUrl.openConnection()
            /**
             * 设置关键值
             */
            uploadconn.setDoOutput(true)
            uploadconn.setDoInput(true)
            uploadconn.setRequestMethod("POST")//post方式提交  默认get
            // 设置请求头信息  边界
            uploadconn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary)
            OutputStream ops = uploadconn.getOutputStream()
            URL mediaurl = new URL(grailsApplication.config.HTTP_FILE_URL+File.separator+mater.doc.name)
            HttpURLConnection mediaconn = mediaurl.openConnection()
            mediaconn.setDoOutput(true)
            mediaconn.setRequestMethod("GET")
            String contenttype = mediaconn.getHeaderField("Content-Type")
            //第一部分// 必须多两道线
            ops.write(("--"+boundary+"\r\n").getBytes())
            ops.write(String.format("Content-Disposition:form-data;name=\"media\";filename=\""+mater.doc.name+"%s\"\r\n;", fileExt).getBytes())
            ops.write(String.format("Content-Type: %s\r\n\r\n","application/json;charset=UTF-8").getBytes())
            //文件正文部分  写入二进制
            BufferedInputStream bis = new BufferedInputStream(mediaconn.getInputStream())
            byte[] buf = new byte[8096]
            int size = 0
            while((size=bis.read(buf))!= -1){
                ops.write(buf,0,size)
            }
            //结尾
            ops.write(("\r\n--"+boundary+"--\r\n").getBytes())
            ops.close()
            bis.close()
            mediaconn.disconnect()
            //获取媒体文件上传的输入流（从微信服务器读数据）
            InputStream inputStream = uploadconn.getInputStream()
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8")
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            StringBuffer  buffer = new StringBuffer()
            String str = null;
            while ((str = bufferedReader.readLine())!=null){
                buffer.append(str)
            }
            bufferedReader.close()
            inputStreamReader.close()
            inputStream.close()
            inputStream = null
            uploadconn.disconnect()
            //
            String data = buffer.toString()
            def jsondata  = JSON.parse(data)
            println jsondata
            if (jsondata.errcode==null||jsondata.errcode==0) {
                if (jsondata.url) {
                    mater.setUrl(jsondata.url)
                }
                if (jsondata.media_id) {
                    mater.setMediaId(jsondata.media_id)
                    return true
                }
                return true
            }else{
                return false
            }
        } catch (Exception e) {
            println "exception"
            e.printStackTrace()
        }
        //		def errcode
        //		def file = new File('D:/ggts_workspace/weixin/web-app/images/1445394991980.mp3')
        //		println  file.size()
        //		byte[] buffer = null;
        //		FileInputStream fis = new FileInputStream(file);
        //		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //		byte[] b = new byte[1024];
        //		int n;
        //		while ((n = fis.read(b)) != -1)
        //		{
        //			bos.write(b, 0, n);
        //		}
        //		fis.close();
        //		bos.close();
        //		buffer = bos.toByteArray();
        //      def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=${weixin.accessToken}&type=voice")
        //		http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
        //			body = [media:buffer]
        //			//设置请求头信息
        ////			headers.'User-Agent' = 'Mozill/5.0'
        ////          uri.query = ['access_token':weixin.accessToken,"type":"voice","media":buffer]
        //			response.success = { resp, json ->
        //				println 'success'
        //				println resp
        //                println json
        //				errcode = json.errcode
        //            }
        //        }
        //		errcode
    }

    def delete(Weixin weixin,String media_id){
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=${weixin.accessToken}")
        http.request(POST, ContentType.JSON) { req ->
			body = [media_id:media_id]
			response.success = { resp, json ->
				println 'success'
				println resp
                println json
				errcode = json.errcode
            }
        }
    }
}
