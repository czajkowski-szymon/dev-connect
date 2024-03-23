package pl.czajkowski.devconnect.task.models;

import java.time.LocalDate;

public record UpdateTaskRequest(Integer id, String body, LocalDate deadline) {
}
