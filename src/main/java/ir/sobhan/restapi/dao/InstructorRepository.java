package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.individual.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByCustomUserUsername(String username);
}
