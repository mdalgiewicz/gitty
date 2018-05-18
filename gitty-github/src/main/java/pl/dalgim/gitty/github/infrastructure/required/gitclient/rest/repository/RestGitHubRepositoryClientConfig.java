package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.GitHubClientProperties;
import pl.dalgim.gitty.common.rest.RestTemplateWrapper;

/**
 * @author Mateusz Dalgiewicz
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GitHubClientProperties.class)
class RestGitHubRepositoryClientConfig {

  private final GitHubClientProperties gitHubClientProperties;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder
        .setConnectTimeout(gitHubClientProperties.getConnectionTimeout())
        .setReadTimeout(gitHubClientProperties.getReadTimeout())
        .build();
  }

  @Bean
  @Autowired
  public RestTemplateWrapper restTemplateWrapper(RestTemplate restTemplate) {
    restTemplate.setErrorHandler(new RestGitHubRepositoryExceptionHandler());
    return new RestTemplateWrapper(restTemplate);
  }

  @Bean
  @Autowired
  public RestGitHubRepositoryQueryServiceClientAdapter restRepositoryGitHubClientAdapter(RestTemplateWrapper restTemplateWrapper) {
    return new RestGitHubRepositoryQueryServiceClientAdapter(
        new RestTemplateGitHubRepositoryClient(
            gitHubClientProperties.getBaseUrl(),
            gitHubClientProperties.getRepositoryPath(),
            restTemplateWrapper
        )
    );
  }

}
