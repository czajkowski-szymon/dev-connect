package pl.czajkowski.devconnect.task.models;

import jakarta.persistence.*;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.user.models.User;

import java.time.LocalDate;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String body;

    private LocalDate deadline;

    private boolean isDone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Task() {
    }

    public Task(String body, LocalDate deadline) {
        this.body = body;
        this.deadline = deadline;
        this.isDone = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
