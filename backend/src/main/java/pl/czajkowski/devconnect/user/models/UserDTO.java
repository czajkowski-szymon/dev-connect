package pl.czajkowski.devconnect.user.models;

import pl.czajkowski.devconnect.technology.Technology;

import java.util.List;

public record UserDTO(
        Integer id,
        String email,
        String firstName,
        String gitUrl,
        String profileImageId,
        List<Technology> technologies
) {
}