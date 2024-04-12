package pl.czajkowski.devconnect.task;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czajkowski.devconnect.task.models.Task;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByProjectId(Integer projectId);
}
