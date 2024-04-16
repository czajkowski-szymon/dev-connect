package pl.czajkowski.devconnect.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.czajkowski.devconnect.exception.ErrorResponse;
import pl.czajkowski.devconnect.task.models.AddTaskRequest;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.task.models.TaskDTO;
import pl.czajkowski.devconnect.task.models.UpdateTaskRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Task Management")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Get all tasks for project",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksForProject(@PathVariable Integer projectId, Authentication user) {
        return ResponseEntity.ok(taskService.getAllTasksForProject(projectId, user.getName()));
    }

    @Operation(
            summary = "Get task for project by id",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> getTaskForProject(@PathVariable("projectId") Integer projectId,
                                                     @PathVariable("taskId") Integer taskId,
                                                     Authentication user) {
        return ResponseEntity.ok(taskService.getTaskForProject(projectId, taskId, user.getName()));
    }

    @Operation(
            summary = "Add task for project",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskDTO> addTaskForProject(@Valid @RequestBody AddTaskRequest request,
                                                     @PathVariable Integer projectId,
                                                     Authentication user) {
        TaskDTO response = taskService.addTaskForProject(request, projectId, user.getName());
        return ResponseEntity.created(
                URI.create("/projects/%s/tasks/".formatted(projectId) + response.id())
        ).body(response);
    }

    @Operation(
            summary = "Update task",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/{projectId}/tasks")
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody UpdateTaskRequest request,
                                              @PathVariable Integer projectId,
                                              Authentication user) {
        return ResponseEntity.ok(taskService.updateTask(request, projectId, user.getName()));
    }

    @Operation(
            summary = "Delete task",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("taskId") Integer taskId,
                                        Authentication user) {
        taskService.deleteTask(projectId, taskId, user.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Set task as done",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> setTaskDone(@PathVariable("projectId") Integer projectId,
                                         @PathVariable("taskId") Integer taskId,
                                         Authentication user) {
        taskService.setTaskDone(projectId, taskId, user.getName());
        return ResponseEntity.noContent().build();
    }
}
