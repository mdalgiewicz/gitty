package pl.dalgim.gitty.common.rest;

import org.springframework.web.client.RestTemplate;
import java.net.URI;

/**
 * @author <a href="mateusz.dalgiewicz@coi.gov.pl">Mateusz Dalgiewicz</a>
 * @since 18.04.18
 */
public class RestTemplateWrapper {

    private final RestTemplate restTemplate;

    public RestTemplateWrapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T getForEntity(final URI uri, final Class<T> responseClazz) {
        if (uri == null) {
            throw new NullPointerException("URI cannot be null");
        }
        if (responseClazz == null) {
            throw new NullPointerException("Response Class type cannot be null");
        }
        return restTemplate.getForEntity(
                uri,
                responseClazz
        ).getBody();
    }

}
