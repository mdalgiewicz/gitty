package pl.dalgim.gitty.api.logic.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import pl.dalgim.gitty.api.ErrorDto;
import pl.dalgim.gitty.api.logic.ErrorCode;
import pl.dalgim.gitty.api.logic.ErrorDtoFactory;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientException;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientTimeoutException;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository.GitHubRepositoryForbiddenException;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository.GitHubRepositoryNotFoundException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

/**
 * Domain exception handler for Repository access.
 *
 * @author Mateusz Dalgiewicz
 */
@Slf4j
@ControllerAdvice(assignableTypes = RepositoryEndpointImpl.class)
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
class RepositoryExceptionAdvice {

  private final ErrorDtoFactory errorDtoFactory;

  /**
   * GitHub Api connection exception handler
   * @param ex {@link RestGitHubClientException}
   * @return {@link ErrorCode.DefaultErrorCode#BAD_REQUEST}
   */
  @ExceptionHandler(RestGitHubClientException.class)
  ResponseEntity<List<ErrorDto>> restGitHubClientException(RestGitHubClientException ex) {
    log.warn("Exception occurred while trying fetch data from GiyHub {}", ex);
    if (ex.getCause() instanceof HttpClientErrorException) {
      return new ResponseEntity<>(errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.BAD_REQUEST), BAD_REQUEST);
    }
    return new ResponseEntity<>(errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RestGitHubClientTimeoutException.class)
  @ResponseBody
  @ResponseStatus(SERVICE_UNAVAILABLE)
  List<ErrorDto> restGitHubClientTimeoutException(RestGitHubClientTimeoutException ex) {
    return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE);
  }

  /**
   * GitHub Repository not found
   * @param ex {@link GitHubRepositoryNotFoundException}
   * @return {@link RepositoryErrorCodes#REPOSITORY_NOT_FOUND}
   */
  @ExceptionHandler(GitHubRepositoryNotFoundException.class)
  @ResponseBody
  @ResponseStatus(NOT_FOUND)
  List<ErrorDto> gitHubRepositoryNotFoundException(GitHubRepositoryNotFoundException ex) {
    log.warn("Repository exception: {}", ex.getMessage());
    return errorDtoFactory.createErrorList(RepositoryErrorCodes.REPOSITORY_NOT_FOUND);
  }

  /**
   * GitHub Repository access restriction
   * @param ex {@link GitHubRepositoryForbiddenException}
   * @return {@link RepositoryErrorCodes#REPOSITORY_FORBIDDEN}
   */
  @ExceptionHandler(GitHubRepositoryForbiddenException.class)
  @ResponseBody
  @ResponseStatus(FORBIDDEN)
  List<ErrorDto> gitHubRepositoryForbiddenException(GitHubRepositoryForbiddenException ex) {
    log.warn("Repository exception: {}", ex.getMessage());
    return errorDtoFactory.createErrorList(RepositoryErrorCodes.REPOSITORY_FORBIDDEN);
  }

}
