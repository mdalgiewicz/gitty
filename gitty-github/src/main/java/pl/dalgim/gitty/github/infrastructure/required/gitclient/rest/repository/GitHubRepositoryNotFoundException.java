package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientException;

/**
 * Throwing while GitHub repository not found.
 * @author Mateusz Dalgiewicz
 */
public class GitHubRepositoryNotFoundException extends RestGitHubClientException {

  public GitHubRepositoryNotFoundException(String message) {
    super(message);
  }
}
