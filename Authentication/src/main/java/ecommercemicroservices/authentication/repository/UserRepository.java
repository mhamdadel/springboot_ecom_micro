package ecommercemicroservices.authentication.repository;

import ecommercemicroservices.authentication.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {

    Optional<CustomUser> findByEmail(String email);
    @Query("SELECT u FROM CustomUser u WHERE u.username = ?1 or u.email = ?2")
    Optional<CustomUser> findByUsernameOrEmail(String username,
                                               @NotNull(message = "Email cannot be null")
                                               @Size(
                                                min = 1,
                                                max = 255,
                                                    message = "Email length must be between 1 and 255 characters"
                                               ) String email
    );
    boolean existsByEmail(String email);
}