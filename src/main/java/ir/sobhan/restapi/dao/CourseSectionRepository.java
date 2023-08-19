package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.coursesection.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {

    Optional<List<CourseSection>> findAllByTermTitle(String title);
    void updateByTermTitleAndCourseTitle(String termTitle, String CourseTitle);
    Optional<CourseSection> findByTermTitleAndCourseTitle(String termTitle, String courseTitle);

    void deleteByTermTitleAndCourseTitle(String termTitle, String courseTitle);
}
