package pl.czajkowski.devconnect.project;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.czajkowski.devconnect.project.model.AddProjectRequest;
import pl.czajkowski.devconnect.project.model.ProjectDTO;

import java.net.URI;

@RestController
@RequestMapping("api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> addProject(@RequestBody AddProjectRequest request, Authentication user) {
        ProjectDTO response = projectService.addProject(request, user.getName());
        return ResponseEntity.created(URI.create("/projects/" + response.id())).body(response);
    }

    @PostMapping("/{projectId}/contributors/{userId}")
    public ResponseEntity<ProjectDTO> addContributorToProject(@PathVariable("projectId") Integer projectId,
                                                              @PathVariable("userId") Integer userId,
                                                              Authentication user) {
        return ResponseEntity.ok(projectService.addContributorToProject(projectId, userId, user.getName()));
    }
}