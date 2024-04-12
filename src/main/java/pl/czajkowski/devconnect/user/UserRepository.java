package pl.czajkowski.devconnect.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.czajkowski.devconnect.user.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    boolean existsUserById(Integer id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.profileImageId = ?1 WHERE u.id = ?2")
    int updateProfileImageId(@Param("imageProfileId") String imageProfileId, @Param("userId") Integer userId);
}