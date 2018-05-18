package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Mateusz Dalgiewicz
 */
@Getter
@ToString(doNotUseGetters = true)
@RequiredArgsConstructor
class RepositoryInfoRequest {

  @NonNull
  private final String owner;
  @NonNull
  private final String repositoryName;

}
