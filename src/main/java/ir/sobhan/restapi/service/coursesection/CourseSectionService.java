package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.model.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.request.SetStudentsScoreRequest;
import ir.sobhan.restapi.response.ListResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CourseSectionService {

    private final TermService termService;
    private final CourseService courseService;
    private final InstructorRepository instructorRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final  CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    @Autowired
    public CourseSectionService(TermService termService,
                                CourseService courseService,
                                InstructorRepository instructorRepository,
                                CourseSectionRepository courseSectionRepository, CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.termService = termService;
        this.courseService = courseService;
        this.instructorRepository = instructorRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

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

    public Optional<CourseSection> getCourseSectionByTermTitleAndCourseTitle(String termTitle, String courseTitle) {

        return courseSectionRepository.findByTermTitleAndCourseTitle(termTitle, courseTitle);
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

    private void setStudentScore(List<CourseSectionRegistration> scores,
                                 String studentId, Double score) {

        for (var courseSectionRegistration: scores) {
            if (courseSectionRegistration.getStudent().getStudentId().equals(studentId)) {
                courseSectionRegistration.setScore(score);
                courseSectionRegistrationRepository.save(courseSectionRegistration);
            }
        }
    }

    public ResponseEntity<String> setStudentsScore(
            long courseSectionId,
            @NotNull SetStudentsScoreRequest setStudentsScoreRequest) {

        Optional<CourseSection> courseSection = courseSectionRepository.findById(courseSectionId);

        if (courseSection.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("course section not found!");

        List<CourseSectionRegistration> courseSectionRegistrations = courseSection.get().getCourseSectionRegistration();

        Map<String, Double> studentScores = setStudentsScoreRequest.getScores();

        for (Map.Entry<String, Double> entry : studentScores.entrySet()) {
            String studentId = entry.getKey();
            Double score = entry.getValue();

            setStudentScore(courseSectionRegistrations, studentId, score);
        }

        return ResponseEntity.ok("updated scores successfully!");

    }

}
