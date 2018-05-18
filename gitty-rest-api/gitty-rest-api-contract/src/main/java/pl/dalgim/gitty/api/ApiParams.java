package pl.dalgim.gitty.api;

import org.springframework.http.MediaType;
import pl.dalgim.gitty.api.repository.RepositoryEndpoint;

import java.util.HashSet;
import java.util.Set;

/**
 * Application parameters
 * @author Mateusz Dalgiewicz
 */
public class ApiParams {

  /**
   * Current REST API version
   */
  public static final String VERSION = "v1";

  /**
   * Relative path to Repository resource.
   * {@link RepositoryEndpoint}
   */
  public static final String REPOSITORIES_ENDPOINT = "/repositories";

  /**
   * Supported Media Types.
   */
  public static final Set<MediaType> SUPPORTED_MEDIA_TYPES = new HashSet<>();

  static {
    SUPPORTED_MEDIA_TYPES.add(MediaType.APPLICATION_JSON);
  }
}
