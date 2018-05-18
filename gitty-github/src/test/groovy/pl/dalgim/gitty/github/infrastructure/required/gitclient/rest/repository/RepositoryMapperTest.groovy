package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository

import pl.dalgim.gitty.common.date.DateTimeProvider

import pl.dalgim.gitty.github.query.RepositorySearchParams
import spock.lang.Specification
import java.time.LocalDateTime

/**
 * @author Mateusz Dalgiewicz
 */
class RepositoryMapperTest extends Specification {

    void setup() {
        def stubbedDateTime = LocalDateTime.of(2017, 1, 1, 10, 0, 0)
        DateTimeProvider.useFixedClock(stubbedDateTime)
    }

    def "should map RepositoryInfoResponse to RepositoryInfo"() {
        given: "instance od RepositoryInfoResponse"
            def repoInfoResponse = new RepositoryInfoResponse(
                    fullName: "Repozytium",
                    description: null,
                    cloneUrl: "https://git.repozytorum.git",
                    stars: 10,
                    createdAt: DateTimeProvider.currentLocalDateTime()
            )
        when: "map object to RepositoryInfo"
            def repositoryInfo = RepositoryMapper.map(repoInfoResponse)
        then: "verify instance of RepositoryInfo"
            repositoryInfo != null
            repositoryInfo.getCloneUrl() == repoInfoResponse.getCloneUrl()
            repositoryInfo.getCreatedAt() == repoInfoResponse.getCreatedAt()
            repositoryInfo.getDescription() == repoInfoResponse.getDescription()
            repositoryInfo.getFullName() == repoInfoResponse.getFullName()
            repositoryInfo.getStars() == repoInfoResponse.getStars()
    }

    def "should return null when map null instance of RepositoryInfoResponse"() {
        expect: "null result"
            RepositoryMapper.map(null) == null
    }

    def "should map RepositorySearchParams to RepositoryInfoRequest"() {
        given: "instance od RepositorySearchParams"
            def params = new RepositorySearchParams(
                    "John",
                    "java-app"
            )
        when: "map object to RepositoryInfoRequest"
            def request = RepositoryMapper.mapToRequest(params)
        then: "verify instance of RepositoryInfoRequest"
            request != null
            request.getOwner() == params.getOwner()
            request.getRepositoryName() == params.getRepositoryName()
    }

    def "should return null when map null instance of RepositorySearchParams"() {
        expect: "null result"
            RepositoryMapper.mapToRequest(null) == null
    }

}
