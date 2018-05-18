package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import pl.dalgim.gitty.github.query.RepositoryInfo;
import pl.dalgim.gitty.github.query.RepositoryQueryService;
import pl.dalgim.gitty.github.query.RepositorySearchParams;

/**
 * Adapter for {@link RepositoryQueryService} interface.
 * This is plug-in into to domain logic based or GitHub REST Api
 *
 * @author Mateusz Dalgiewicz
 */
@RequiredArgsConstructor
class RestGitHubRepositoryQueryServiceClientAdapter implements RepositoryQueryService {

  private final RestGitHubRepositoryClient gitRepositoryClient;

  @Override
  public RepositoryInfo find(RepositorySearchParams params) {
    Preconditions.checkNotNull(params, "RepositorySearchParams arg cannot be null.");

    return RepositoryMapper.map(
        gitRepositoryClient.getRepository(RepositoryMapper.mapToRequest(params))
    );
  }

}
