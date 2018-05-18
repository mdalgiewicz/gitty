package pl.dalgim.gitty.api.logic.config

import org.apache.logging.log4j.ThreadContext
import pl.dalgim.gitty.api.logic.config.AppParams
import spock.lang.Specification
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Mateusz Dalgiewicz
 */
class TraceIdRequestInterceptorTest extends Specification {

    TraceIdRequestInterceptor interceptor

    def "should add UUID attribute to ThreadContext and return true"() {
        given:
            interceptor = new TraceIdRequestInterceptor()
        HttpServletResponse response = Stub(HttpServletResponse)
        when:
            def status = interceptor.preHandle(_ as HttpServletRequest, response, _ as Object)
        then:
            status
            ThreadContext.get(AppParams.UUID_CONTEXT_ATTRIBUTE_NAME) != null
    }
}
