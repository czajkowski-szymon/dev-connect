package pl.czajkowski.devconnect.project;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czajkowski.devconnect.project.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
