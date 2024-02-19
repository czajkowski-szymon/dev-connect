package pl.czajkowski.devconnect.user;

import org.junit.jupiter.api.Test;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.user.models.Role;
import pl.czajkowski.devconnect.user.models.User;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserDTOMapperTest {

    @Test
    public void shouldMapToUserDTO() {
        UserDTOMapper mapper = new UserDTOMapper();
        String email = "john@gmail.com";
        String password = "password";
        String firstName = "John";
        String githubUrl = "github.com";
        Role role = Role.USER;
        List<Technology> technologies = new ArrayList<>();
        User user = new User(
                email,
                password,
                firstName,
                githubUrl,
                role,
                false,
                true
        );
        user.setProfileImageId("1111");
        user.setTechnologies(technologies);
        user.setId(1);

        UserDTO userDto = mapper.apply(user);

        assertThat(userDto.id()).isEqualTo(1);
        assertThat(userDto.email()).isEqualTo(email);
        assertThat(userDto.firstName()).isEqualTo(firstName);
        assertThat(userDto.githubUrl()).isEqualTo(githubUrl);
        assertThat(userDto.profileImageId()).isEqualTo("1111");
        assertThat(userDto.technologies()).isEqualTo(technologies);
    }
}