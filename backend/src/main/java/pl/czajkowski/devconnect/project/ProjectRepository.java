package pl.czajkowski.devconnect.project;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.user.models.User;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByProjectManager(User projectManager);

    @Modifying
    @Transactional
    @Query(
            value = "delete from user_project where user_id = :contributorId and project_id = :projectId",
            nativeQuery = true
    )
    void deleteContributor(Integer contributorId, Integer projectId);
}
