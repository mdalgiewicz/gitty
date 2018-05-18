package pl.dalgim.gitty.api.logic.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Mateusz Dalgiewicz
 */
@Configuration
@RequiredArgsConstructor
class ApiConfig implements WebMvcConfigurer {

  @Setter
  @ConfigurationProperties(prefix = LoggingProperties.CONFIG_PATH)
  private class LoggingProperties {

    static final String CONFIG_PATH = "gitty.api.logging";

    private boolean clientInfo = false;
    private boolean queryString = false;
    private boolean payload = false;
    private int maxPayloadLength = 1000;
    private boolean headers = false;

  }

  @Bean
  public LoggingProperties loggingProperties() {
    return new LoggingProperties();
  }

  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TraceIdRequestInterceptor());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**");
  }

  @Bean
  @Autowired
  public CommonsRequestLoggingFilter requestLoggingFilter(LoggingProperties properties) {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
    loggingFilter.setIncludeClientInfo(properties.clientInfo);
    loggingFilter.setIncludeQueryString(properties.queryString);
    loggingFilter.setIncludePayload(properties.payload);
    loggingFilter.setMaxPayloadLength(properties.maxPayloadLength);
    loggingFilter.setIncludeHeaders(properties.headers);
    return loggingFilter;
  }
}
