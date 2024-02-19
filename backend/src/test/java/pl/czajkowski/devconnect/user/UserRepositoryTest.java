package pl.czajkowski.devconnect.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.czajkowski.devconnect.PostgreContainer;
import pl.czajkowski.devconnect.user.models.Role;
import pl.czajkowski.devconnect.user.models.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends PostgreContainer {

    @Autowired
    UserRepository underTest;

    @Test
    public void shouldUpdateProfileImageId() {
        User user = new User(
                "szymon@gmail.com",
                "password",
                "Szymon",
                "github.com",
                Role.USER,
                false,
                true
        );
        user.setProfileImageId("1111");
        underTest.save(user);

        underTest.updateProfileImageId("2222", user.getId());
        User userFromDb = underTest.findById(user.getId()).get();

        assertThat(userFromDb.getProfileImageId()).isEqualTo("2222");
    }
}