package filters

import com.uniproud.wcb.Employee
import grails.converters.JSON

class AuthFilters {
    def annotationService
    def baseService
    def validationService
    def filters = {
        all(controller: '*', action: '*') {
            before = {
//                log.info params
                if ((controllerName != null && actionName != null) &&
                     (!"assets".equals(controllerName) &&
                             !"dbdoc".equals(controllerName) &&
                             !"login".equals(controllerName) &&
                             !"logout".equals(controllerName))) {
                    String u = request.getHeader('U')//获取操作类型
                    String uid = request.getHeader('uid')
                    if(u==null){
                        if(request.getHeader('Referer')?.contains('u.html')){
                            u = 'user'
                        }else if(request.getHeader('Referer')?.contains('admin.html')){
                            u = 'admin'
                        }else if(request.getHeader('Referer')?.contains('ag.html')){
                            u = 'agent'
                        }else{
                            u = 'user'
                        }
                    }
                    params.u=u
                    def accessCtrlMethod = controllerName + "_" + actionName
                    if(!annotationService.normalAllowList?.contains(accessCtrlMethod)) {
                        if (u == 'user' && session.employee == null) {
                            response.setStatus(401)
                            return false
                        }
                        if (u == 'admin' && session.admin == null) {
                            response.setStatus(401)
                            return false
                        }
                        switch (u) {
                            case 'admin':
                                if (session.admin) {
                                    log.info "admin:${session.admin.adminId} params:${params}"
                                    if (!annotationService.adminAllowList?.contains(accessCtrlMethod) && !annotationService.normalAllowList?.contains(accessCtrlMethod)) {
                                        response.setStatus(403)
                                        return false
                                    }
                                }
                                break
                            case 'user':
                                if (session.employee) {
                                    if(uid && session.employee.user?.userId != uid){
                                        response.setStatus(402)
                                        return false
                                    }
                                    log.info "emp:${session.employee.name} params:${params}"
                                    //                                println annotationService.userAllowList
                                    if (!annotationService.userAllowList?.contains(accessCtrlMethod) && !annotationService.normalAllowList?.contains(accessCtrlMethod)) {
                                        response.setStatus(403)
                                        return false
                                    }
                                    if(params.moduleId){
                                        // 验证提交的数据
                                        def errors =  validationService.formValidation(params)
                                        if(errors){
                                            def json = [success:false,errors: errors] as JSON
                                            render baseService.validate(params,json)
                                            return false
                                        }
                                    }
                                    if(params.moduleId != 'public_customer' && !params.containsKey('owner')&&params.action=='insert'){
                                        params.owner = session.employee.id
                                    }
                                    if(params.owner){
                                        def ownerId
                                        if(params.owner?.class?.getName() == Long.class.getName() || params.owner?.class?.getName() == String.class.getName()){
                                            ownerId = params.owner
                                        }else{
                                            ownerId = params.owner?.id
                                        }
                                        def owner = Employee.get(ownerId as Long)
                                        if(owner)
                                            params.dept = owner.dept?.id
                                    }
                                }
                                break
                            case 'agent':
                                if (session.agent) {
                                    log.info "agent:${session.agent.name} params:${params}"
                                    if (!annotationService.agentAllowList?.contains(accessCtrlMethod) && !annotationService.normalAllowList?.contains(accessCtrlMethod)) {
                                        response.setStatus(403)
                                        return false
                                    }
                                }
                                break
                            default:
                                if (session.user) {
                                    if (!annotationService.userAllowList?.contains(accessCtrlMethod) && !annotationService.normalAllowList?.contains(accessCtrlMethod)) {
                                        response.setStatus(403)
                                        return false
                                    }
                                } else {
                                    if (!annotationService.normalAllowList?.contains(accessCtrlMethod)) {
                                        response.setStatus(403)
                                        return false
                                    }
                                }
                                break
                        }
                    }
                }
//                println request.getServletPath()
//                if ((controllerName != null && actionName != null) && (!"assets".equals(controllerName) && !"dbdoc".equals(controllerName)
//                         && !"index".equals(actionName)) && (!"login".equals(controllerName) && !"login".equals(actionName))) {
//                    def key
//                    if(session.employee){
//                        key = 'user'
//                    }else if(session.agent){
//                        key = 'agent'
//                    }else if(session.admin){
//                        key = 'admin'
//                    }
//                    def methodAnnotationsMap = annotationService.methodAnnotationsMap
//                    println(methodAnnotationsMap)
//                    def annotationsList = methodAnnotationsMap.get(key)
//                    def normalAnnotationsList = methodAnnotationsMap.get('normal')
//                    def accessCtrlMethod = controllerName + "_" + actionName
//                    if(key){ // session不为空
//                        if(!normalAnnotationsList.contains(accessCtrlMethod) && !annotationsList.contains(accessCtrlMethod)){
//                            response.setStatus(403)
//                            return false
//                        }
//                    }else{
//                        if(!normalAnnotationsList.contains(accessCtrlMethod)){
//                            redirect(uri: '/')
//                            return false
//                        }
//                    }
//                }
            }
            after = { Map model ->
            }
            afterView = { Exception e ->

            }
        }
    }
}
