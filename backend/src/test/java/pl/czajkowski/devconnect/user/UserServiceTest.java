package pl.czajkowski.devconnect.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import pl.czajkowski.devconnect.exception.EmailAlreadyExistsException;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import pl.czajkowski.devconnect.exception.UserNotFoundException;
import pl.czajkowski.devconnect.s3.S3Service;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.technology.TechnologyRepository;
import pl.czajkowski.devconnect.user.models.RegistrationRequest;
import pl.czajkowski.devconnect.user.models.Role;
import pl.czajkowski.devconnect.user.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TechnologyRepository technologyRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private S3Service s3;
    private final UserDTOMapper mapper = new UserDTOMapper();
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, technologyRepository, mapper, encoder, s3);
    }

    @Test
    void shouldRegisterUser() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "john@gmail.com",
                "password",
                "John",
                "github.com",
                List.of("Java")
        );

        User user = new User(
                "john@gmail.com",
                "#sdawq@!/sds",
                "John",
                "github.com",
                Role.USER,
                false,
                true
        );

        String hashedPassword = "#sdawq@!sds";
        List<Technology> technologies = List.of(new Technology(1, "Java"));
        when(userRepository.existsUserByEmail(request.email())).thenReturn(false);
        when(encoder.encode(request.password())).thenReturn(hashedPassword);
        when(technologyRepository.findByTechnologyName("Java"))
                .thenReturn(Optional.of(new Technology(1, "Java")));
        when(userRepository.save(any())).thenReturn(user);

        // when
        underTest.register(request);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getPassword()).isEqualTo(hashedPassword);
        assertThat(capturedUser.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedUser.getGithubUrl()).isEqualTo(request.githubUrl());
        assertThat(capturedUser.getProfileImageId()).isNull();
        assertThat(capturedUser.getTechnologies()).isEqualTo(technologies);
        assertThat(capturedUser.getRole()).isEqualTo(Role.USER);
        assertThat(capturedUser.isLocked()).isFalse();
        assertThat(capturedUser.isEnabled()).isTrue();
    }

    @Test
    void shouldThrowForExistingEmail() {
        // given
        String email = "john@gmail.com";
        RegistrationRequest request = new RegistrationRequest(
                email,
                "password",
                "John",
                "github.com",
                new ArrayList<>()
        );
        when(userRepository.existsUserByEmail(email)).thenReturn(true);

        // then
        assertThatThrownBy(() -> underTest.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("User with email: " + request.email() + " already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowForNonExistingTechnology() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "john@gmail.com",
                "password",
                "John",
                "github.com",
                List.of("NonExistingTech")
        );

        String hashedPassword = "#sdawq@!/sds";
        List<Technology> technologies = List.of(new Technology(1, "NonExistingTech"));
        when(userRepository.existsUserByEmail(request.email())).thenReturn(false);
        when(encoder.encode(request.password())).thenReturn(hashedPassword);
        when(technologyRepository.findByTechnologyName("NonExistingTech"))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.register(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Technology not found");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUploadProfileImage() {
        int id = 1;
        byte[] bytes = "Hello".getBytes();
        MultipartFile file = new MockMultipartFile("file", bytes);

        when(userRepository.existsUserById(id)).thenReturn(true);

        underTest.uploadProfileImage(id, file);

        ArgumentCaptor<String> profileImageIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).updateProfileImageId(profileImageIdArgumentCaptor.capture(), eq(id));

        String profileImageId = profileImageIdArgumentCaptor.getValue();
        verify(s3).uploadFile("profile-images/%s/%s".formatted(id, profileImageId), bytes);
    }

    @Test
    void shouldThrowWhenUploadingProfileImageForNonExistingUser() {
        int id = 1;
        byte[] bytes = "Hello".getBytes();
        MultipartFile file = new MockMultipartFile("file", bytes);

        when(userRepository.existsUserById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.uploadProfileImage(id, file))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id: [%s] not found".formatted(id));
    }

    @Test
    void shouldThrowWhenUploadingProfileImageFails() throws IOException {
        int id = 1;
        byte[] bytes = "Hello".getBytes();
        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.existsUserById(id)).thenReturn(true);

        when(file.getBytes()).thenThrow(IOException.class);

        assertThatThrownBy(() -> underTest.uploadProfileImage(id, file))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Failed to upload profile image");
    }

    @Test
    void shouldDownloadImage() {
        int id = 1;
        byte[] expected = "Hello".getBytes();
        String profileImageId = "1111";
        User user = new User(
                "john@gmail.com",
                "password",
                "John",
                "github.com",
                Role.USER,
                false,
                true
        );
        user.setProfileImageId(profileImageId);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(s3.downloadFile("profile-images/%s/%s".formatted(id, profileImageId)))
                .thenReturn(expected);

        assertThat(underTest.downloadProfileImage(id)).isEqualTo(expected);
    }

    @Test
    void shouldThrowForNonExistingUser() {
        int id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.downloadProfileImage(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id: [%s] not found".formatted(id));

        verify(s3, never()).downloadFile(any());
    }

    @Test
    void shouldThrowForNonExistingProfileImage() {
        int id = 1;

        User user = mock(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        when(user.getProfileImageId()).thenReturn("");

        assertThatThrownBy(() -> underTest.downloadProfileImage(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("profile image for user with id: [%s] not found".formatted(id));
    }

}