package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository

import pl.dalgim.gitty.common.date.DateTimeProvider
import pl.dalgim.gitty.github.query.RepositorySearchParams
import spock.lang.Specification
import java.time.LocalDateTime

/**
 * @author Mateusz Dalgiewicz
 */
class RestGitHubRepositoryClientAdapterSpec extends Specification {

    RestGitHubRepositoryQueryServiceClientAdapter adapter
    RestGitHubRepositoryClient repositoryClient

    void setup() {
        def stubbedDateTime = LocalDateTime.of(2017, 1, 1, 10, 0, 0)
        DateTimeProvider.useFixedClock(stubbedDateTime)

        repositoryClient = Mock(RestGitHubRepositoryClient)
        adapter = new RestGitHubRepositoryQueryServiceClientAdapter(
                repositoryClient
        )
    }

    def "should return RepositoryInfo when find any for given params"() {
        given: "repository and owner params"
            def params = new RepositorySearchParams(
                    "John",
                    "java-app"
            )
        and:
            def expectedResult = new RepositoryInfoResponse(
                    fullName: "Repozytium",
                    description: null,
                    cloneUrl: "https://git.repozytorum.git",
                    stars: 10,
                    createdAt: DateTimeProvider.currentLocalDateTime()
            )
            repositoryClient.getRepository(_ as RepositoryInfoRequest) >> expectedResult
        when: "get data from GitHub Rest Client"
            def repositoryInfo = adapter.find(params)
        then: "verify results"
            repositoryInfo != null
            repositoryInfo.getCloneUrl() == expectedResult.getCloneUrl()
            repositoryInfo.getCreatedAt() == expectedResult.getCreatedAt()
            repositoryInfo.getDescription() == expectedResult.getDescription()
            repositoryInfo.getFullName() == expectedResult.getFullName()
            repositoryInfo.getStars() == expectedResult.getStars()
    }
}
