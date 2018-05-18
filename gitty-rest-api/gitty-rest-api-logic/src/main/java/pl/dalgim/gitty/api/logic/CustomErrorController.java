package pl.dalgim.gitty.api.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dalgim.gitty.api.ErrorDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This controller catch every unexpected system exception, which occurred before API layer.
 *
 * @author Mateusz Dalgiewicz
 */
@RestController
@RequiredArgsConstructor
class CustomErrorController implements ErrorController {

  private static final String ERROR_PATH = "/error";
  private static final String STATUS_CODE_ATT = "javax.servlet.error.status_code";

  private final ErrorDtoFactory errorDtoFactory;

  @GetMapping(ERROR_PATH)
  List<ErrorDto> error(HttpServletRequest request) {
    int statusCode = (int) request.getAttribute(STATUS_CODE_ATT);
    switch (statusCode) {
      case 404: {
        return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.NOT_FOUND);
      }
      default: {
        return errorDtoFactory.createErrorList(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR);
      }
    }
  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }
}
