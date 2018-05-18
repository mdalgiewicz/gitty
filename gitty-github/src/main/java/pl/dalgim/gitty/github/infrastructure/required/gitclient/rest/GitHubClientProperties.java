package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Configuration class for GitHub Api connection.
 *
 * @author Mateusz Dalgiewicz
 */
@Getter
@ConfigurationProperties(prefix = GitHubClientProperties.CONFIG_PREFIX)
public class GitHubClientProperties {

  /**
   * Path to properties file
   */
  final static String CONFIG_PREFIX = "gitty.clients.github";

  private int connectionTimeout;
  private int readTimeout;
  private String version;

  /**
   * Absoulte path to GitHub Api
   */
  private String baseUrl;

  /**
   * Relative path to Repository resource
   */
  private String repositoryPath;

  public void setConnectionTimeout(int connectionTimeout) {
    if (connectionTimeout <= 0) {
      throw new IllegalArgumentException("Connection Timeout not specified.");
    }
    this.connectionTimeout = connectionTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    if (readTimeout <= 0) {
      throw new IllegalArgumentException("Read Timeout not specified.");
    }
    this.readTimeout = readTimeout;
  }

  public void setVersion(String version) {
    this.version = isNullOrEmpty(version)
        ? "v1"
        : version;
  }

  public void setBaseUrl(String baseUrl) {
    if (isNullOrEmpty(baseUrl)) {
      throw new IllegalArgumentException("GitHub Client url not specified.");
    }
    this.baseUrl = baseUrl;
  }

  public void setRepositoryPath(String repositoryPath) {
    if (isNullOrEmpty(repositoryPath)) {
      throw new IllegalArgumentException("GitHub Repository path not specified.");
    }
    this.repositoryPath = repositoryPath;
  }
}
