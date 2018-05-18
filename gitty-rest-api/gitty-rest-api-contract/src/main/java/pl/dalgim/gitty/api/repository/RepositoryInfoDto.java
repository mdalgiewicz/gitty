package pl.dalgim.gitty.api.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;
import pl.dalgim.gitty.common.date.LocalDateTimeDeserializer;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Class represents GitHub Repository data.
 * Class contains support for HIPERMEDIA {@link ResourceSupport}
 *
 * @author Mateusz Dalgiewicz
 */
@ToString(doNotUseGetters = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryInfoDto extends ResourceSupport {

  @NotNull
  @JsonProperty(required = true)
  private String fullName;
  private String description;
  @NotNull
  @JsonProperty(required = true)
  private String cloneUrl;
  @Min(value = 0)
  @JsonProperty(required = true)
  private int stars;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

}
