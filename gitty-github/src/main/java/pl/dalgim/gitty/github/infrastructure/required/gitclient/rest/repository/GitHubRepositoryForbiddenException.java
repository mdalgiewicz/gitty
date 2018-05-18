package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.RestGitHubClientException;

/**
 * Throwing while repository access is private
 * @author Mateusz Dalgiewicz
 */
public class GitHubRepositoryForbiddenException extends RestGitHubClientException {

  public GitHubRepositoryForbiddenException(String message) {
    super(message);
  }

}
