package pl.dalgim.gitty.common.date

import pl.dalgim.gitty.common.date.DateTimeProvider
import spock.lang.Specification
import java.time.LocalDateTime

/**
 * @author Mateusz Dalgiewicz
 */
class DateTimeProviderTest extends Specification {

    def "should return LocalDate from stubbed handler"() {
        given:
            def stubbedDateTime = LocalDateTime.of(2017, 1, 1, 10, 0, 0)
        and:
            DateTimeProvider.useFixedClock(stubbedDateTime)
        expect:
            DateTimeProvider.currentLocalDateTime() == stubbedDateTime
    }

    def "should return LocalDateTime from stubbed handler"() {
        given:
            def stubbedDateTime = LocalDateTime.of(2017, 1, 1, 10, 0, 0)
        and:
            DateTimeProvider.useFixedClock(stubbedDateTime)
        expect:
            DateTimeProvider.currentLocalDate() == stubbedDateTime.toLocalDate()
    }

    def "should return LocalDateTime from default handler"() {
        expect:
            DateTimeProvider.currentLocalDateTime() != null
    }

    def "should return LocalDate from default handler"() {
        expect:
            DateTimeProvider.currentLocalDate() != null
    }
}
