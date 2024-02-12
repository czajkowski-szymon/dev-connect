package pl.czajkowski.devconnect.user;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
        List<String> techNames = request.technologies();
        for (String techName : techNames) {
            // TODO: custom exception
            Technology t = technologyRepository.findByTechnologyName(techName).orElseThrow(
                    () -> new RuntimeException("technology not found")
            );
            t.addUser(user);
            technologies.add(t);
        }

        user.setTechnologies(technologies);

        return new RegistrationResponse(mapper.apply(userRepository.save(user)), REGISTER_SUCCESS_MSG);
    }

    public void uploadProfileImage(Integer userId, MultipartFile file) {
        String profileImageId = UUID.randomUUID().toString();

        try {
            s3.uploadFile("profile-images/%s/%s".formatted(userId, profileImageId), file.getBytes());
        } catch (IOException e) {
            // TODO: custom exception
            throw new RuntimeException(e);
        }

        userRepository.updateProfileImageId(profileImageId, userId);
    }

    public byte[] downloadProfileImage(Integer userId) {
        // TODO: custom exception
        UserDTO user = userRepository.findById(userId).map(mapper).orElseThrow(
                () -> new RuntimeException("user with id: [%s] not found".formatted(userId))
        );

        String profileImageId = user.profileImageId();
        if (profileImageId.isBlank()) {
            throw new RuntimeException("profile image for user with id: [%s] not found".formatted(userId));
        }

        return s3.downloadFile("profile-images/%s/%s".formatted(userId, profileImageId));
    }
}
