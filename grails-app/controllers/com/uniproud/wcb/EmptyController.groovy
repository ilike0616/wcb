package com.uniproud.wcb

import grails.converters.JSON
import grails.transaction.Transactional

import static com.uniproud.wcb.ErrorUtil.*

/**
 * 空的Controller，不允许写方法
 */
@Transactional(readOnly = true)
class EmptyController {
}
