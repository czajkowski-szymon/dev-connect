package pl.czajkowski.devconnect.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.czajkowski.devconnect.exception.EmailAlreadyExistsException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.UserNotFoundException;
import pl.czajkowski.devconnect.s3.S3Service;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.technology.TechnologyRepository;
import pl.czajkowski.devconnect.user.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        boolean emailExists = userRepository.existsUserByEmail(request.email());
        if (emailExists) {
            throw new EmailAlreadyExistsException("User with email: " + request.email() + " already exists");
        }

        User user = new User(
                request.email(),
                encoder.encode(request.password()),
                request.firstName(),
                request.githubUrl(),
                Role.USER,
                false,
                true
        );

        List<Technology> technologies = new ArrayList<>();
        List<String> technologyNames = request.technologies();
        for (String technologyName : technologyNames) {
            Technology t = technologyRepository.findByTechnologyName(technologyName).orElseThrow(
                    () -> new ResourceNotFoundException("Technology not found")
            );
            t.addUser(user);
            technologies.add(t);
        }

        user.setTechnologies(technologies);

        return new RegistrationResponse(mapper.apply(userRepository.save(user)), REGISTER_SUCCESS_MSG);
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
        UserDTO user = userRepository.findById(userId).map(mapper).orElseThrow(
                () -> new UserNotFoundException("User with id: [%s] not found".formatted(userId))
        );

        String profileImageId = user.profileImageId();
        if (profileImageId.isBlank()) {
            throw new ResourceNotFoundException("profile image for user with id: [%s] not found".formatted(userId));
        }

        return s3.downloadFile("profile-images/%s/%s".formatted(userId, profileImageId));
    }
}
