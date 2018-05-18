package pl.dalgim.gitty.api.logic.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import pl.dalgim.gitty.GittyWebApp;
import pl.dalgim.gitty.TestCase;
import pl.dalgim.gitty.api.ErrorDto;
import pl.dalgim.gitty.api.logic.ErrorCode;
import pl.dalgim.gitty.api.logic.config.AppParams;
import pl.dalgim.gitty.api.repository.RepositoryInfoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static pl.dalgim.gitty.GittyAssertions.assertThatResponseEntityIsCorrect;

/**
 * @author Mateusz Dalgiewicz
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = GittyWebApp.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestPropertySource(properties = {
    "gitty.clients.github.baseUrl=http://host:1000/testapi",
    "gitty.clients.github.repositoryPath=/repos",
    "server.port=9999"
})
public class RepositoryEndpointIT extends TestCase {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private MessageSource messageSource;
  private JacksonTester<Resource<RepositoryInfoDto>> jsonRepository;
  private JacksonTester<List<ErrorDto>> jsonError;
  private MockRestServiceServer server;
  private TestRestTemplate testRestTemplate;

  @Before
  public void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    JacksonTester.initFields(this, objectMapper);
    testRestTemplate = new TestRestTemplate();
    server = MockRestServiceServer
        .bindTo(restTemplate)
        .build();
  }

  @Test
  public void shouldReturnExpectedRepositoryInfoForGivenParams() throws Exception {
    //given
    String name = "dalgim";
    String repo = "1";
    String expectedGitResponseFileName = "get_repository_200.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/1";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/1";

    final String content = readFile(expectedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(content, MediaType.APPLICATION_JSON));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
        appRequestUrl,
        HttpMethod.GET,
        new HttpEntity<>(headers),
        String.class
    );

    //then
    assertThatResponseEntityIsCorrect(exchange, HttpStatus.OK);
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    assertThat(exchange.getHeaders().get("Cache-Control")).isNotNull();
    assertThat(exchange.getHeaders().get("Cache-Control").get(0)).isEqualTo("no-cache, no-store, max-age=0, must-revalidate");
    assertThat(exchange.getHeaders().get("Pragma")).isNotNull();
    assertThat(exchange.getHeaders().get("Pragma").get(0)).isEqualTo("no-cache");
    final Resource<RepositoryInfoDto> resource = jsonRepository
        .parse(exchange.getBody())
        .getObject();
    assertThat(resource).isNotNull();
    final RepositoryInfoDto repository = resource.getContent();
    assertThat(repository).isNotNull();
    assertThat(repository.getCloneUrl()).isNotNull().isEqualTo("https://github.com/dalgim/1.git");
    assertThat(repository.getFullName()).isNotNull().isEqualTo(name + "/" + repo);
    assertThat(repository.getCreatedAt()).isNotNull().isNotEqualTo(LocalDateTime.of(2018, 4, 14, 6, 53));
    assertThat(repository.getStars()).isEqualTo(0);
    assertThat(repository.getDescription()).isNull();
  }

  @Test
  public void shouldReturnBadRequestWhenOwnerIsTooLong() throws Exception {
    //given
    String expectedGitResponseFileName = "get_repository_404.json";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/1";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
            appRequestUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
    );

    //then
    assertThatResponseEntityIsCorrect(exchange, HttpStatus.BAD_REQUEST);
    assertThat(exchange.getHeaders().get(AppParams.TRACE_ID_HEADER_NAME)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    assertThat(exchange.getHeaders().get("Cache-Control")).isNotNull();
    assertThat(exchange.getHeaders().get("Cache-Control").get(0)).isEqualTo("no-cache, no-store, max-age=0, must-revalidate");
    assertThat(exchange.getHeaders().get("Pragma")).isNotNull();
    assertThat(exchange.getHeaders().get("Pragma").get(0)).isEqualTo("no-cache");
    final List<ErrorDto> errors = jsonError
            .parse(exchange.getBody())
            .getObject();
    Assertions.assertThat(errors).isNotNull();
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getPath()).isEqualTo("getByOwnerAndRepositoryName.arg0");
    assertThat(errors.get(0).getErrorCode()).isNotNull().isEqualTo("bad_request");
    assertThat(errors.get(0).getMessage()).isNotNull().isEqualTo("size must be between 1 and 39");
    assertThat(errors.get(0).getTimestamp()).isNotNull();
    assertThat(errors.get(0).getStatus()).isEqualTo(400);
  }

  @Test
  public void shouldReturnNotFoundResponseForGivenParams() throws Exception {
    //given
    String expecedGitResponseFileName = "get_repository_404.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/2";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/2";

    final String content = readFile(expecedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND).body(content));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
        appRequestUrl,
        HttpMethod.GET,
        new HttpEntity<>(headers),
        String.class
    );

    //then
    assertThatResponseEntityIsCorrect(exchange, HttpStatus.NOT_FOUND);
    assertThat(exchange.getHeaders().get(AppParams.TRACE_ID_HEADER_NAME)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    assertThat(exchange.getHeaders().get("Cache-Control")).isNotNull();
    assertThat(exchange.getHeaders().get("Cache-Control").get(0)).isEqualTo("no-cache, no-store, max-age=0, must-revalidate");
    assertThat(exchange.getHeaders().get("Pragma")).isNotNull();
    assertThat(exchange.getHeaders().get("Pragma").get(0)).isEqualTo("no-cache");
    final List<ErrorDto> errors = jsonError
        .parse(exchange.getBody())
        .getObject();
    Assertions.assertThat(errors).isNotNull();
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getPath()).isNull();
    assertThat(errors.get(0).getErrorCode()).isNotNull().isEqualTo(RepositoryErrorCodes.REPOSITORY_NOT_FOUND.getCode());
    assertThat(errors.get(0).getMessage()).isNotNull().isEqualTo(messageSource.getMessage(errors.get(0).getErrorCode(), new Object[]{}, Locale.ROOT));
    assertThat(errors.get(0).getTimestamp()).isNotNull();
    assertThat(errors.get(0).getStatus()).isEqualTo(RepositoryErrorCodes.REPOSITORY_NOT_FOUND.getHttpStatusRawValue());
  }

  @Test
  public void shouldReturnNotAcceptableForPostRequest() throws Exception {
    //given
    String expecedGitResponseFileName = "get_repository_404.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/2";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/2";

    final String content = readFile(expecedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_ACCEPTABLE).body(content));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
            appRequestUrl,
            HttpMethod.POST,
            new HttpEntity<>(headers),
            String.class
    );

    //then
    assertThatResponseEntityIsCorrect(exchange, HttpStatus.NOT_ACCEPTABLE);
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    final List<ErrorDto> errors = jsonError
            .parse(exchange.getBody())
            .getObject();
    Assertions.assertThat(errors).isNotNull();
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getPath()).isNull();
    assertThat(errors.get(0).getErrorCode()).isNotNull().isEqualTo(ErrorCode.DefaultErrorCode.NOT_ACCEPTABLE.getCode());
    assertThat(errors.get(0).getMessage()).isNotNull().isEqualTo(messageSource.getMessage(errors.get(0).getErrorCode(), new Object[]{}, Locale.ROOT));
    assertThat(errors.get(0).getTimestamp()).isNotNull();
    assertThat(errors.get(0).getStatus()).isEqualTo(ErrorCode.DefaultErrorCode.NOT_ACCEPTABLE.getHttpStatusRawValue());
  }

  @Test
  public void shouldReturnUnsupportedMediaTypeWhenContentTypeNotGiven() throws Exception {
    //given
    String expecedGitResponseFileName = "get_repository_404.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/2";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/2";

    final String content = readFile(expecedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(content));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, null);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
            appRequestUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
    );

    //then
    assertThatResponseEntityIsCorrect(exchange, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    assertThat(exchange.getHeaders().get("Cache-Control")).isNotNull();
    assertThat(exchange.getHeaders().get("Cache-Control").get(0)).isEqualTo("no-cache, no-store, max-age=0, must-revalidate");
    assertThat(exchange.getHeaders().get("Pragma")).isNotNull();
    assertThat(exchange.getHeaders().get("Pragma").get(0)).isEqualTo("no-cache");
    final List<ErrorDto> errors = jsonError
            .parse(exchange.getBody())
            .getObject();
    Assertions.assertThat(errors).isNotNull();
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getPath()).isNull();
    assertThat(errors.get(0).getErrorCode()).isNotNull().isEqualTo(ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode());
    assertThat(errors.get(0).getMessage()).isNotNull().isEqualTo(messageSource.getMessage(errors.get(0).getErrorCode(), new Object[]{}, Locale.ROOT));
    assertThat(errors.get(0).getTimestamp()).isNotNull();
    assertThat(errors.get(0).getStatus()).isEqualTo(ErrorCode.DefaultErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatusRawValue());
  }

  @Test
  public void shouldReturnNotAcceptableWhenAcceptNotSupported() throws Exception {
    //given
    String expecedGitResponseFileName = "get_repository_404.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/2";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/2";

    final String content = readFile(expecedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_ACCEPTABLE).body(content));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
            appRequestUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
    );

    //then
    assertThatResponseEntityIsCorrect(exchange, HttpStatus.NOT_ACCEPTABLE);
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("text/html;charset=iso-8859-1");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    assertThat(exchange.getHeaders().get("Cache-Control")).isNotNull();
    assertThat(exchange.getHeaders().get("Cache-Control").get(0)).isEqualTo("must-revalidate,no-cache,no-store");
    assertThat(exchange.getHeaders().get("Pragma")).isNotNull();
    assertThat(exchange.getHeaders().get("Pragma").get(0)).isEqualTo("no-cache");
    assertThat(exchange.getBody().replace("\n", "").replace("\n", ""))
            .isEqualTo("<html><head><meta http-equiv=\"Content-Type\" " +
                    "content=\"text/html;charset=utf-8\"/><title>Error " +
                    "406 Not Acceptable</title></head><body><h2>HTTP ERROR " +
                    "406</h2><p>Problem accessing /gitty/error. Reason:<pre> " +
                    "   Not Acceptable</pre></p></body></html>");
  }


  @Test
  public void shouldReturnInternalSeverErrorWhenGitHubReturnServerError() throws Exception {
    //given
    String expecedGitResponseFileName = "get_repository_500.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/1";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/1";

    final String content = readFile(expecedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR).body(content));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
            appRequestUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
    );

    assertThatResponseEntityIsCorrect(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    final List<ErrorDto> errors = jsonError
            .parse(exchange.getBody())
            .getObject();
    Assertions.assertThat(errors).isNotNull();
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getPath()).isNull();
    assertThat(errors.get(0).getErrorCode()).isNotNull().isEqualTo(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getCode());
    assertThat(errors.get(0).getMessage()).isNotNull().isEqualTo(messageSource.getMessage(errors.get(0).getErrorCode(), new Object[]{}, Locale.ROOT));
    assertThat(errors.get(0).getTimestamp()).isNotNull();
    assertThat(errors.get(0).getStatus()).isEqualTo(ErrorCode.DefaultErrorCode.INTERNAL_SERVER_ERROR.getHttpStatusRawValue());
  }

  @Test
  public void shouldReturnSerivceUnavailableWhenGitHubClientReturnConnectionTimeout() throws Exception {
    //given
    String expecedGitResponseFileName = "get_repository_408.json";
    String gitRequestUrl = "http://host:1000/testapi/repos/dalgim/1";
    String appRequestUrl = "http://localhost:9999/gitty/v1/repositories/dalgim/1";

    final String content = readFile(expecedGitResponseFileName);
    server.expect(requestTo(gitRequestUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.REQUEST_TIMEOUT).body(content));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "pl,en;q=0.9,en-US;q=0.8");

    //when
    final ResponseEntity<String> exchange = testRestTemplate.exchange(
            appRequestUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
    );

    assertThatResponseEntityIsCorrect(exchange, HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE)).isNotNull();
    assertThat(exchange.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/json;charset=utf-8");
    assertThat(exchange.getHeaders().get("X-Content-Type-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Content-Type-Options").get(0)).isEqualTo("nosniff");
    assertThat(exchange.getHeaders().get("X-XSS-Protection")).isNotNull();
    assertThat(exchange.getHeaders().get("X-XSS-Protection").get(0)).isEqualTo("1; mode=block");
    assertThat(exchange.getHeaders().get("X-Frame-Options")).isNotNull();
    assertThat(exchange.getHeaders().get("X-Frame-Options").get(0)).isEqualTo("SAMEORIGIN");
    final List<ErrorDto> errors = jsonError
            .parse(exchange.getBody())
            .getObject();
    Assertions.assertThat(errors).isNotNull();
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getPath()).isNull();
    assertThat(errors.get(0).getErrorCode()).isNotNull().isEqualTo(ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE.getCode());
    assertThat(errors.get(0).getMessage()).isNotNull().isEqualTo(messageSource.getMessage(errors.get(0).getErrorCode(), new Object[]{}, Locale.ROOT));
    assertThat(errors.get(0).getTimestamp()).isNotNull();
    assertThat(errors.get(0).getStatus()).isEqualTo(ErrorCode.DefaultErrorCode.SERVICE_UNAVAILABLE.getHttpStatusRawValue());
  }
}
