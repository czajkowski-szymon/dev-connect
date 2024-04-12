package pl.czajkowski.devconnect.user;

import org.junit.jupiter.api.Test;
import pl.czajkowski.devconnect.user.models.Role;
import pl.czajkowski.devconnect.user.models.User;
import pl.czajkowski.devconnect.user.models.UserDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserDTOMapperTest {

    @Test
    public void shouldMapToUserDTO() {
        UserDTOMapper mapper = new UserDTOMapper();
        String email = "john@gmail.com";
        String password = "password";
        String firstName = "John";
        String gitUrl = "github.com";
        Role role = Role.USER;
        User user = new User(
                email,
                password,
                firstName,
                role,
                false,
                true
        );
        user.setProfileImageId("1111");
        user.setId(1);

        UserDTO userDto = mapper.apply(user);

        assertThat(userDto.id()).isEqualTo(1);
        assertThat(userDto.email()).isEqualTo(email);
        assertThat(userDto.firstName()).isEqualTo(firstName);
        assertThat(userDto.profileImageId()).isEqualTo("1111");
    }
}