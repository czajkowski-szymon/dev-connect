package pl.czajkowski.devconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ProjectOwnershipException extends RuntimeException {
    public ProjectOwnershipException(String message) {
        super(message);
    }
}
