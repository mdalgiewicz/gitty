package pl.dalgim.gitty.api.logic;

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
public class ErrorCodeTest {

  @Autowired
  private MessageSource messageSource;

  @Test
  public void shouldContainsMessagesForDefaultErrorCodes() throws Exception {
    //throw exception if not found message for given code
    Arrays
        .stream(ErrorCode.DefaultErrorCode.values())
        .forEach((ErrorCode.DefaultErrorCode e) -> {
                  messageSource.getMessage(e.getCode(), new Object[]{}, Locale.ENGLISH);
                  messageSource.getMessage(e.getCode(), new Object[]{}, Locale.ROOT);
                  messageSource.getMessage(e.getCode(), new Object[]{}, new Locale("pl"));
                }
        );
  }
}