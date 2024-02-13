package pl.czajkowski.devconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceUploadException extends RuntimeException {
    public ResourceUploadException(String message) {
        super(message);
    }
}
