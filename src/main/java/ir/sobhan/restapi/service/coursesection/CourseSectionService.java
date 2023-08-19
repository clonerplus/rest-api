package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseSectionService {

    private final CourseRepository courseRepository;
    private final TermService termService;
    private final CourseService courseService;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;

    public ResponseEntity<String> buildCourseSection(
            @NotNull CourseSectionRequest courseSectionRequest,
            String instructorName) {
        var term = termService.getTermByTitle(courseSectionRequest.getTermTitle());
        if (term.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Term not found!");
        }

        var course = courseService.getCourseByTitle(courseSectionRequest.getCourseTitle());
        if (course.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }
        var instructor = instructorRepository.findByCustomUserUsername(instructorName);
        var courseSection = CourseSection.builder()
                .instructor(instructor.get())
                .term(term.get())
                .course(course.get())
                .build();

        courseSectionRepository.save(courseSection);

        return ResponseEntity.ok("courseSection created successfully!");
    }

    public ResponseEntity<ListResponse<CourseSection>> getAllTermsAndStudentsCount(@NotNull String termTitle) {

        var termCourseSections = courseSectionRepository.findAllByTermTitle(termTitle);

        return termCourseSections.map(courseSections -> ResponseEntity.ok(ListResponse.<CourseSection>builder()
                .responseList(courseSections)
                .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<ListResponse<CourseSection>> getAllTerms(@NotNull String termTitle) {

        var termCourseSections = courseSectionRepository.findAllByTermTitle(termTitle);

        return termCourseSections.map(courseSections -> ResponseEntity.ok(ListResponse.<CourseSection>builder()
                .responseList(courseSections)
                .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    public ResponseEntity<String> deleteCourseSection(@NotNull CourseSectionRequest courseSectionRequest) {

        courseSectionRepository.deleteByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());

        return ResponseEntity.ok("successfully deleted term!");
    }

}
