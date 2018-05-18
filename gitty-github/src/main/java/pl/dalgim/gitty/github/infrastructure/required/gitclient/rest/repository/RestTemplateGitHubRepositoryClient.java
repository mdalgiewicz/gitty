package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import pl.dalgim.gitty.common.rest.RestTemplateWrapper;

import java.net.URI;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Implementation of {@link RestGitHubRepositoryClient}
 *
 * @author Mateusz Dalgiewicz
 */
@Slf4j
@RequiredArgsConstructor
class RestTemplateGitHubRepositoryClient implements RestGitHubRepositoryClient {

  private final String url;
  private final String repositoryPath;
  private final RestTemplateWrapper restTemplate;

  @Override
  public RepositoryInfoResponse getRepository(RepositoryInfoRequest request) {
    Preconditions.checkNotNull(request, "RepositoryInfoRequest args cannot be null.");
    log.info("Fetching repository data from GutHub for: {}", request);

    if (isNullOrEmpty(url) || isNullOrEmpty(repositoryPath)) {
      throw new IllegalStateException("GitHub Client configuration not provided.");
    }
    final URI uri = prepareUri(request);
    if (log.isDebugEnabled()) {
      log.debug("Prepared URI: {}", uri);
    }
    final RepositoryInfoResponse repositoryInfo = restTemplate.getForEntity(
        uri,
        RepositoryInfoResponse.class
    );

    log.info("Fetched repository data from GitHub: {}", repositoryInfo);
    return repositoryInfo;
  }

  private URI prepareUri(RepositoryInfoRequest request) {
    return UriComponentsBuilder
        .fromHttpUrl(url)
        .path(repositoryPath)
        .pathSegment(request.getOwner(), request.getRepositoryName())
        .build()
        .toUri();
  }
}
