package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder
import javax.net.ssl.HttpsURLConnection
import static groovyx.net.http.Method.POST

@Transactional
class QrCodeService {

    /**
     * 创建二维码ticket
     * action_name二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
     * expire_seconds该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * action_info二维码详细信息
     * scene_id场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * scene_str场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段
     */
    def getTicket(Weixin weixin,scene_id,type){
        QRCode qrcode = new QRCode()
        println qrcode
        def ticket
        def action_name
        def scene = [:]
        def action_info = [:]
        def bodys = [:]
        if(type == 'QR_LIMIT_STR_SCENE'){
            action_name = 'QR_LIMIT_STR_SCENE'
            scene.put("scene_str",scene_id)
            action_info.put("scene",scene)
            qrcode.setSceneStr(scene_id)
            qrcode.setType(2)
        }else{
            if(type == 'QR_LIMIT_SCENE'){
                qrcode.setType(2)
                action_name = 'QR_LIMIT_SCENE'
            }
            if(type == 'QR_SCENE'){
                qrcode.setType(1)
                action_name = 'QR_SCENE'
                bodys.put("expire_seconds",2592000)     //30天
                def date = new Date().plus(30)
                println date.format('yy/MM/dd HH:mm')
                qrcode.setDateExpire(new Date().plus(30))
            }
            scene.put("scene_id",scene_id)
            action_info.put("scene",scene)
            qrcode.setSceneId(scene_id as Integer)
        }
        bodys.put("action_name",action_name)
        bodys.put("action_info",action_info)
        println bodys as JSON
        def http = new HTTPBuilder("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=${weixin.accessToken}")
        http.request(POST, groovyx.net.http.ContentType.JSON) { req ->
            body = bodys
            response.success = { resp, json ->
                ticket= json.ticket
            }
        }
        if(ticket){
            def  savePath = "E://新建文件夹"
            getQRCode(qrcode,ticket,savePath)
        }else{
            println 'error'
            return false
        }
    }

    /**
     * 通过ticket换取二维码
     */
    def getQRCode(QRCode qrcode,ticket,savePath){
        String filepath = null
        String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}"
        try{
            URL url = new URL(requestUrl)
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection()
            conn.setRequestMethod("GET")
            if(!savePath.endsWith("/")){
                savePath += "/"
            }
            filepath = savePath+ticket+'.jpg'
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream())
            File file = new File(filepath)
            FileOutputStream fos = new FileOutputStream(file)
            byte[] buf = new  byte[8096]
            int size = 0
            while((size = bis.read(buf)) != -1){
                fos.write(buf,0,size)
            }
            fos.close()
            bis.close()
            conn.disconnect()
            log.info('------------------------------------------')
            def user = WebUtilTools.session.employee.user
            def employee = WebUtilTools.session.employee
            Doc doc = new Doc()
            doc.setExt('.jpg')
            doc.setFileSize(file.size())
            doc.setName(ticket)
            doc.setEmployee(employee)
            doc.setUser(user)
            doc.save(flush:true)
            qrcode.setDoc(doc)
            qrcode.setEmployee(employee)
            qrcode.setUser(user)
            qrcode.setDept(employee.dept)
            qrcode.save()
            return true
        }catch(Exception e ){
            println e
            return false
        }
    }
}
