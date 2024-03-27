package pl.czajkowski.devconnect.task.models;

import pl.czajkowski.devconnect.user.models.UserDTO;

import java.time.LocalDate;

public record TaskDTO(Integer id, String body, LocalDate deadline, boolean isDone, UserDTO user) {
}
