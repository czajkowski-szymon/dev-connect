package pl.czajkowski.devconnect.task.models;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddTaskRequest(
        @NotBlank(message = "task body is mandatory") String body,
        @NotNull(message = "deadline is mandatory") @Future(message = "deadline must be in future")  LocalDate deadline,
        @NotNull(message = "user id is mandatory") Integer userId) {
}
