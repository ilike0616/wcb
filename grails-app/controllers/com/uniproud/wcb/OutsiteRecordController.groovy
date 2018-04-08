package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import java.text.SimpleDateFormat

import static com.uniproud.wcb.ErrorUtil.*


@UserAuthAnnotation
@Transactional(readOnly = true)
class OutsiteRecordController {
    def modelService
	def baseService

    def list(){
        def extraCondition = {
//            if(params.searchValue){
//                or {
//                    customerFollow{
//                        ilike("subject","%$params.searchValue%")
//                    }
//                    objectFollow{
//                        ilike("subject","%$params.searchValue%")
//                    }
//                    serviceTask{
//                        ilike("subject","%$params.searchValue%")
//                    }
//                }
//            }
        }
        render modelService.getDataJSON('outsite_record',extraCondition)
    }

    @Transactional
    def insert(OnsiteObject onsiteObject) {
        def employee = session.employee
        if (onsiteObject == null) {
			render baseService.error(params)
            return
        }
        def finalName = SysTool.uploadFile(request,"tempFiles")
        onsiteObject.files = finalName
        onsiteObject.user = employee.user
        onsiteObject.employee = employee
        if (!onsiteObject.validate()) {
            def json = [success:false,errors: errorsToResponse(onsiteObject.errors)] as JSON
			render baseService.validate(params,json)
            return
        }
        onsiteObject.save flush: true
		render baseService.success(params)
    }

    @Transactional
    def update(OnsiteObject onsiteObject) {
        if (onsiteObject == null) {
            render baseService.error(params)
            return
        }
        if (!onsiteObject.validate()) {
            def json = [success:false,errors: errorsToResponse(onsiteObject.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        onsiteObject.save flush: true
        render baseService.success(params)
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        OutsiteRecord.executeUpdate("update OutsiteRecord set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
		render baseService.success(params)
    }

    /*@Transactional
    def photo(){
        def saveFileDir = grailsApplication.config.SAVE_FILE_DIR // 存放文件的目录
        def base64Str = params.doc
        if(base64Str){
            def ext = '.jpg'
            User user = session.employee?.user
            Employee employee = session.employee
            def fileName = SysTool.makeFileName(ext)
            def finalFileName = baseService.getUserMonthDir(employee) + fileName
            def uploadSucc = baseService.transBase64TextToFile(base64Str,finalFileName)
            if(uploadSucc == true){
                File file = new File(saveFileDir + "/" + finalFileName)
                Doc doc = new Doc(user: user,employee: employee,name: finalFileName,orgName: fileName,ext: "jpg",fileSize: file.length()).save()
                def msg = [success:true,msg:"上传成功",docSeed:doc.id] as JSON
                if(params.callback) {
                    render "${params.callback}($msg)"
                }else{
                    render(msg)
                }
            }else{
                if(params.callback) {
                    render "${params.callback}($uploadErr)"
                }else{
                    render(uploadErr)
                }
            }
        }else{
            if(params.callback) {
                render "${params.callback}($uploadErr)"
            }else{
                render(uploadErr)
            }
        }
    }*/

    @Transactional
    def locate(){
        if(params.objectId){
            def customer = Customer.get(params.objectId.toLong())
            customer.properties = params
            customer.save(flush: true)
        }
    }

}
