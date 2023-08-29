package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.entity.individual.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByCustomUserUsername(String username);
}
