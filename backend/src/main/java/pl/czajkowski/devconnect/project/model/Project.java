package pl.czajkowski.devconnect.project.model;

import jakarta.persistence.*;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.user.models.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String projectName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "project_manager_id", nullable = false)
    private User projectManager;

    @ManyToMany(mappedBy = "contributedProjects")
    private List<User> contributors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_technology",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologies;

    public Project() {
    }

    public Project(String projectName,
                   String description,
                   User projectManager) {
        this.projectName = projectName;
        this.description = description;
        this.projectManager = projectManager;
        this.contributors = new ArrayList<>();
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

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }
}
