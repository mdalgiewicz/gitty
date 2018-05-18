package pl.dalgim.gitty.api.logic.repository;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dalgim.gitty.api.logic.ErrorDtoFactory;

/**
 * @author Mateusz Dalgiewicz
 */
@Configuration
class RepositoryConfig {

  @Bean
  public ErrorDtoFactory errorDtoFactory(MessageSource messageSource) {
    return new ErrorDtoFactory(messageSource);
  }

}
