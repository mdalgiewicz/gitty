package pl.dalgim.gitty.api.logic.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Mateusz Dalgiewicz
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppParams {

  /**
   * Unique request identifier attribute name
   */
  public static final String UUID_CONTEXT_ATTRIBUTE_NAME = "UUID";

  /**
   * Response header Trace-Id which is equal to {@link AppParams#UUID_CONTEXT_ATTRIBUTE_NAME} value.
   */
  public static final String TRACE_ID_HEADER_NAME = "Trace-Id";

}
