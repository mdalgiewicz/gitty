package pl.dalgim.gitty.api.logic.repository

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import pl.dalgim.gitty.api.ErrorDto
import pl.dalgim.gitty.api.logic.ErrorCode
import pl.dalgim.gitty.api.logic.ErrorDtoFactory
import pl.dalgim.gitty.common.date.DateTimeProvider
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientException
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientTimeoutException
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository.GitHubRepositoryForbiddenException
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository.GitHubRepositoryNotFoundException
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author Mateusz Dalgiewicz
 */
class RestRepositoryExceptionAdviceTest extends Specification {

    RepositoryExceptionAdvice advice
    ErrorDtoFactory errorDtoFactory
    LocalDateTime stubbedDateTime

    void setup() {
        stubbedDateTime = LocalDateTime.of(2018, 1, 1, 1, 1, 1)
        DateTimeProvider.useFixedClock(stubbedDateTime)
        errorDtoFactory = Mock(ErrorDtoFactory)
        advice = new RepositoryExceptionAdvice(
                errorDtoFactory
        )
    }

    def "should map RestGitHubClientException to ErrorDto if exception is instance of HttpClientErrorException"() {
        given:
            def ex = new RestGitHubClientException("msg", new HttpClientErrorException(HttpStatus.CONFLICT))
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.BAD_REQUEST.getHttpStatusRawValue(),
                    ErrorCode.DefaultErrorCode.BAD_REQUEST.getCode(),
                    "repository not found"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.BAD_REQUEST) >> expectedErrorDto
        when:
            def entity = advice.restGitHubClientException(ex)
        then:
            entity != null
            entity.getBody() != null
            entity.getBody().size() == 1
            entity.getBody().get(0).getMessage() == error.getMessage()
            entity.getBody().get(0).getErrorCode() == error.getErrorCode()
            entity.getBody().get(0).getStatus() == error.getStatus()
            entity.getBody().get(0).getPath() == null
            entity.getBody().get(0).getTimestamp() == stubbedDateTime
            entity.getStatusCode() == HttpStatus.BAD_REQUEST
    }

    def "should map RestGitHubClientException to ErrorDto"() {
        given:
            def ex = new RestGitHubClientException("msg")
            def error = new ErrorDto(
                    ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getHttpStatusRawValue(),
                     ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR) >> expectedErrorDto
        when:
            def entity = advice.restGitHubClientException(ex)
        then:
          entity != null
          entity.getBody() != null
          entity.getBody().size() == 1
          entity.getBody().get(0).getMessage() == error.getMessage()
          entity.getBody().get(0).getErrorCode() == error.getErrorCode()
          entity.getBody().get(0).getStatus() == error.getStatus()
          entity.getBody().get(0).getPath() == null
          entity.getBody().get(0).getTimestamp() == stubbedDateTime
          entity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
    }

    def "should map RestGitHubClientTimeoutException to ErrorDto"() {
        given:
          def ex = new RestGitHubClientTimeoutException("msg")
          def error = new ErrorDto(
                  ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE.getHttpStatusRawValue(),
                  ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE.getCode(),
                  "msg"
          )
          def expectedErrorDto = (List) [error]
        and:
          errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE) >> expectedErrorDto
        when:
          def errorDto = advice.restGitHubClientTimeoutException(ex)
        then:
          errorDto != null
          errorDto.size() == 1
          errorDto.get(0).getMessage() == error.getMessage()
          errorDto.get(0).getErrorCode() == error.getErrorCode()
          errorDto.get(0).getStatus() == error.getStatus()
          errorDto.get(0).getPath() == null
          errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map GitHubRepositoryNotFoundException to ErrorDto"() {
        given:
            def ex = new GitHubRepositoryNotFoundException("msg")
            def error = new ErrorDto(
                    404,
                    "not_found",
                    "repository not found"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(RepositoryErrorCodes.REPOSITORY_NOT_FOUND) >> expectedErrorDto
        when:
            def errorDto = advice.gitHubRepositoryNotFoundException(ex)
        then:
            errorDto != null
            errorDto.size() == 1
            errorDto.get(0).getMessage() == error.getMessage()
            errorDto.get(0).getErrorCode() == error.getErrorCode()
            errorDto.get(0).getStatus() == error.getStatus()
            errorDto.get(0).getPath() == null
            errorDto.get(0).getTimestamp() == stubbedDateTime
    }

    def "should map GitHubRepositoryForbiddenException to ErrorDto"() {
        given:
            def ex = new GitHubRepositoryForbiddenException("msg")
            def error = new ErrorDto(
                    RepositoryErrorCodes.REPOSITORY_FORBIDDEN.getHttpStatusRawValue(),
                    RepositoryErrorCodes.REPOSITORY_FORBIDDEN.getCode(),
                    "msg"
            )
            def expectedErrorDto = (List) [error]
        and:
            errorDtoFactory.createErrorList(RepositoryErrorCodes.REPOSITORY_FORBIDDEN) >> expectedErrorDto
        when:
            def errorDto = advice.gitHubRepositoryForbiddenException(ex)
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
