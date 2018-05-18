package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientException;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientTimeoutException;

import java.io.IOException;

/**
 * GitHub Api exception handler.
 *
 * @author Mateusz Dalgiewicz
 */
@Slf4j
class RestGitHubRepositoryExceptionHandler extends DefaultResponseErrorHandler {

  /**
   * Function handler GitHub API exceptions and map into domain exceptio
   * @param response {@link ClientHttpResponse}
   * @throws IOException While I/O error occurred.
   */
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    log.debug("Error while trying fetch data from GitHub: Message={}, Code={}",
        response.getStatusText(), response.getStatusCode());

    switch (response.getStatusCode()) {
      case NOT_FOUND: {
        throw new GitHubRepositoryNotFoundException("Requested GitHub repository not found.");
      }
      case FORBIDDEN: {
        throw new GitHubRepositoryForbiddenException("No permission to requested repository.");
      }
      case REQUEST_TIMEOUT: {
        throw new RestGitHubClientTimeoutException("GitHub api connection timout.");
      }
      default: {
        try {
          super.handleError(response);
        } catch (Exception ex) {
          throw new RestGitHubClientException("Exception occurred when trying connect to GitHub API.", ex);
        }
      }
    }
  }
}
