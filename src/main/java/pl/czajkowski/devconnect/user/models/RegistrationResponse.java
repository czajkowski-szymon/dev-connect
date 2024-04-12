package pl.czajkowski.devconnect.user.models;

public record RegistrationResponse(
        UserDTO user,
        String message
) {
}