package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientException
import spock.lang.Specification

/**
 * @author Mateusz Dalgiewicz
 */
class RestGitHubRepositoryExceptionHandlerTest extends Specification {

    RestGitHubRepositoryExceptionHandler exceptionHandler

    void setup() {
        exceptionHandler = new RestGitHubRepositoryExceptionHandler()
    }

    def "should throw GitHubRepositoryNotFoundException when Http Status Code is NOT_FOUND"() {
        given:
            def response = Mock(ClientHttpResponse)
        and:
            response.getStatusCode() >> HttpStatus.NOT_FOUND
        when: "repository for given params not found"
            exceptionHandler.handleError(response)
        then: "throw GitHubRepositoryNotFoundException exception"
            thrown(GitHubRepositoryNotFoundException)
    }

    def "should catch HttpClientErrorException and throw RestGitHubClientException"() {
        given:
            def response = Mock(ClientHttpResponse)
        and:
            response.getStatusCode() >> HttpStatus.CONFLICT
        when:
            exceptionHandler.handleError(response)
        then: "throw GitHubRepositoryNotFoundException exception"
            thrown(RestGitHubClientException)

    }

    def "should throw GitHubRepositoryForbiddenException when no permission to repository"() {
        given:
            def response = Mock(ClientHttpResponse)
        and:
            response.getStatusCode() >> HttpStatus.FORBIDDEN
        when: "no permission to requested repository"
            exceptionHandler.handleError(response)
        then: "throw GitHubRepositoryForbiddenException exception"
            thrown(GitHubRepositoryForbiddenException)
    }
}
