package pl.czajkowski.devconnect.project;

import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.user.UserDTOMapper;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProjectDTOMapper implements Function<Project, ProjectDTO> {
    @Override
    public ProjectDTO apply(Project project) {
        UserDTOMapper mapper = new UserDTOMapper();

        return new ProjectDTO(
                project.getId(),
                project.getProjectName(),
                project.getDescription(),
                mapper.apply(project.getProjectManager()),
                project.getContributors().stream().map(mapper).collect(Collectors.toList()),
                project.getTechnologies()
        );
    }
}
