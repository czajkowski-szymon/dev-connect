package pl.czajkowski.devconnect.auth.models;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email is mandatory") String email,
        @NotBlank(message = "password is mandatory") String password
) {
}