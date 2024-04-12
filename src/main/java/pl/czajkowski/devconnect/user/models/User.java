package pl.czajkowski.devconnect.user.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.czajkowski.devconnect.project.model.Project;
import pl.czajkowski.devconnect.task.models.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    private String firstName;

    private String profileImageId;

    @OneToMany(mappedBy = "projectManager", cascade = CascadeType.REMOVE)
    private List<Project> managedProjects;

    @ManyToMany(mappedBy = "contributors")
    private List<Project> contributedProjects;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean locked;

    private boolean enabled;

    public User() {}

    public User(
            String email,
            String password,
            String firstName,
            Role role,
            boolean locked,
            boolean enabled
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.role = role;
        this.locked = locked;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(String profileImageId) {
        this.profileImageId = profileImageId;
    }

    public List<Project> getContributedProjects() {
        return contributedProjects;
    }

    public void setContributedProjects(List<Project> contributedProjects) {
        this.contributedProjects = contributedProjects;
    }

    public List<Project> getManagedProjects() {
        return managedProjects;
    }

    public void setManagedProjects(List<Project> managedProjects) {
        this.managedProjects = managedProjects;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addContributedProject(Project project) {
        if (contributedProjects == null) {
            contributedProjects = new ArrayList<>();
        }
        contributedProjects.add(project);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return locked == user.locked &&
                enabled == user.enabled &&
                Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(profileImageId, user.profileImageId) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, email, password, firstName, profileImageId, role, locked, enabled
        );
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", profileImageId='" + profileImageId + '\'' +
                ", contributedProjects=" + contributedProjects +
                ", managedProject=" + managedProjects +
                ", tasks=" + tasks +
                ", role=" + role +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }
}
