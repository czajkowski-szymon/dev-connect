package pl.czajkowski.devconnect.task;

import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.task.models.TaskDTO;

import java.util.function.Function;

@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {
    @Override
    public TaskDTO apply(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getBody(),
                task.getDeadline(),
                task.isDone()
        );
    }
}
