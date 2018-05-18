package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest;

/**
 * @author <a href="mateusz.dalgiewicz@coi.gov.pl">Mateusz Dalgiewicz</a>
 * @since 18.04.18
 */
public class RestGitHubClientTimeoutException extends RuntimeException {

    public RestGitHubClientTimeoutException(String message) {
        super(message);
    }
}
