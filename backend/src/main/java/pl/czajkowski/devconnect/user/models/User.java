package pl.czajkowski.devconnect.user.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.czajkowski.devconnect.technology.Technology;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "user_",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "profile_image_id_unique",
                        columnNames = "profile_image_id"
                ),
        }
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    private String gitUrl;
    private String profileImageId;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_technology",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologies;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private boolean locked;
    private boolean enabled;

    public User() {}

    public User(
            String email,
            String password,
            String firstName,
            String gitUrl,
            Role role,
            boolean locked,
            boolean enabled
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.gitUrl = gitUrl;
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

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(String profileImageId) {
        this.profileImageId = profileImageId;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
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
                Objects.equals(gitUrl, user.gitUrl) &&
                Objects.equals(profileImageId, user.profileImageId) &&
                Objects.equals(technologies, user.technologies) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, email, password, firstName, gitUrl, profileImageId, technologies, role, locked, enabled
        );
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", gitUrl='" + gitUrl + '\'' +
                ", profileImageId='" + profileImageId + '\'' +
                ", technologies=" + technologies +
                ", role=" + role +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }
}
