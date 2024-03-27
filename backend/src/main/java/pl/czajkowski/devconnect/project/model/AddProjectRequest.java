package pl.czajkowski.devconnect.project.model;

public record AddProjectRequest(
        String projectName,
        String description
) {
}
