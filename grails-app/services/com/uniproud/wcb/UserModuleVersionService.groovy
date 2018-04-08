package com.uniproud.wcb

import grails.events.Listener
import grails.transaction.Transactional
import org.grails.plugin.platform.events.EventMessage
import org.springframework.transaction.annotation.Isolation

class UserModuleVersionService {
    def grailsApplication
    def static listenerClass = [
         'com.uniproud.wcb.View',
         'com.uniproud.wcb.ViewDetail',
         'com.uniproud.wcb.ViewDetailExtend',
         'com.uniproud.wcb.UserFieldExtend',
         'com.uniproud.wcb.PrivilegeViewDetail',
         'com.uniproud.wcb.UserOperation',
         'com.uniproud.wcb.ViewOperation',
         'com.uniproud.wcb.UserField'
    ]

    /**
     * 定义事件 upumv
     * @param msg
     * @return
     */
    @Listener(topic = 'upumv')
    def updateVer(EventMessage msg) {
        def user = msg.data.user
        def module = msg.data.module
        if (user && module) {
            this.updateVer(user, module)
        }
    }

    @Listener(topic = 'upallumv')
    def updateAllVer(EventMessage msg) {
        def module = msg.data.module
        if (module) {
            User.findAll()?.each {user->
                this.updateVer(user, module)
            }
        }
    }

 /*   *//**
     * 监听 domian 的 afterDelete 事件，修改module的版本号
     * @param domain
     * @return
     */
//    @Listener(namespace = 'gorm',topic = 'afterDelete')
    def afterDeleteDomain(domain){
        this.afterDomain(domain)
    }
    /**
     * 监听 domian 的 afterInsert 事件，修改module的版本号
     * @param domain
     * @return
     */
//    @Listener(namespace = 'gorm',topic = 'afterInsert')
    def afterInsertDomain(domain){
        this.afterDomain(domain)
    }
    /**
     * 监听 domian 的 afterUpdate 事件，修改module的版本号
     * @param domain
     * @return
     */
//    @Listener(namespace = 'gorm',topic = 'afterUpdate')
    def afterUpdateDomain(domain){
        this.afterDomain(domain)
    }
    /**
     * 监听 domian 事件，修改module的版本号的最终实现方法
     * @param domain
     * @return
     */
    def afterDomain(domain){
        if (grailsApplication.config.dataSource.dbCreate == 'update') {
            def domainClass = domain.class.name
            if (listenerClass.contains(domainClass)) {
                def user
                def module
                def dom = grailsApplication.getArtefact("Domain", domainClass)
                if (dom.getPersistentProperty('user')) {
                    user = domain.user
                }
                if (dom.getPersistentProperty('module')) {
                    module = domain.module
                } else {
                    if (domainClass == 'com.uniproud.wcb.ViewDetail') {
                        module = domain.view.module
                    } else if (domainClass == 'com.uniproud.wcb.ViewDetailExtend' || domainClass == 'com.uniproud.wcb.PrivilegeViewDetail') {
                        user = domain.viewDetail.user
                        module = domain.viewDetail.view.module
                    } else if (domainClass == 'com.uniproud.wcb.UserFieldExtend') {
                        user = domain.userField.user
                        module = domain.userField.module
                    }
                }
                if (user && module) {
                    log.info "domainClass 变更。触发了、数据模型变更. user:$user module:$module"
                    this.updateVer(user, module)
                }
            }
        }
    }
    /**
     * 更改模块的版本号
     * @param user
     * @param module
     */
    def updateVer(User user,Module module) {
        UserModuleVersion.withNewTransaction {status ->
            def userModuleVersion = UserModuleVersion.findByUserAndModule(user,module)
            if(userModuleVersion){
                userModuleVersion.ver = userModuleVersion.ver+1
                userModuleVersion.save(flush: true)
            }else{
                event('initUmv',user)
            }
        }
    }


}
