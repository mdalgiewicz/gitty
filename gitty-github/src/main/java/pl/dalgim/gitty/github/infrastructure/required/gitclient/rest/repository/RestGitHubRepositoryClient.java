package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

/**
 * This interface is used to getting Repository data from GitHub API
 *
 * @author Mateusz Dalgiewicz
 */
interface RestGitHubRepositoryClient {

  /**
   * Function return data of requested repository.
   * @param request {@link RepositoryInfoRequest} Search parameters.
   * @return {@link RepositoryInfoResponse} Repository data
   */
  RepositoryInfoResponse getRepository(RepositoryInfoRequest request);

}
