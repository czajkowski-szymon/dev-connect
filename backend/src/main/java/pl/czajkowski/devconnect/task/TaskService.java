package pl.czajkowski.devconnect.task;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.exception.ProjectContributionException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.TaskNotBelongToProjectException;
import pl.czajkowski.devconnect.project.ProjectRepository;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.task.models.AddTaskRequest;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.task.models.TaskDTO;
import pl.czajkowski.devconnect.task.models.UpdateTaskRequest;
import pl.czajkowski.devconnect.user.UserService;
import pl.czajkowski.devconnect.user.models.User;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private final ProjectRepository projectRepository;

    private final TaskDTOMapper mapper;

    public TaskService(TaskRepository taskRepository,
                       UserDetailsService userDetailsService,
                       UserService userService,
                       ProjectRepository projectRepository,
                       TaskDTOMapper mapper) {
        this.taskRepository = taskRepository;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }

    public List<TaskDTO> getAllTasksForProject(Integer projectId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        validateProjectContributionOrManaging(project, user);

        return taskRepository.findAllByProjectId(projectId)
                .stream()
                .map(mapper)
                .toList();
    }

    public TaskDTO getTaskForProject(Integer projectId, Integer taskId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        validateProjectContributionOrManaging(project, user);

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%s] not found".formatted(taskId))
        );

        validateTaskBelongsToProject(project, task);

        return mapper.apply(
                taskRepository.findById(taskId).orElseThrow(
                    () -> new ResourceNotFoundException("Task with id: [%] not found")
                )
        );
    }

    public TaskDTO addTaskForProject(AddTaskRequest request, Integer projectId, String username) {

        User projectManager = (User) userDetailsService.loadUserByUsername(username);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        validateProjectManaging(project, projectManager);

        User user = userService.getUserById(request.userId());

        validateProjectContribution(project, user);

        Task task = new Task(
                request.body(),
                request.deadline()
        );
        task.setUser(user);
        task.setProject(project);

        return mapper.apply(taskRepository.save(task));
    }

    public TaskDTO updateTask(UpdateTaskRequest request, Integer projectId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        validateProjectManaging(project, projectManager);

        Task task = taskRepository.findById(request.id()).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%] not found")
        );
        task.setBody(request.body());
        task.setDeadline(request.deadline());

        return mapper.apply(taskRepository.save(task));
    }

    public void deleteTask(Integer projectId, Integer taskId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        validateProjectManaging(project, projectManager);

        taskRepository.deleteById(taskId);
    }

    private void validateProjectContributionOrManaging(Project project, User user) {
        if (!isUserProjectManager(project, user) && !isUserProjectContributor(project, user)) {
            throw new ProjectContributionException(
                    "User with id: [%s] is not manager nor contributor for project with id: [%s]"
                            .formatted(user.getId(), project.getId())
            );
        }
    }

    private void validateProjectManaging(Project project, User user) {
        if (!isUserProjectManager(project, user)) {
            throw new ProjectContributionException(
                    "User with id: [%s] is not manager for project with id: [%s]"
                        .formatted(user.getId(), project.getId())
            );
        }
    }

    private void validateProjectContribution(Project project, User user) {
        if (!isUserProjectContributor(project, user)) {
            throw new ProjectContributionException(
                    "User with id: [%s] is not contributor for project with id: [%s]"
                            .formatted(user.getId(), project.getId())
            );
        }
    }

    private void validateTaskBelongsToProject(Project project, Task task) {
        if (!task.getProject().equals(project)) {
            throw new TaskNotBelongToProjectException(
                    "Task with id: [%s] does not belong to project with id: [%s]".formatted(task.getId(), project.getId())
            );
        }
    }

    private boolean isUserProjectManager(Project project, User user) {
        return project.getProjectManager().equals(user);
    }

    private boolean isUserProjectContributor(Project project, User user) {
        return project.getContributors().contains(user);
    }
}
