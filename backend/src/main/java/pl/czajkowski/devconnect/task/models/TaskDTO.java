package pl.czajkowski.devconnect.task.models;

import java.time.LocalDate;

public record TaskDTO(Integer id, String body, LocalDate deadline, boolean isDone) {
}
