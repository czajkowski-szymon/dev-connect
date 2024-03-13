package pl.czajkowski.devconnect.technology;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.user.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "technology")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String technologyName;
    @JsonIgnore
    @ManyToMany(mappedBy = "technologies")
    private List<User> users;
    @JsonIgnore
    @ManyToMany(mappedBy = "technologies")
    private List<Project> projects;

    public Technology() {}

    public Technology(Integer id, String technologyName) {
        this.id = id;
        this.technologyName = technologyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> user) {
        this.users = user;
    }

    public void addUser(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void addProject(Project project) {
        if (projects == null) {
            projects = new ArrayList<>();
        }
        projects.add(project);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technology that = (Technology) o;
        return Objects.equals(id, that.id) && Objects.equals(technologyName, that.technologyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, technologyName);
    }
}
