package pl.czajkowski.devconnect.admin;

import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.exception.UserNotFoundException;
import pl.czajkowski.devconnect.user.UserRepository;
import pl.czajkowski.devconnect.user.models.User;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void lockUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id: [%s] not found".formatted(userId))
        );
        user.setLocked(true);
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
