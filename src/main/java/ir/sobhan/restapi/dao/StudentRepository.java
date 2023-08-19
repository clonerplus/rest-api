package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.individual.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByCustomUserUsername(String username);
    void updateStudentByCustomUserUsername(String username, Student student);
}
