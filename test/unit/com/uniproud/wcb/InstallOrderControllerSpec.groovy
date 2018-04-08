package com.uniproud.wcb


import grails.test.mixin.*
import spock.lang.*

@TestFor(InstallOrderController)
@Mock(InstallOrder)
class InstallOrderControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.installOrderInstanceList
        model.installOrderInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.installOrderInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        def installOrder = new InstallOrder()
        installOrder.validate()
        controller.save(installOrder)

        then: "The create view is rendered again with the correct model"
        model.installOrderInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        installOrder = new InstallOrder(params)

        controller.save(installOrder)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/installOrder/show/1'
        controller.flash.message != null
        InstallOrder.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def installOrder = new InstallOrder(params)
        controller.show(installOrder)

        then: "A model is populated containing the domain instance"
        model.installOrderInstance == installOrder
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def installOrder = new InstallOrder(params)
        controller.edit(installOrder)

        then: "A model is populated containing the domain instance"
        model.installOrderInstance == installOrder
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/installOrder/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def installOrder = new InstallOrder()
        installOrder.validate()
        controller.update(installOrder)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.installOrderInstance == installOrder

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        installOrder = new InstallOrder(params).save(flush: true)
        controller.update(installOrder)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/installOrder/show/$installOrder.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/installOrder/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def installOrder = new InstallOrder(params).save(flush: true)

        then: "It exists"
        InstallOrder.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(installOrder)

        then: "The instance is deleted"
        InstallOrder.count() == 0
        response.redirectedUrl == '/installOrder/index'
        flash.message != null
    }
}
