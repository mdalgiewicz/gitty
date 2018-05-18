package pl.dalgim.gitty.github.query;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * @author Mateusz Dalgiewicz
 */
@Getter
@ToString(doNotUseGetters = true)
@RequiredArgsConstructor
public class RepositoryInfo {

  @NonNull
  private final String fullName;
  private final String description;
  @NonNull
  private final String cloneUrl;
  private final int stars;
  @NonNull
  private final LocalDateTime createdAt;

}
