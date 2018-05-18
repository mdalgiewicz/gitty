package pl.dalgim.gitty.api.logic.config;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Interceptor for generating random request identifier, which can be uses to bug fixing.

 * @author Mateusz Dalgiewicz
 */
class TraceIdRequestInterceptor extends HandlerInterceptorAdapter {

  /**
   * Function generate random identifier based on UUID and put it
   * into ThreadContext which uses this identifier for logging.
   *
   * Funtion also set Trace-Id header.
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    final String uuid = UUID.randomUUID().toString();
    ThreadContext.put(AppParams.UUID_CONTEXT_ATTRIBUTE_NAME, uuid);
    response.setHeader(AppParams.TRACE_ID_HEADER_NAME, uuid);
    return true;
  }

}
