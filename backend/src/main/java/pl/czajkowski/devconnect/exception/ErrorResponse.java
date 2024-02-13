package pl.czajkowski.devconnect.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String path,
        int statusCode,
        String message,
        LocalDateTime timestamp
) {
}
