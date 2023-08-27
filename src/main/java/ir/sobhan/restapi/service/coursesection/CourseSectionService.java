package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.dao.CourseSectionRegistrationRepository;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.InstructorRepository;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.model.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.request.SetStudentsScoreRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public CourseSectionService(TermService termService, CourseService courseService,
                                InstructorRepository instructorRepository,
                                CourseSectionRepository courseSectionRepository,
                                CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.termService = termService;
        this.courseService = courseService;
        this.instructorRepository = instructorRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public String buildCourseSection(
            @NotNull CourseSectionRequest courseSectionRequest,
            String instructorName) {
        var term = termService.getTermByTitle(courseSectionRequest.getTermTitle());
        if (term.isEmpty()) {
            return "Term not found!";
        }

        var course = courseService.getCourseByTitle(courseSectionRequest.getCourseTitle());
        if (course.isEmpty()) {
            return "Course not found!";
        }
        var instructor = instructorRepository.findByCustomUserUsername(instructorName);
        var courseSection = CourseSection.builder()
                .instructor(instructor.get())
                .term(term.get())
                .course(course.get())
                .build();

        courseSectionRepository.save(courseSection);

        return "courseSection created successfully!";
    }

    public Optional<CourseSection> getCourseSectionByTermTitleAndCourseTitle(
            String termTitle, String courseTitle) {

        return courseSectionRepository.findByTermTitleAndCourseTitle(termTitle, courseTitle);
    }

    public Optional<List<CourseSection>> getAllTermsAndStudentsCount(@NotNull String termTitle) {
        return courseSectionRepository.findAllByTermTitle(termTitle);
    }

    public Optional<List<CourseSection>> getAllTerms(@NotNull String termTitle) {
        return courseSectionRepository.findAllByTermTitle(termTitle);
    }

    public String deleteCourseSection(@NotNull CourseSectionRequest courseSectionRequest,
                                                        Authentication authentication) {

        if (!courseSectionRepository.findByTermTitleAndCourseTitle(courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle()).get().getInstructor().getCustomUser().getUsername()
                .equals(authentication.getName()))
            return "user not authorized to delete this course section";

        courseSectionRepository.deleteByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());

        return "successfully deleted term!";
    }

    private void setStudentScore(List<CourseSectionRegistration> scores,
                                 String studentId, Double score) {

        scores.stream()
                .filter(registration -> registration.getStudent().getStudentId().equals(studentId))
                .forEach(registration -> {
                    registration.setScore(score);
                    courseSectionRegistrationRepository.save(registration);
                });
    }

    public String setStudentsScore(
            long courseSectionId,
            @NotNull SetStudentsScoreRequest setStudentsScoreRequest) {

        Optional<CourseSection> courseSection = courseSectionRepository.findById(courseSectionId);

        if (courseSection.isEmpty())
            return "course section not found!";

        List<CourseSectionRegistration> courseSectionRegistrations = courseSection.get()
                .getCourseSectionRegistration();

        Map<String, Double> studentScores = setStudentsScoreRequest.getScores();

        studentScores.forEach((studentId, score) -> setStudentScore(courseSectionRegistrations,
                studentId, score));

        return "updated scores successfully!";

    }

}
