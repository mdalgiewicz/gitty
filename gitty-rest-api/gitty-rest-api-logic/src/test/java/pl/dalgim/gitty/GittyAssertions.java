package pl.dalgim.gitty;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author <a href="mateusz.dalgiewicz@coi.gov.pl">Mateusz Dalgiewicz</a>
 * @since 18.04.18
 */
public class GittyAssertions {


    public static void assertThatResponseEntityIsCorrect(ResponseEntity entity, HttpStatus httpStatus) {
        assertThat(entity).isNotNull();
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(httpStatus);
        assertThat(entity.getHeaders()).isNotNull();
    }
}
