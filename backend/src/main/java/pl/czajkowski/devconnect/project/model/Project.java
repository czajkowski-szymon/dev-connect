package pl.czajkowski.devconnect.project.model;

import jakarta.persistence.*;
import pl.czajkowski.devconnect.task.models.Task;
import pl.czajkowski.devconnect.user.models.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String projectName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @ManyToMany
    @JoinTable(
            name = "user_project",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<User> contributors = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    public Project() {
    }

    public Project(String projectName,
                   String description,
                   User projectManager) {
        this.projectName = projectName;
        this.description = description;
        this.projectManager = projectManager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public List<User> getContributors() {
        return contributors;
    }

    public void setContributors(List<User> contributors) {
        this.contributors = contributors;
    }
}
