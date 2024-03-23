package pl.czajkowski.devconnect.task;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.czajkowski.devconnect.task.models.AddTaskRequest;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.task.models.TaskDTO;
import pl.czajkowski.devconnect.task.models.UpdateTaskRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksForProject(@PathVariable Integer projectId, Authentication user) {
        return ResponseEntity.ok(taskService.getAllTasksForProject(projectId, user.getName()));
    }

    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> getTaskForProject(@PathVariable("projectId") Integer projectId,
                                                  @PathVariable("taskId") Integer taskId,
                                                  Authentication user) {
        return ResponseEntity.ok(taskService.getTaskForProject(projectId, taskId, user.getName()));
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskDTO> addTaskForProject(@RequestBody AddTaskRequest request,
                                                  @PathVariable Integer projectId,
                                                  Authentication user) {
        TaskDTO response = taskService.addTaskForProject(request, projectId, user.getName());
        return ResponseEntity.created(
                URI.create("/projects/%s/tasks/".formatted(projectId) + response.id())
        ).body(response);
    }

    @PutMapping("/{projectId}/tasks")
    public ResponseEntity<TaskDTO> updateTask(@RequestBody UpdateTaskRequest request,
                                           @PathVariable Integer projectId,
                                           Authentication user) {
        return ResponseEntity.ok(taskService.updateTask(request, projectId, user.getName()));
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("taskId") Integer taskId,
                                        Authentication user) {
        taskService.deleteTask(projectId, taskId, user.getName());
        return ResponseEntity.noContent().build();
    }
}
