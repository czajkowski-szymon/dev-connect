package pl.czajkowski.devconnect.task;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.czajkowski.devconnect.task.models.Task;

import java.util.List;

@RestController
@RequestMapping("api/projects")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> getAllTasksForProject(@PathVariable Integer projectId, Authentication user) {
        return ResponseEntity.ok(taskService.getAllTasksForProject(projectId, user.getName()));
    }

    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Task> getTaskForProject(@PathVariable("projectId") Integer projectId,
                                                  @PathVariable("taskId") Integer taskId,
                                                  Authentication user) {
        return ResponseEntity.ok(taskService.getTaskForProject(projectId, taskId, user.getName()));
    }
}
