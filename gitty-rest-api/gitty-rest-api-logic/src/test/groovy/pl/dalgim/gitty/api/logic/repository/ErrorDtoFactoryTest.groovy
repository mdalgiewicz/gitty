package pl.dalgim.gitty.api.logic.repository

import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import pl.dalgim.gitty.api.logic.ErrorCode
import pl.dalgim.gitty.api.logic.ErrorDtoFactory
import spock.lang.Specification

/**
 * @author Mateusz Dalgiewicz
 */
class ErrorDtoFactoryTest extends Specification {

    MessageSource messageSource
    ErrorDtoFactory factory

    void setup() {
        messageSource = Mock(MessageSource)
        factory = new ErrorDtoFactory(
                messageSource
        )
    }

    def "should create ErrorDto for given code"() {
        given:
            def errorCode = ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR
            def message = "error"
        and:
            messageSource.getMessage(errorCode.getCode(), _ as Object[], _ as Locale) >> message
        when:
            def errorDto = factory.createError(errorCode)
        then:
            errorDto != null
            errorDto.getMessage() == message
            errorDto.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()
            errorDto.getErrorCode() == ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getCode()
    }

    def "should create List<ErrorDto> for given code"() {
        given:
          def errorCode = ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR
          def message = "error"
        and:
          messageSource.getMessage(errorCode.getCode(), _ as Object[], _ as Locale) >> message
        when:
          def errorDto = factory.createErrorList(errorCode)
        then:
          errorDto != null
          errorDto.size() == 1
          errorDto.get(0).getMessage() == message
          errorDto.get(0).getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()
          errorDto.get(0).getErrorCode() == ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getCode()
    }

}
