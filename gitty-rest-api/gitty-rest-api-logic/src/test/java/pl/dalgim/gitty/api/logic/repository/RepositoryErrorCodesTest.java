package pl.dalgim.gitty.api.logic.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Mateusz Dalgiewicz
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryErrorCodesTest {

  @Autowired
  private MessageSource messageSource;

  @Test
  public void shouldContainsMessagesForDefaultErrorCodes() throws Exception {
    //throw exception if not found message for given code
    Arrays
        .stream(RepositoryErrorCodes.values())
        .forEach((RepositoryErrorCodes e) -> {
            messageSource.getMessage(e.getCode(), new Object[] {}, Locale.ENGLISH);
            messageSource.getMessage(e.getCode(), new Object[] {}, Locale.ROOT);
            messageSource.getMessage(e.getCode(), new Object[] {}, new Locale("pl"));
    });
  }
}