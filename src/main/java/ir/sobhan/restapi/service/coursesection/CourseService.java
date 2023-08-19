package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.dao.CourseRepository;
import ir.sobhan.restapi.model.coursesection.Course;
import ir.sobhan.restapi.model.coursesection.Term;
import ir.sobhan.restapi.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public String buildCourse(@NotNull Course courseRequest) {
        var course = Course.builder()
                .title(courseRequest.getTitle())
                .units(courseRequest.getUnits())
                .courseSection(courseRequest.getCourseSection())
                .build();

        courseRepository.save(course);

        return "course created successfully!";
    }

    public Optional<Course> getCourseByTitle(@NotNull String title) {

        return courseRepository.findByTitle(title);
    }

    public ListResponse<Course> getAllCourses() {

        return ListResponse.<Course>builder()
                .responseList(courseRepository.findAll())
                .build();
    }

    public String deleteTerm(@NotNull String title) {

        courseRepository.deleteTermByTitle(title);

        return "successfully deleted course!";
    }

}
