package pl.dalgim.gitty.api.logic;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.dalgim.gitty.api.ErrorDto;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mateusz Dalgiewicz
 */
@RequiredArgsConstructor
public class ErrorDtoFactory {

  private final MessageSource messageSource;

  public ErrorDto createError(ConstraintViolation<?> violation, ErrorCode errorCode) {
    Preconditions.checkNotNull(violation, "ConstraintViolation cannot be null.");
    return new ErrorDto(
            errorCode.getHttpStatusRawValue(),
            errorCode.getCode(),
            violation.getMessage(),
            violation.getPropertyPath().toString()
    );
  }

  public List<ErrorDto> createError(Set<ConstraintViolation<?>> violations, ErrorCode errorCode) {
    Preconditions.checkNotNull(violations, "ConstraintViolations cannot be null.");
    return violations.stream()
            .map((ConstraintViolation cv) -> createError(cv, errorCode))
            .collect(Collectors.toList());
  }

  public ErrorDto createError(ErrorCode errorCode) {
    Preconditions.checkNotNull(errorCode, "ErrorCode cannot be null.");
    return new ErrorDto(
            errorCode.getHttpStatusRawValue(),
            errorCode.getCode(),
            getMessageForCurrentLocale(errorCode.getCode())
    );
  }

  public List<ErrorDto> createErrorList(ErrorCode ... errorCodes) {
    Preconditions.checkNotNull(errorCodes, "ErrorCodes cannot be null.");
    return Stream.of(errorCodes)
            .map(this::createError)
            .collect(Collectors.toList());
  }

  private String getMessageForCurrentLocale(String errorCode) {
    return messageSource.getMessage(errorCode, new Object[]{}, LocaleContextHolder.getLocale());
  }

}
