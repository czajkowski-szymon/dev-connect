package pl.czajkowski.devconnect.project;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.exception.ProjectAssignmentException;
import pl.czajkowski.devconnect.exception.ProjectOwnershipException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.UserNotFoundException;
import pl.czajkowski.devconnect.project.model.AddProjectRequest;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.technology.TechnologyRepository;
import pl.czajkowski.devconnect.user.UserRepository;
import pl.czajkowski.devconnect.user.UserService;
import pl.czajkowski.devconnect.user.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final TechnologyRepository technologyRepository;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final ProjectDTOMapper mapper;

    public ProjectService(ProjectRepository projectRepository,
                          TechnologyRepository technologyRepository,
                          UserService userService,
                          UserDetailsService userDetailsService,
                          ProjectDTOMapper mapper) {
        this.projectRepository = projectRepository;
        this.technologyRepository = technologyRepository;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
    }

    public ProjectDTO addProject(AddProjectRequest request, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);

        Project project = new Project(
                request.projectName(),
                request.description(),
                projectManager
        );

        List<Technology> technologies = mapTechnologiesToProject(request.technologies(), project);
        project.setTechnologies(technologies);

        return mapper.apply(projectRepository.save(project));
    }

    public ProjectDTO addContributorToProject(Integer projectId, Integer userId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);
        Project project = getProjectById(projectId);

        validateProjectOwnership(project, projectManager);
        validateAssignment(projectManager, userId);

        User contributor = userService.getUserById(userId);
        project.getContributors().add(contributor);
        contributor.addContributedProject(project);

        return mapper.apply(projectRepository.save(project));
    }

    private List<Technology> mapTechnologiesToProject(List<String> technologyNames, Project project) {
        return technologyNames.stream()
                .map(technologyName -> technologyRepository.findByTechnologyName(technologyName).orElseThrow(
                        () -> new ResourceNotFoundException("Technology not found")
                ))
                .peek(technology -> technology.addProject(project))
                .collect(Collectors.toList());
    }

    private Project getProjectById(Integer projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with given ID does not exists")
        );
    }

    private void validateProjectOwnership(Project project, User user) {
        if (!user.equals(project.getProjectManager())) {
            throw  new ProjectOwnershipException("Project does not belong to given user");
        }
    }

    private void validateAssignment(User projectManager, Integer contributorId) {
        if (projectManager.getId().equals(contributorId)) {
            throw new ProjectAssignmentException("You can`t assign yourself to your project");
        }
    }
}
