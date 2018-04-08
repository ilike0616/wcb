package com.uniproud.wcb

import com.uniproud.wcb.annotation.UserAuthAnnotation
import grails.transaction.Transactional

@UserAuthAnnotation
@Transactional(readOnly = true)
class EmployeeLimitDetailController {

    def list() {
    }

    @Transactional
    def insert(EmployeeLimit employeeLimit) {
    }

    @Transactional
    def update(EmployeeLimit employeeLimit) {
    }

    @Transactional
    def delete() {
    }
}
