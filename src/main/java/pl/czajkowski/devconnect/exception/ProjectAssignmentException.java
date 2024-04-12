package pl.czajkowski.devconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ProjectAssignmentException extends RuntimeException {
    public ProjectAssignmentException(String message) {
        super(message);
    }
}
