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
import pl.czajkowski.devconnect.user.UserDTOMapper;
import pl.czajkowski.devconnect.user.UserService;
import pl.czajkowski.devconnect.user.models.User;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final TechnologyRepository technologyRepository;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final ProjectDTOMapper projectMapper;

    private final UserDTOMapper userMapper;

    public ProjectService(ProjectRepository projectRepository,
                          TechnologyRepository technologyRepository,
                          UserService userService,
                          UserDetailsService userDetailsService,
                          ProjectDTOMapper projectMapper,
                          UserDTOMapper userMapper) {
        this.projectRepository = projectRepository;
        this.technologyRepository = technologyRepository;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
    }

    public ProjectDTO getProject(Integer projectId) {
        return projectRepository.findById(projectId).map(projectMapper).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );
    }

    public List<ProjectDTO> getAllProjectManagedByUser(Integer userId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        return projectRepository.findByProjectManager(user).stream()
                .map(projectMapper)
                .toList();
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

        return projectMapper.apply(projectRepository.save(project));
    }


    public List<UserDTO> getAllContributorsForProject(Integer projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        return project.getContributors().stream().map(userMapper).toList();
    }

    public ProjectDTO addContributorToProject(Integer projectId, Integer userId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);
        Project project = getProjectById(projectId);

        validateProjectOwnership(project, projectManager);
        validateAssignment(projectManager, userId);

        User contributor = userService.getUserById(userId);
        project.getContributors().add(contributor);
        contributor.addContributedProject(project);

        return projectMapper.apply(projectRepository.save(project));
    }

    public ProjectDTO deleteContributor(Integer projectId, Integer userId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);
        Project project = getProjectById(projectId);

        validateProjectOwnership(project, projectManager);

        User contributor = project.getContributors().stream()
                .filter(c -> c.getId().equals(userId))
                .findFirst()
                .orElseThrow(
                        () -> new UserNotFoundException("User with id: %s is not contributor of this project")
                );

        project.getContributors().remove(contributor);
        projectRepository.deleteContributor(userId, projectId);
        return projectMapper.apply(project);
    }

    public void deleteProject(Integer projectId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);
        Project project = getProjectById(projectId);

        validateProjectOwnership(project, projectManager);

        projectRepository.deleteById(projectId);
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
