package pl.czajkowski.devconnect.task;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.exception.ProjectContributionException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.TaskNotBelongToProjectException;
import pl.czajkowski.devconnect.exception.TaskNotBelongToUserException;
import pl.czajkowski.devconnect.project.ProjectRepository;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.task.models.AddTaskRequest;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.task.models.TaskDTO;
import pl.czajkowski.devconnect.task.models.UpdateTaskRequest;
import pl.czajkowski.devconnect.user.UserService;
import pl.czajkowski.devconnect.user.models.User;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

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

        validateProjectContributionOrManaging(user, projectId);

        return taskRepository.findAllByProjectId(projectId)
                .stream()
                .map(mapper)
                .toList();
    }

    public TaskDTO getTaskForProject(Integer projectId, Integer taskId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);

        validateProjectContributionOrManaging(user, projectId);

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%s] not found".formatted(taskId))
        );

        validateTaskBelongsToProject(task, projectId);

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

        validateProjectManaging(projectManager, projectId);

        User user = userService.getUserById(request.userId());

        validateProjectContribution(user, projectId);

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

        validateProjectManaging(projectManager, projectId);

        Task task = taskRepository.findById(request.id()).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%] not found")
        );
        task.setBody(request.body());
        task.setDeadline(request.deadline());

        return mapper.apply(taskRepository.save(task));
    }

    public void deleteTask(Integer projectId, Integer taskId, String username) {
        User projectManager = (User) userDetailsService.loadUserByUsername(username);

        validateProjectManaging(projectManager, projectId);

        taskRepository.deleteById(taskId);
    }

    public void setTaskDone(Integer projectId, Integer taskId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        validateTaskBelongingOrProjectManaging(user, projectId, taskId);

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%s] not found")
        );
        task.setDone(true);
        taskRepository.save(task);
    }

    private void validateTaskBelongingOrProjectManaging(User user, Integer projectId, Integer taskId) {
        if (!isUserTaskOwner(user, taskId) && !isUserProjectManager(user, projectId)) {
            throw new TaskNotBelongToUserException(
                    "Task with id: [%s] does not belong to user with id: [%s]".formatted(taskId, user.getId())
            );
        }
    }

    private void validateProjectContributionOrManaging(User user, Integer projectId) {
        if (!isUserProjectManager(user, projectId) && !isUserProjectContributor(user, projectId)) {
            throw new ProjectContributionException(
                    "User with id: [%s] is not manager nor contributor for project with id: [%s]"
                            .formatted(user.getId(), projectId)
            );
        }
    }

    private void validateProjectManaging(User user, Integer projectId) {
        if (!isUserProjectManager(user, projectId)) {
            throw new ProjectContributionException(
                    "User with id: [%s] is not manager for project with id: [%s]"
                        .formatted(user.getId(), projectId)
            );
        }
    }

    private void validateProjectContribution(User user, Integer projectId) {
        if (!isUserProjectContributor(user, projectId)) {
            throw new ProjectContributionException(
                    "User with id: [%s] is not contributor for project with id: [%s]"
                            .formatted(user.getId(), projectId)
            );
        }
    }

    private void validateTaskBelongsToProject(Task task, Integer projectId) {
        if (!task.getProject().getId().equals(projectId)) {
            throw new TaskNotBelongToProjectException(
                    "Task with id: [%s] does not belong to project with id: [%s]".formatted(task.getId(), projectId)
            );
        }
    }

    private boolean isUserProjectManager(User user, Integer projectId) {
        return user.getManagedProject()
                .stream()
                .map(Project::getId)
                .toList()
                .contains(projectId);
    }

    private boolean isUserProjectContributor(User user, Integer projectId) {
        return user.getContributedProjects()
                .stream()
                .map(Project::getId)
                .toList()
                .contains(projectId);
    }

    private boolean isUserTaskOwner(User user, Integer taskId) {
        return user.getTasks()
                .stream()
                .map(Task::getId)
                .toList()
                .contains(taskId);
    }
}
