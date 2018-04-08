package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.errorsToResponse

@UserAuthAnnotation
@Transactional(readOnly = true)
class InstallOrderController {

    def baseService
    def modelService

    def list() {
        def emp = session.employee
        def emps = modelService.getEmployeeChildrens(emp.id)
        emps << emp
        def extraCondition = {
            if (params.installState == '1') {
                //1、待派单（a、按所有者上下级查询，b、安装状态为待派单，c、派单模式为指派模式 d、3条件必须同时满足）
                if (emps) {
                    inList('owner', emps)
                }
                eq('installState', 1)
                eq('allocateKind', 1)
            }else if(params.installState == '2'){
                //2、待处理（a、按接单人或者做单人查询，无上下级限定 b、安装状态为处理中  c、须同时满足前两个条件）
                if (emp) {
                    or {
                        eq('receivedMan',emp)
                        eq('executeMan',emp)
                    }
                }
                eq('installState', 2)
            }else if(params.installState == '3'){
                //3、已处理（a、按所有者或者接单人或者做单人查询，无上下级限定 b、安装单状态为已完成  c、须同时满足前两个条件）
                if (emp) {
                    or {
//                        eq('owner',emp)
                        eq('receivedMan',emp)
                        eq('executeMan',emp)
                    }
                }
                eq('installState', 3)
            }else if(params.installState == '4'){
                //待接单  （在InstallAllocateDetail中查询）a、按 receivedMan 查询，无上下级限定。 b、isClose != true ） isClose  字段需要在确定接单人时，置为true
                allocates{
                    eq('receivedMan',emp)
                    eq('isClose',false)
                }
            }else{
                if (emps) {
                    inList('owner', emps)
                }
            }
        }
        render modelService.getDataJSON('install_order', extraCondition,true,true)
    }

    /**
     * 待处理数量
     * @return
     */
    def getProcessingNum(){
        def emp = session.employee
        def num = InstallOrder.createCriteria().get{
            or {
                eq('receivedMan',emp)
                eq('executeMan',emp)
            }
            eq('installState', 2)
            projections {
                countDistinct "id"
            }
        }
        render ([success:true,num:num] as JSON)
    }

    /**
     * 已处理数量
     * @return
     */
    def getProcessedNum(){
        def emp = session.employee
        def num = InstallOrder.createCriteria().get{
            or {
                eq('receivedMan',emp)
                eq('executeMan',emp)
            }
            eq('installState', 3)
            projections {
                countDistinct "id"
            }
        }
        render ([success:true,num:num] as JSON)
    }

    def detailList(){
        def emp = session.employee
        def isExcludeEmp = false
        def isPaging = true
        def extraCondition = {
            if(params.object_id){
                eq("installOrder.id",params.object_id?.toLong())
            }
            order("lastUpdated","desc")
        }
        if(params.object_id){
            isExcludeEmp = true
            isPaging = false
        }
        render modelService.getDataJSON('install_order_detail',extraCondition,isPaging,isExcludeEmp)
    }

    @Transactional
    def insert(InstallOrder installOrder) {
        if (installOrder == null) {
            render baseService.error(params)
            return
        }
        installOrder.properties['user'] = session.employee?.user
        installOrder.properties['employee']= session.employee
        if (installOrder.hasErrors()) {
            def json = [success:false,errors: errorsToResponse(installOrder.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        baseService.save(params,installOrder)
        if(params.detail != null){
            def detail = JSON.parse(params.detail)
            //遍历明细、保存
            detail.each {
                InstallOrderDetail od = new InstallOrderDetail()
                bindData(od,it,[exclude:'id'])
                od.properties['user'] = session.employee?.user
                od.properties['employee']= session.employee
                od.properties['owner']= installOrder.owner
                if(it['product.id']){
                    od.product = Product.get(it['product.id']?.toLong())
                }else if(it.product?.id){
                    od.product = Product.get(it.product?.id?.toLong())
                }
                if (od.hasErrors()) {
                    println od.errors
                    def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                    render baseService.validate(params,json)
                    return
                }
                od.save()
                installOrder.addToDetail(od)
            }
            baseService.save(params,installOrder)
        }
        render baseService.success(params)
    }

    @Transactional
    def update() {
        InstallOrder installOrder = InstallOrder.get(params.id as Long)
        if(params.detail){
            def detail = JSON.parse(params.detail)
            detail.each {
                if(it.id){
                    InstallOrderDetail od = InstallOrderDetail.get(it.id)
                    od.properties = it
                    od.properties['user'] = session.employee?.user
                    od.properties['employee']= session.employee
                    od.properties['owner']= installOrder.owner
                    if(it['product.id']){
                        od.product = Product.get(it['product.id']?.toLong())
                    }else if(it.product?.id){
                        od.product = Product.get(it.product?.id?.toLong())
                    }
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                        render baseService.validate(params,json)
                        return
                    }
                    baseService.save(params,od,'install_order_detail')
                }else{
                    InstallOrderDetail od = new InstallOrderDetail(it)
                    od.properties['user'] = session.employee?.user
                    od.properties['employee']= session.employee
                    od.properties['owner']= installOrder.owner
                    if(it['product.id']){
                        od.product = Product.get(it['product.id']?.toLong())
                    }else if(it.product?.id){
                        od.product = Product.get(it.product?.id?.toLong())
                    }
                    if (od.hasErrors()) {
                        log.info od.errors
                        def json = [success:false,errors: errorsToResponse(od.errors)] as JSON
                        render baseService.validate(params,json)
                        return
                    }
                    od.installOrder = installOrder
                    baseService.save(params,od,'install_order_detail')
                }
            }
        }
        if(params.dels){
            def ids = JSON.parse(params.dels)
            if(ids.size()>0)
                InstallOrderDetail.executeUpdate("update InstallOrderDetail set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        }
        bindData(installOrder, params,[exclude:'detail'])
        render baseService.save(params,installOrder)
    }
    /**
     * 派单
     * @return
     */
    @Transactional
    def allocate(){
        if(!params.receivedMan || !params.installOrder){
            render baseService.error(params)
            return
        }
        def employee = session.employee
        def user = employee.user
        def installOrder = InstallOrder.get(params.installOrder as Long)
        installOrder.properties["allocateMan"] = session.employee   //派单人
        installOrder.properties["allocateDate"] = new Date()    //派单时间
        if(installOrder.allocateKind == 1) {
            def receivedMan = Employee.get(params.receivedMan?.toLong())
            installOrder.properties["receivedMan"] = receivedMan    //接单人
            installOrder.properties["receivedDate"] = new Date()    // 接单时间
            installOrder.properties["executeMan"] = receivedMan // 做单人
            installOrder.installState = 2
        }else{
            def receivedMans = params.receivedMan?.split(',')
            //删除取消分配的
            installOrder.allocates?.each{
                if(!(it.id in receivedMans)){
                    it.deleteFlag = true
                    it.save()
                }
            }
            receivedMans.each { id ->
                def receiver = Employee.get(id.toLong())
                if(receiver && !InstallAllocateDetail.findByReceivedManAndInstallOrderAndDeleteFlag(receiver,installOrder,false)) {
                    def allocate = new InstallAllocateDetail(user: user, employee: employee, owner: receiver, dept: receiver.dept, installOrder: installOrder, receivedMan: receiver)
                    baseService.save(params, allocate,'install_allocate_detail')
                    installOrder.addToAllocates(allocate)
                }
            }
        }
        render baseService.save(params,installOrder,'install_order')
    }
    /**
     * 转单
     * @return
     */
    @Transactional
    def transfer(){
        if(!params.executeMan || !params.installOrders){
            render baseService.error(params)
            return
        }
        def executeMan = Employee.get(params.executeMan?.toLong())
        def installOrders = JSON.parse(params.installOrders) as List
        installOrders.each {id->
            def installOrder = InstallOrder.get(id.toLong())
            installOrder.properties["executeMan"] = executeMan // 做单人
            baseService.save(params,installOrder,'install_order')
        }
        def result = ([success:true]) as JSON
        if(params.callback) {
            render "${params.callback}($result)"
        }else{
            render result
        }
    }

    @Transactional
    def delete() {
        def ids = JSON.parse(params.ids) as List
        InstallOrder.executeUpdate("update InstallOrder set deleteFlag=true where id in (:ids)",[ids:ids*.toLong()])
        InstallOrderDetail.executeUpdate("update InstallOrderDetail set deleteFlag=true where installOrder.id in (:ids)",[ids:ids*.toLong()])
        render baseService.success(params)
    }

    /**
     * 获取签到信息
     * @param id
     * @return
     */
    def getSignon(Long id){
        def install = InstallOrder.get(id)
        def sign = install?.signon
        if(sign){
            def photos = []
            sign.photos?.each {
                def p = Doc.get(it.id)
                photos << [id:p.id,name:p.name]
            }
            def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,
                                           installOrder:[id:install.id,subject:install.subject],photos:photos]] as JSON
            render json
        }else{
            render baseService.error(params)
        }
    }
    /**
     * 获取签退信息
     * @param id
     * @return
     */
    def getSignout(long id){
        def install = InstallOrder.get(id)
        def sign = install?.signout
        if(sign){
            def photos = []
            sign.photos?.each {
                def p = Doc.get(it.id)
                photos << [id:p.id,name:p.name]
            }
            def json = [success:true,data:[id:sign.id,longtitude:sign.longtitude,latitude:sign.latitude,location:sign.location,remark:sign.remark,
                                           installOrder:[id:install.id,subject:install.subject],photos:photos]] as JSON
            render json
        }else{
            render baseService.error(params)
        }
    }
    /**
     * 跟进中点拍照
     * @param id	跟进ID
     * @return
     */
    def getPhoto(long id){
        def install = InstallOrder.get(id)
        if(install){
            def records = []
            install.photos?.each{
                def record = OutsiteRecord.get(it.id)
                if(record&&record.actionKind==3){   //3-拍照
                    def photos = []
                    record.photos?.each {
                        def p = Doc.get(it.id)
                        photos << [id:p.id,name:p.name]
                    }
                    records << [
                            id:record.id,remark:record.remark,longtitude:record.longtitude,latitude:record.latitude,location:record.location,photos:photos,dateCreated:record.dateCreated,
                            employee:[
                                    employeeName:record.employee?.name,
                                    photo:[
                                            id:record.employee?.photo?.id,name:record.employee?.photo?.name
                                    ]
                            ]
                    ]
                }
            }
            def json = [success:true,data:[installOrder:[id:install.id,subject:install.subject],photos:records]] as JSON
            render json
        }else{
            render baseService.error(params)
        }
    }

    /**
     * 签到
     * @return
     */
    @Transactional
    def signon(){
        User user = session.employee?.user
        Employee employee = session.employee
        OutsiteRecord outsiteRecord = new OutsiteRecord(params)
        outsiteRecord.user = user
        outsiteRecord.employee = employee
        //验证
        if (!outsiteRecord.validate()) {
            println outsiteRecord.errors
            def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        //保存签到信息
        outsiteRecord.actionKind = 1
        outsiteRecord.save flush: true
        //更新跟进的相关签到信息
        InstallOrder installOrder = InstallOrder.get(outsiteRecord.installOrder?.id)
        installOrder.signon = outsiteRecord
        installOrder.signonDate = new Date()
        installOrder.save flush: true
        render baseService.success(params)
    }

    /**
     * 签退
     * @return
     */
    @Transactional
    def signout(){
        User user = session.employee?.user
        Employee employee = session.employee
        OutsiteRecord outsiteRecord = new OutsiteRecord(params)
        outsiteRecord.user = user
        outsiteRecord.employee = employee
        //验证
        if (!outsiteRecord.validate()) {
            def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        //保存签退信息
        outsiteRecord.actionKind = 2
        outsiteRecord.save flush: true
        //修改跟进的签退时间
        InstallOrder installOrder = InstallOrder.get(outsiteRecord.installOrder?.id)
        installOrder.signout = outsiteRecord
        installOrder.signoffDate = new Date()
        installOrder.save flush: true
        render baseService.success(params)
    }
    /**
     * 拍照
     * @return
     */
    @Transactional
    def photo(){
        User user = session.employee?.user
        Employee employee = session.employee
        OutsiteRecord outsiteRecord = new OutsiteRecord(params)
        outsiteRecord.user = user
        outsiteRecord.employee = employee
        //验证
        if (!outsiteRecord.validate()) {
            def json = [success:false,errors: errorsToResponse(outsiteRecord.errors)] as JSON
            render baseService.validate(params,json)
            return
        }
        //保存拍照信息
        outsiteRecord.actionKind = 3
        outsiteRecord.save flush: true
        render baseService.success(params)
    }
}
