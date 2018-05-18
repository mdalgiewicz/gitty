package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.dalgim.gitty.common.date.LocalDateTimeDeserializer;
import java.time.LocalDateTime;

/**
 * @author Mateusz Dalgiewicz
 */
@Getter
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
class RepositoryInfoResponse {

  private static final String FULL_NAME = "full_name";
  private static final String CLONE_URL = "clone_url";
  private static final String STARS = "stargazers_count";
  private static final String CREATED_AT = "created_at";

  @JsonProperty(required = true, value = FULL_NAME)
  private String fullName;
  @JsonProperty
  private String description;
  @JsonProperty(required = true, value = CLONE_URL)
  private String cloneUrl;
  @JsonProperty(required = true, value = STARS)
  private int stars;
  @JsonProperty(required = true, value = CREATED_AT)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;
}
