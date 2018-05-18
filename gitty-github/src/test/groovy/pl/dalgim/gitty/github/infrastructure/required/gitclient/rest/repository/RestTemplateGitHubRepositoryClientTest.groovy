package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository

import pl.dalgim.gitty.common.date.DateTimeProvider
import pl.dalgim.gitty.common.rest.RestTemplateWrapper
import spock.lang.Specification

/**
 * @author Mateusz Dalgiewicz
 */
class RestTemplateGitHubRepositoryClientTest extends Specification {

    RestTemplateGitHubRepositoryClient client
    RestTemplateWrapper restTemplateWrapper
    String url
    String getRepositoryPath

    void setup() {
        url = "https://github.com"
        getRepositoryPath = "/repositories"
        restTemplateWrapper = Mock(RestTemplateWrapper)
        client = new RestTemplateGitHubRepositoryClient(
                url,
                getRepositoryPath,
                restTemplateWrapper
        )
    }

    def "should return RepositoryInfoResponse when find repository for given owner and name"() {
        given: "search params for repository"
            def request = new RepositoryInfoRequest(
                    "John",
                    "java-test"
            )
        and:
            def expectedResult = new RepositoryInfoResponse(
                    fullName: "Repozytium",
                    description: null,
                    cloneUrl: "https://git.repozytorum.git",
                    stars: 10,
                    createdAt: DateTimeProvider.currentLocalDateTime()
            )
            restTemplateWrapper.getForEntity(_ as URI, RepositoryInfoResponse.class) >> expectedResult
        when: "get data from remote api"
            def response = client.getRepository(request)
        then: "verify result"
            response != null
            response.getCloneUrl() == expectedResult.getCloneUrl()
            response.getCreatedAt() == expectedResult.getCreatedAt()
            response.getDescription() == expectedResult.getDescription()
            response.getFullName() == expectedResult.getFullName()
            response.getStars() == expectedResult.getStars()
    }

    def "should throw IllegalStateException when url to github api not provided"() {
        given: "config with null baseUrl"
            client = new RestTemplateGitHubRepositoryClient(
                    null,
                    getRepositoryPath,
                    restTemplateWrapper
            )
        when: "get repository for given params"
            client.getRepository(new RepositoryInfoRequest(
                    "John",
                    "java-test"
            ))
        then: "throw IllegalStateException"
            thrown(IllegalStateException)
    }

    def "should throw IllegalStateException when get repository path not provided"() {
        given: "config with null repository path"
            client = new RestTemplateGitHubRepositoryClient(
                    url,
                    null,
                    restTemplateWrapper
            )
        when: "get repository for given params"
            client.getRepository(new RepositoryInfoRequest(
                    "John",
                    "java-test"
            ))
        then: "throw IllegalStateException"
            thrown(IllegalStateException)
    }
}
