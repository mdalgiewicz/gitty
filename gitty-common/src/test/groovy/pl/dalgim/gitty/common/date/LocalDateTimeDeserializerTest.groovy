package pl.dalgim.gitty.common.date

import com.fasterxml.jackson.core.JsonParser
import pl.dalgim.gitty.common.date.LocalDateTimeDeserializer
import spock.lang.Specification
import java.time.LocalDateTime

/**
 * @author Mateusz Dalgiewicz
 */
class LocalDateTimeDeserializerTest extends Specification {

    LocalDateTimeDeserializer deserializer = new LocalDateTimeDeserializer()

    def "should map date with Z at the end"() {
        given:
            JsonParser parser = Mock(JsonParser)
        and:
            parser.getText() >> "2017-04-02T22:00:00.000Z"
        when:
            def result = deserializer.deserialize(parser, null)
        then:
            result == LocalDateTime.of(2017, 04, 03, 0, 0, 0)
    }

    def "should map date without Z at the end"() {
        given:
            JsonParser parser = Mock(JsonParser)
        and:
            parser.getText() >> "2017-04-02T22:00:00"
        when:
            def result = deserializer.deserialize(parser, null)
        then:
            result == LocalDateTime.of(2017, 04, 02, 22, 0, 0)
    }

    def "should return null when null value given"() {
        given:
            JsonParser parser = Mock(JsonParser)
        and:
            parser.getText() >> null
        when:
            def result = deserializer.deserialize(parser, null)
        then:
            result == null
    }

}
