package pl.czajkowski.devconnect.auth.models;

import pl.czajkowski.devconnect.user.models.UserDTO;

public record LoginResponse(UserDTO user, String token) {
}
