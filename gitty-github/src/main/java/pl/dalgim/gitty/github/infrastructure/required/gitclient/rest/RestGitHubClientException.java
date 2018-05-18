package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest;

/**
 * Throwing while GitHub API unknown exception occurred
 * @author Mateusz Dalgiewicz
 */
public class RestGitHubClientException extends RuntimeException {

  public RestGitHubClientException(String message) {
    super(message);
  }

  public RestGitHubClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
