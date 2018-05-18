package pl.dalgim.gitty.github.query;

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
public class RepositorySearchParams {

  @NonNull
  private final String owner;
  @NonNull
  private final String repositoryName;

}
