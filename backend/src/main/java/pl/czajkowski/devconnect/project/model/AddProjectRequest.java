package pl.czajkowski.devconnect.project.model;

import java.util.List;

public record AddProjectRequest(
        String projectName,
        String description,
        List<String> technologies
) {
}
