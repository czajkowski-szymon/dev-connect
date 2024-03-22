package pl.czajkowski.devconnect.task;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.exception.ProjectContributionException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.TaskNotBelongToProjectException;
import pl.czajkowski.devconnect.project.ProjectRepository;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.user.models.User;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserDetailsService userDetailsService;

    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       UserDetailsService userDetailsService,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userDetailsService = userDetailsService;
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
}
