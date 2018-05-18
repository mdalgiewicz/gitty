package pl.dalgim.gitty.api.logic.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Mateusz Dalgiewicz
 */
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .headers()
        .frameOptions().sameOrigin()
        .httpStrictTransportSecurity()
        .disable();
  }
}
