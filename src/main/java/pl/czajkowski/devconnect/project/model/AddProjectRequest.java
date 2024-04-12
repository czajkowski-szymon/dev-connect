package pl.czajkowski.devconnect.project.model;

import jakarta.validation.constraints.NotBlank;

public record AddProjectRequest(
        @NotBlank(message = "project name is mandatory") String projectName,
        String description
) {
}
