package pl.czajkowski.devconnect.project.model;

import pl.czajkowski.devconnect.user.models.UserDTO;

import java.util.List;

public record ProjectDTO(
        Integer id,
        String projectName,
        String description,
        UserDTO projectManager,
        List<UserDTO> contributors
) {
}
