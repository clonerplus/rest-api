package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.entity.individual.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
