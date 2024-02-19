package pl.czajkowski.devconnect.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserDetailsServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void shouldThrowForNonExistingUser() {
        // given
        String email = "email";

        // then
        assertThatThrownBy(() -> underTest.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with username: " + email + " not found");
    }
}