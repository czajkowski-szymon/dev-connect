package pl.czajkowski.devconnect.user;

import org.springframework.stereotype.Component;
import pl.czajkowski.devconnect.user.models.User;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.util.function.Function;

@Component
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getGithubUrl(),
                user.getProfileImageId(),
                user.getTechnologies()
        );
    }
}
