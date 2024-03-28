package pl.czajkowski.devconnect.project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProjectRequest(
        @NotNull(message = "project id is mandatory") Integer id,
        @NotBlank(message = "project name is mandatory") String projectName,
        String description
) {
}
