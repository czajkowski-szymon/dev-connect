package pl.czajkowski.devconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ProjectContributionException extends RuntimeException {
    public ProjectContributionException(String message) {
        super(message);
    }
}
