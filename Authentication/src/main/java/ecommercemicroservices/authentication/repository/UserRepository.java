package ecommercemicroservices.authentication.repository;

import ecommercemicroservices.authentication.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Integer> {

    Optional<CustomUser> findByEmail(String email);
    boolean existsByEmail(String email);
}