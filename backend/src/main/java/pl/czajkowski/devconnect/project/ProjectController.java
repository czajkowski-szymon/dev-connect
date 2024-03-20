package pl.czajkowski.devconnect.project;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.czajkowski.devconnect.project.model.AddProjectRequest;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.project.model.UpdateProjectRequest;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable("projectId") Integer projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectDTO>> getAllProjectManagedByUser(@PathVariable Integer userId,
                                                                       Authentication user) {
        return ResponseEntity.ok(projectService.getAllProjectManagedByUser(userId, user.getName()));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> addProject(@RequestBody AddProjectRequest request, Authentication user) {
        ProjectDTO response = projectService.addProject(request, user.getName());
        return ResponseEntity.created(URI.create("/projects/" + response.id())).body(response);
    }

    @GetMapping("/{projectId}/contributors")
    public ResponseEntity<List<UserDTO>> getAllContributorsForProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(projectService.getAllContributorsForProject(projectId));
    }

    @PostMapping("/{projectId}/contributors/{userId}")
    public ResponseEntity<ProjectDTO> addContributorToProject(@PathVariable("projectId") Integer projectId,
                                                              @PathVariable("userId") Integer userId,
                                                              Authentication user) {
        return ResponseEntity.ok(projectService.addContributorToProject(projectId, userId, user.getName()));
    }

    @PutMapping
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody UpdateProjectRequest request, Authentication user) {
        return ResponseEntity.ok(projectService.updateProject(request, user.getName()));
    }

    @DeleteMapping("/{projectId}/contributors/{userId}")
    public ResponseEntity<ProjectDTO> deleteContributor(@PathVariable("projectId") Integer projectId,
                                               @PathVariable("userId") Integer userId,
                                               Authentication user) {
        return ResponseEntity.ok(projectService.deleteContributor(projectId, userId, user.getName()));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId, Authentication user) {
        projectService.deleteProject(projectId, user.getName());
        return ResponseEntity.noContent().build();
    }
}