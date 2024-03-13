package pl.czajkowski.devconnect.project;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.user.models.User;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByProjectManager(User projectManager);
}
