package pl.czajkowski.devconnect.project;

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
import pl.czajkowski.devconnect.project.model.AddProjectRequest;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.project.model.UpdateProjectRequest;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project Management")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Get project by id",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable("projectId") Integer projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @Operation(
            summary = "Get all projects managed by user",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectDTO>> getAllProjectManagedByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(projectService.getAllProjectManagedByUser(userId));
    }

    @Operation(
            summary = "Add project",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ProjectDTO> addProject(@Valid @RequestBody AddProjectRequest request, Authentication user) {
        ProjectDTO response = projectService.addProject(request, user.getName());
        return ResponseEntity.created(URI.create("/projects/" + response.id())).body(response);
    }

    @Operation(
            summary = "Get all contributors for project",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{projectId}/contributors")
    public ResponseEntity<List<UserDTO>> getAllContributorsForProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(projectService.getAllContributorsForProject(projectId));
    }

    @Operation(
            summary = "Add contributor to project",
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
    @PostMapping("/{projectId}/contributors/{userId}")
    public ResponseEntity<ProjectDTO> addContributorToProject(@PathVariable("projectId") Integer projectId,
                                                              @PathVariable("userId") Integer userId,
                                                              Authentication user) {
        return ResponseEntity.ok(projectService.addContributorToProject(projectId, userId, user.getName()));
    }

    @Operation(
            summary = "Update project",
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
    @PutMapping
    public ResponseEntity<ProjectDTO> updateProject(@Valid @RequestBody UpdateProjectRequest request, Authentication user) {
        return ResponseEntity.ok(projectService.updateProject(request, user.getName()));
    }

    @Operation(
            summary = "Delete contributor from project",
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
    @DeleteMapping("/{projectId}/contributors/{userId}")
    public ResponseEntity<ProjectDTO> deleteContributor(@PathVariable("projectId") Integer projectId,
                                                        @PathVariable("userId") Integer userId,
                                                        Authentication user) {
        return ResponseEntity.ok(projectService.deleteContributor(projectId, userId, user.getName()));
    }

    @Operation(
            summary = "Delete project",
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
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId, Authentication user) {
        projectService.deleteProject(projectId, user.getName());
        return ResponseEntity.noContent().build();
    }
}