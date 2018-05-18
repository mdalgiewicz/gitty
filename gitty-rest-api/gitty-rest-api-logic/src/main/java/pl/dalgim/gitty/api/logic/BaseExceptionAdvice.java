package pl.dalgim.gitty.api.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;
import pl.dalgim.gitty.api.ErrorDto;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

/**
 * System exception handler.
 *
 * @author Mateusz Dalgiewicz
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@Order
class BaseExceptionAdvice {

  private final ErrorDtoFactory errorDtoFactory;

  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  List<ErrorDto> exception(Exception ex) {
    log.error("Unexpected exception: {}", ex);
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  @ResponseStatus(NOT_ACCEPTABLE)
  List<ErrorDto> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
    log.error("Operation not allowed: {}", ex);
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(HttpMessageConversionException.class)
  @ResponseBody
  @ResponseStatus(BAD_REQUEST)
  List<ErrorDto> httpMessageConversionException(HttpMessageConversionException ex) {
    log.error("Message conversion problem: {}", ex.getMessage());
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  List<ErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    log.error("Validation problem: {}", ex);
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ResourceAccessException.class)
  @ResponseBody
  @ResponseStatus(SERVICE_UNAVAILABLE)
  List<ErrorDto> resourceAccessException(ResourceAccessException ex) {
    log.error("Connection problem: ", ex);
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseBody
  @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
  public List<ErrorDto> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
    log.error("Not allowed MediaType: ", ex);
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  @ResponseBody
  @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
  public List<ErrorDto> httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
    log.error("Not allowed MediaType: ", ex);
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  @ResponseStatus(BAD_REQUEST)
  List<ErrorDto> constraintViolationException(ConstraintViolationException ex) {
    log.error("Validation problem: {}", ex.getMessage());
    return errorDtoFactory.createError(ex.getConstraintViolations(), ErrorCode.DefaultErrorCode.BAD_REQUEST);
  }
}
