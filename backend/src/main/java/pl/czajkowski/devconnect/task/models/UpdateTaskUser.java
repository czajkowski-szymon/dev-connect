package pl.czajkowski.devconnect.task.models;

import java.time.LocalDate;

public record UpdateTaskUser(Integer id, String body, LocalDate deadline) {
}
