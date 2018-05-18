package pl.dalgim.gitty.api.logic.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import pl.dalgim.gitty.api.logic.ErrorCode;

/**
 * Enumeration of Repository access possible errors.
 *
 * <p>Error codes should have defined user friendly messages in messages.properties</p>
 * @author Mateusz Dalgiewicz
 */
@Getter
@RequiredArgsConstructor
enum RepositoryErrorCodes implements ErrorCode {
  REPOSITORY_NOT_FOUND("repository_not_found", HttpStatus.NOT_FOUND),
  REPOSITORY_FORBIDDEN("repository_forbidden", HttpStatus.FORBIDDEN);

  private final String code;
  private final HttpStatus httpStatus;

}
