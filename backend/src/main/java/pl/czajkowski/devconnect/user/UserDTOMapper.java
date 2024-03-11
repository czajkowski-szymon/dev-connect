package pl.czajkowski.devconnect.user;

import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.user.models.User;
import pl.czajkowski.devconnect.user.models.UserDTO;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getGitUrl(),
                user.getProfileImageId(),
                user.getTechnologies()
        );
    }
}
