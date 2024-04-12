package pl.czajkowski.devconnect.user.models;

public record UserDTO(
        Integer id,
        String email,
        String firstName,
        String profileImageId
) {
}