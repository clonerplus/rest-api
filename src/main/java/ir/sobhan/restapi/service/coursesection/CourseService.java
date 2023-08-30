package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.controller.exception.ApiRequestException;
import ir.sobhan.restapi.dao.CourseRepository;
import ir.sobhan.restapi.model.entity.coursesection.Course;
import ir.sobhan.restapi.model.input.coursesection.CourseRequest;
import ir.sobhan.restapi.model.output.ListResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    public void buildCourse(@NotNull CourseRequest courseRequest) {
        courseRepository.findByTitle(courseRequest.getTitle())
                .ifPresent(existingCourse -> {
                    throw new ApiRequestException("Course already exists!\n" +
                            "Please consider updating the course for your use", HttpStatus.BAD_REQUEST);
                });

        var course = Course.builder()
                .title(courseRequest.getTitle())
                .units(courseRequest.getUnits())
                .courseSection(courseRequest.getCourseSection())
                .build();

        courseRepository.save(course);
    }

    public Course getCourseByTitle(String title) {
        if (title == null) {
            throw new ApiRequestException("Invalid course title", HttpStatus.BAD_REQUEST);
        }
        return courseRepository.findByTitle(title)
                .orElseThrow(() -> new ApiRequestException("course not found!", HttpStatus.NOT_FOUND));
    }

    public Optional<Course> getCourseById(long id) {
        return courseRepository.findById(id);
    }

    public ListResponse<Course> getAllCourses() {
        return ListResponse.<Course>builder()
                .responseList(courseRepository.findAll())
                .build();
    }

    public void deleteTerm(@NotNull String title) {
        courseRepository.deleteTermByTitle(title);
    }
}
