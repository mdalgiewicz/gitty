package pl.dalgim.gitty.api.logic.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.dalgim.gitty.api.ApiParams;
import pl.dalgim.gitty.api.repository.RepositoryEndpoint;
import pl.dalgim.gitty.api.repository.RepositoryInfoDto;
import pl.dalgim.gitty.github.query.RepositoryQueryService;
import pl.dalgim.gitty.github.query.RepositorySearchParams;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.OK;

/**
 * Implementation of {@link RepositoryEndpoint}
 * @author Mateusz Dalgiewicz
 */
@Slf4j
@Validated
@RestController
@RequestMapping(
    value = ApiParams.VERSION + ApiParams.REPOSITORIES_ENDPOINT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
class RepositoryEndpointImpl implements RepositoryEndpoint {

  private final RepositoryQueryService repositoryQueryService;

  @GetMapping(value = GET_BY_OWNER_REPOS)
  @ResponseBody
  @ResponseStatus(OK)
  public RepositoryInfoDto getByOwnerAndRepositoryName(@Size(min = OWNER_LENGTH_MIN, max = OWNER_LENGTH_MAX) @Pattern(regexp = OWNER_REGEX)
                                                       @PathVariable(name = "owner") String owner,
                                                       @Size(min = REPOSITORY_NAME_LENGTH_MIN, max = REPOSITORY_NAME_LENGTH_MAX)
                                                       @PathVariable(name = REPOS_NAME_PARAM) String repositoryName) {
    log.info("Getting Repository info for given Owner={}, Name={}", owner, repositoryName);
    final RepositoryInfoDto repositoryInfoDto = RepositoryMapper.map(
        repositoryQueryService.find(new RepositorySearchParams(owner, repositoryName)
        )
    );
    repositoryInfoDto.add(
        linkTo(methodOn(RepositoryEndpointImpl.class)
            .getByOwnerAndRepositoryName(owner, repositoryName))
            .withSelfRel()
    );
    log.info("Returning RepositoryInfo: {}", repositoryInfoDto);
    return repositoryInfoDto;
  }

}
