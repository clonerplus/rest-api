package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.coursesection.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);
    void deleteTermByTitle(String title);
}
