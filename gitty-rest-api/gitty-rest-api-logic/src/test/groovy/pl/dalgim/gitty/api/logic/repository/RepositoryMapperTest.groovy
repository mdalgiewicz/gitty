package pl.dalgim.gitty.api.logic.repository

import pl.dalgim.gitty.common.date.DateTimeProvider
import pl.dalgim.gitty.github.query.RepositoryInfo
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

    def "should map RepositoryInfo to RepositoryInfDto"() {
        given: "instance of RepositoryInfo"
            def repoInfo = new RepositoryInfo (
                    "Repozytium",
                    null,
                    "https://git.repozytorum.git",
                    10,
                    DateTimeProvider.currentLocalDateTime()
            )
        when: "map object to RepositoryInfoDto"
            def dto = RepositoryMapper.map(repoInfo)
        then: "verify instance of RepositoryInfoDto"
            dto != null
            dto.getCloneUrl() == repoInfo.getCloneUrl()
            dto.getCreatedAt() == repoInfo.getCreatedAt()
            dto.getDescription() == repoInfo.getDescription()
            dto.getFullName() == repoInfo.getFullName()
            dto.getStars() == repoInfo.getStars()
    }

    def "should return null when map null instance of RepositoryInfoDto"() {
        expect: "null result"
            RepositoryMapper.map(null) == null
    }

}
