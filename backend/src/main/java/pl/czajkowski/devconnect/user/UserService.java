package pl.czajkowski.devconnect.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.czajkowski.devconnect.exception.EmailAlreadyExistsException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.UserNotFoundException;
import pl.czajkowski.devconnect.project.ProjectRepository;
import pl.czajkowski.devconnect.project.model.ProjectDTO;
import pl.czajkowski.devconnect.s3.S3Service;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.technology.TechnologyRepository;
import pl.czajkowski.devconnect.user.models.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final String REGISTER_SUCCESS_MSG = "User registered successfully";
    private final UserRepository userRepository;
    private final TechnologyRepository technologyRepository;
    private final UserDTOMapper mapper;
    private final PasswordEncoder encoder;
    private final S3Service s3;

    public UserService(
            UserRepository userRepository,
            TechnologyRepository technologyRepository,
            UserDTOMapper mapper,
            PasswordEncoder encoder,
            S3Service s3) {
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.s3 = s3;
    }

    public RegistrationResponse register(RegistrationRequest request) {
        validateEmail(request.email());

        User user = new User(
                request.email(),
                encoder.encode(request.password()),
                request.firstName(),
                request.gitUrl(),
                Role.USER,
                false,
                true
        );

        List<Technology> technologies = mapTechnologiesToUser(request.technologies(), user);
        user.setTechnologies(technologies);

        return new RegistrationResponse(mapper.apply(userRepository.save(user)), REGISTER_SUCCESS_MSG);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id: [%s] not found".formatted(userId))
        );
    }

    public void uploadProfileImage(Integer userId, MultipartFile file) {
        if (!userRepository.existsUserById(userId)) {
            throw new UserNotFoundException("User with id: [%s] not found".formatted(userId));
        }

        String profileImageId = UUID.randomUUID().toString();

        try {
            s3.uploadFile("profile-images/%s/%s".formatted(userId, profileImageId), file.getBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException("Failed to upload profile image");
        }

        userRepository.updateProfileImageId(profileImageId, userId);
    }

    public byte[] downloadProfileImage(Integer userId) {
        UserDTO user = mapper.apply(getUserById(userId));

        String profileImageId = user.profileImageId();
        if (profileImageId.isBlank()) {
            throw new ResourceNotFoundException("profile image for user with id: [%s] not found".formatted(userId));
        }

        return s3.downloadFile("profile-images/%s/%s".formatted(userId, profileImageId));
    }

    private void validateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new EmailAlreadyExistsException("User with email: " + email + " already exists");
        }
    }

    private List<Technology> mapTechnologiesToUser(List<String> technologyNames, User user) {
        return technologyNames.stream()
                .map(technologyName -> technologyRepository.findByTechnologyName(technologyName).orElseThrow(
                        () -> new ResourceNotFoundException("Technology not found")
                ))
                .peek(technology -> technology.addUser(user))
                .collect(Collectors.toList());
    }
}
