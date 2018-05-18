package pl.dalgim.gitty.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.dalgim.gitty.common.date.DateTimeProvider;
import pl.dalgim.gitty.common.date.LocalDateTimeDeserializer;
import java.time.LocalDateTime;

/**
 * Class represents errors returning from system.
 * @author Mateusz Dalgiewicz
 */
@Getter
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDto {

  /**
   * Http status code.
   */
  private int status;

  /**
   * Error code for developers.
   */
  @NonNull
  private String errorCode;

  /**
   * User friendly message.
   */
  @NonNull
  private String message;

  /**
   * Optional field. Field contains full path into argument while validation error occurred.
   */
  private String path;

  /**
   * Error occurred timestamp
   */
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @NonNull
  private LocalDateTime timestamp;

  public ErrorDto(int status, String errorCode, String message) {
    this.status = status;
    this.errorCode = errorCode;
    this.message = message;
    this.timestamp = DateTimeProvider.currentLocalDateTime();
  }

  public ErrorDto(int status, String errorCode, String message, String path) {
    this(status, errorCode, message);
    this.path = path;
  }
}
