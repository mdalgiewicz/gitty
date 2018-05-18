package pl.dalgim.gitty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mateusz Dalgiewicz
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = GittyWebApp.class)
public class GittyWebApp {

  public static void main(String[] args) {
    SpringApplication.run(GittyWebApp.class, args);
  }
}
