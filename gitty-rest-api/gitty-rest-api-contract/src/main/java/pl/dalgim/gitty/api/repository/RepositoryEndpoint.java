package pl.dalgim.gitty.api.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dalgim.gitty.api.ApiParams;
import pl.dalgim.gitty.api.MethodNotImplementedException;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * This interface represents api for GitHub Repositories.
 * {@link RepositoryInfoDto}
 *
 * @author Mateusz Dalgiewicz
 */
@RestController
@RequestMapping(
    value = ApiParams.VERSION + ApiParams.REPOSITORIES_ENDPOINT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public interface RepositoryEndpoint {

  String GET_BY_OWNER_REPOS = "/{owner}/{repository-name}";
  String REPOS_NAME_PARAM = "repository-name";

  String OWNER_REGEX = "^[a-zA-Z0-9-]*$";
  short OWNER_LENGTH_MIN = 1;
  short OWNER_LENGTH_MAX = 39;
  short REPOSITORY_NAME_LENGTH_MIN = 1;
  short REPOSITORY_NAME_LENGTH_MAX = 100;

  /**
   * Function returns repository data for given owner and repository name.
   *
   * <p><Returns {@code org.springframework.http.{@link org.springframework.http.HttpStatus#NOT_FOUND}}
   * for not existing repository</p>
   * <p><Returns {@code org.springframework.http.{@link org.springframework.http.HttpStatus#FORBIDDEN}}
   * when repository is private</p>
   *
   * @param owner Username of GitHub service
   * @param repositoryName Repository name
   * @return Return repository information {@link RepositoryInfoDto}
   */
  @GetMapping(value = GET_BY_OWNER_REPOS)
  default RepositoryInfoDto getByOwnerAndRepositoryName(@Size(min = OWNER_LENGTH_MIN, max = OWNER_LENGTH_MAX) @Pattern(regexp = OWNER_REGEX)
                                                        @PathVariable String owner,
                                                        @Size(min = REPOSITORY_NAME_LENGTH_MIN, max = REPOSITORY_NAME_LENGTH_MAX)
                                                        @PathVariable(name = REPOS_NAME_PARAM) String repositoryName) {
    throw new MethodNotImplementedException("Method not implemented");
  }
}
