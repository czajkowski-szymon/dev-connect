package pl.czajkowski.devconnect.task;

import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.task.models.TaskDTO;
import pl.czajkowski.devconnect.user.UserDTOMapper;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.util.function.Function;

@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {

    private final UserDTOMapper mapper;

    public TaskDTOMapper(UserDTOMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TaskDTO apply(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getBody(),
                task.getDeadline(),
                task.isDone(),
                mapper.apply(task.getUser())
        );
    }
}
