package pl.czajkowski.devconnect.user.models;

import java.util.List;

public record RegistrationRequest(
        String email,
        String password,
        String firstName,
        String githubUrl,
        List<String> technologies
) {
}