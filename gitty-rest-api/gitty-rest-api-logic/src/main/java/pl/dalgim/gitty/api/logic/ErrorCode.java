package pl.dalgim.gitty.api.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import pl.dalgim.gitty.api.ApiParams;

/**
 * This interface is use to create dedicated error codes for specific REST resource.
 * @see pl.dalgim.gitty.api.logic.repository.RepositoryErrorCodes
 *
 * @author Mateusz Dalgiewicz
 */
public interface ErrorCode {

  /**
   * @return Return String representation of unique identifier of error.
   * This code is using for mapping code into user friendly message.
   */
  String getCode();

  /**
   * @return Return a http status {@link org.springframework.http.HttpStatus} which will be returning into client.
   */
  HttpStatus getHttpStatus();

  /**
   * @return Return http status in raw value.
   */
  default int getHttpStatusRawValue() {
    return getHttpStatus().value();
  }

  /**
   * Enumeration of default system error codes.
   *
   * <p>Error codes should have defined user friendly messages in messages.properties</p>
   */
  @RequiredArgsConstructor
  @Getter
  enum DefaultErrorCode implements ErrorCode {

    /**
     * {@code unknown - Internal server error}
     */
    INTERNAL_SERVER_ERROR("unknown", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * {@code bad_request - Bad request received from client}
     */
    BAD_REQUEST("bad_request", HttpStatus.BAD_REQUEST),

    /**
     * {@code not_found - Requested resource not found}
     */
    NOT_FOUND("not_found", HttpStatus.NOT_FOUND),

    /**
     * {@code unsupported_media_type - Requested MediaType is not supported.}
     * {@link ApiParams#SUPPORTED_MEDIA_TYPES}
     */
    UNSUPPORTED_MEDIA_TYPE("unsupported_media_type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    /**
     * {@code unprocessable_entity - Validation problem}
     */
    UNPROCESSABLE_ENTITY("unprocessable_entity", HttpStatus.UNPROCESSABLE_ENTITY),

    /**
     * {@code service_unavailable - Server not unavailable}
     */
    SERVICE_UNAVAILABLE("service_unavailable", HttpStatus.SERVICE_UNAVAILABLE),

    /**
     * {@code not_acceptable - Requested Http Method not supported by given resource}
     */
    NOT_ACCEPTABLE("not_acceptable", HttpStatus.NOT_ACCEPTABLE),
    ;

    private final String code;
    private final HttpStatus httpStatus;

  }
}
