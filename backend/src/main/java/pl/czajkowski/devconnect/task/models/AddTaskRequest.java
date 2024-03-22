package pl.czajkowski.devconnect.task.models;

import java.time.LocalDate;

public record AddTaskRequest(String body, LocalDate deadline, Integer userId) {
}
