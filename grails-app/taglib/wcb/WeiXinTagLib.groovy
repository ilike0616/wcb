package wcb

import grails.artefact.Artefact
import grails.converters.JSON
import groovy.json.JsonSlurper
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib

@Artefact("TagLibrary")
class WeiXinTagLib extends FormTagLib{
    def moduleVersionService
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def wxList = {attrs,body->
        log.info "-----wxList"
        def list = attrs.list
        log.info "数据格式：${list.class.getName()}"
        JSON.
        log.info list
//        def obj = new JsonSlurper().parseText(list)
        log.info list?.success
//
        out << body()
    }
    /**
     * @attr viewId REQUIRED
     */
    Closure wxForm = {attrs,body->
        log.info "attrs:${attrs}"
        def employee = session.employee
        log.info employee
        resolveAttributes(attrs)
//        def json = moduleVersionService.formView(employee,attrs.viewId)
//        log.info json.items
//        json.items?.each {
//            out << body(it)
//            out << render(template: "/common/formFieldTemplate", model: [field: it])
            out << "<div "
            outputAttributes(attrs, out, true)
            out << "></div>"
//        }
    }
}
