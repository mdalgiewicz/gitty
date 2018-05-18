package pl.dalgim.gitty.api.logic

import org.springframework.core.MethodParameter
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.client.ResourceAccessException
import pl.dalgim.gitty.api.ErrorDto
import pl.dalgim.gitty.api.logic.ErrorCode
import pl.dalgim.gitty.api.logic.ErrorDtoFactory
import pl.dalgim.gitty.common.date.DateTimeProvider
import spock.lang.Specification
import javax.validation.ConstraintViolationException
import java.time.LocalDateTime

/**
 * @author Mateusz Dalgiewicz
 */
class BaseExceptionAdviceTest extends Specification {

    ErrorDtoFactory errorDtoFactory
    BaseExceptionAdvice advice
    LocalDateTime stubbedDateTime

    void setup() {
        stubbedDateTime = LocalDateTime.of(2018, 1, 1, 1, 1, 1)
        DateTimeProvider.useFixedClock(stubbedDateTime)
        errorDtoFactory = Mock(ErrorDtoFactory)
        advice = new BaseExceptionAdvice(
                errorDtoFactory
        )
    }

    def "should map Exception to ErrorDto"() {
        given:
            def ex = new NullPointerException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR) >> expectedErrorDto
        when:
            def errorDto = advice.exception(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map HttpMessageConversionException to ErrorDto"() {
        given:
            def ex = new HttpMessageConversionException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.BAD_REQUEST.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.BAD_REQUEST.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.BAD_REQUEST) >> expectedErrorDto
        when:
            def errorDto = advice.httpMessageConversionException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map MethodArgumentNotValidException to ErrorDto"() {
        given:
            def ex = new MethodArgumentNotValidException(Mock(MethodParameter), Mock(BindingResult))
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.UNPROCESSABLE_ENTITY.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.UNPROCESSABLE_ENTITY.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.UNPROCESSABLE_ENTITY) >> expectedErrorDto
        when:
            def errorDto = advice.methodArgumentNotValidException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map ResourceAccessException to ErrorDto"() {
        given:
            def ex = new ResourceAccessException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE) >> expectedErrorDto
        when:
            def errorDto = advice.resourceAccessException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map HttpMediaTypeNotSupportedException to ErrorDto"() {
        given:
            def ex = new HttpMediaTypeNotSupportedException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE) >> expectedErrorDto
        when:
            def errorDto = advice.httpMediaTypeNotSupportedException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map HttpMediaTypeNotAcceptableException to ErrorDto"() {
        given:
            def ex = new HttpMediaTypeNotAcceptableException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE) >> expectedErrorDto
        when:
            def errorDto = advice.httpMediaTypeNotAcceptableException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map ConstraintViolationException to ErrorDto"() {
        given:
            def ex = new ConstraintViolationException("msg", _ as Set)
          def error = new ErrorDto(
                  ErrorCode.DefaultErrorCode.BAD_REQUEST.getHttpStatusRawValue(),
                  ErrorCode.DefaultErrorCode.BAD_REQUEST.getCode(),
                  "msg"
          )
          def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createError(_ as Set, ErrorCode.DefaultErrorCode.BAD_REQUEST) >> expectedErrorDto
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.BAD_REQUEST) >> expectedErrorDto
        when:
            def errorDto = advice.constraintViolationException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map HttpRequestMethodNotSupportedException to ErrorDto"() {
        given:
            def ex = new HttpRequestMethodNotSupportedException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.NOT_ACCEPTABLE.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.NOT_ACCEPTABLE.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.NOT_ACCEPTABLE) >> expectedErrorDto
        when:
            def errorDto = advice.httpRequestMethodNotSupportedException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

}
