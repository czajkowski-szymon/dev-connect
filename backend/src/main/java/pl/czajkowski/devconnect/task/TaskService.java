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
import pl.czajkowski.devconnect.task.models.UpdateTaskUser;
import pl.czajkowski.devconnect.user.UserService;
import pl.czajkowski.devconnect.user.models.User;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       UserDetailsService userDetailsService,
                       UserService userService,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public List<Task> getAllTasksForProject(Integer projectId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        if (!project.getContributors().contains(user) && !project.getProjectManager().equals(user)) {
            throw new ProjectContributionException("You are not manager nor contributor of given project");
        }

        return taskRepository.findAllByProjectId(projectId);
    }

    public Task getTaskForProject(Integer projectId, Integer taskId, String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%s] not found".formatted(taskId))
        );

        if (!project.getContributors().contains(user) && !project.getProjectManager().equals(user)) {
            throw new ProjectContributionException("You are not manager nor contributor of given project");
        }

        if (!task.getProject().equals(project)) {
            throw new TaskNotBelongToProjectException(
                    "Task with id: [%s] does not belong to project with id: [%s]".formatted(taskId, projectId)
            );
        }

        return taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%] not found")
        );
    }

    public Task addTaskForProject(AddTaskRequest request, Integer projectId, String username) {

        User projectManager = (User) userDetailsService.loadUserByUsername(username);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        if (!project.getProjectManager().equals(projectManager)) {
            throw new ProjectContributionException("You are not manager of given project");
        }

        User user = userService.getUserById(request.userId());

        if (!project.getContributors().contains(user)) {
             throw new ProjectContributionException(
                     "User with id: [%s] is not contributor for project with id: [%s]"
                             .formatted(request.userId(), projectId)
             );
        }

        Task task = new Task(
                request.body(),
                request.deadline()
        );
        task.setUser(user);
        task.setProject(project);

        return  taskRepository.save(task);
    }

    public Task updateTask(UpdateTaskUser request, Integer projectId, String username) {

        User projectManager = (User) userDetailsService.loadUserByUsername(username);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        if (!project.getProjectManager().equals(projectManager)) {
            throw new ProjectContributionException("You are not manager of given project");
        }

        Task task = taskRepository.findById(request.id()).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: [%] not found")
        );
        task.setBody(request.body());
        task.setDeadline(request.deadline());

        return taskRepository.save(task);
    }

    public void deleteTask(Integer projectId, Integer taskId, String username) {

        User projectManager = (User) userDetailsService.loadUserByUsername(username);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: [%s] not found".formatted(projectId))
        );

        if (!project.getProjectManager().equals(projectManager)) {
            throw new ProjectContributionException("You are not manager of given project");
        }

        taskRepository.deleteById(taskId);

    }
}
