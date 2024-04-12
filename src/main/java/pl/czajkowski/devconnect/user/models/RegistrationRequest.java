package pl.czajkowski.devconnect.user.models;

public record RegistrationRequest(
        String email,
        String password,
        String firstName
) {
}