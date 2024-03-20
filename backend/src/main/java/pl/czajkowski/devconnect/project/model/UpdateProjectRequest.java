package pl.czajkowski.devconnect.project.model;

import java.util.List;

public record UpdateProjectRequest(
        Integer id,
        String projectName,
        String description,
        List<String> technologies
) {
}
