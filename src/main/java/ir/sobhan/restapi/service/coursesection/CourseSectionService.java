package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.dao.CourseSectionRegistrationRepository;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.InstructorRepository;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.model.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.request.coursesection.CourseSectionRequest;
import ir.sobhan.restapi.request.coursesection.SetStudentsScoreRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public void buildCourseSection(@NotNull CourseSectionRequest courseSectionRequest,
                                     String instructorName) {
        var term = termService.getTermByTitle(courseSectionRequest.getTermTitle())
                .orElseThrow(() -> new ApiRequestException("Term not found!", HttpStatus.NOT_FOUND));

        var course = courseService.getCourseByTitle(courseSectionRequest.getCourseTitle())
                .orElseThrow(() -> new ApiRequestException("Course not found!", HttpStatus.NOT_FOUND));

        var instructor = instructorRepository.findByCustomUserUsername(instructorName)
                .orElseThrow(() -> new ApiRequestException("Course not found!", HttpStatus.NOT_FOUND));

        var courseSection = CourseSection.builder()
                .instructor(instructor)
                .term(term)
                .course(course)
                .build();

        courseSectionRepository.save(courseSection);
    }
    public Optional<CourseSection> getCourseSectionByTermTitleAndCourseTitle(String termTitle,
                                                                             String courseTitle) {
        return courseSectionRepository.findByTermTitleAndCourseTitle(termTitle, courseTitle);
    }
    public Optional<List<CourseSection>> getAllTermsAndStudentsCount(@NotNull String termTitle) {
        return courseSectionRepository.findAllByTermTitle(termTitle);
    }
    public Optional<List<CourseSection>> getAllTerms(@NotNull String termTitle) {
        return courseSectionRepository.findAllByTermTitle(termTitle);
    }
    public void deleteCourseSection(@NotNull CourseSectionRequest courseSectionRequest,
                                                        Authentication authentication) {
        CourseSection courseSection = courseSectionRepository.findByTermTitleAndCourseTitle(
                        courseSectionRequest.getTermTitle(), courseSectionRequest.getCourseTitle())
                .orElseThrow(() -> new ApiRequestException("Course section not found",
                        HttpStatus.NOT_FOUND));

        if (!courseSection.getInstructor().getCustomUser().getUsername()
                .equals(authentication.getName())) {
            throw new ApiRequestException("User not authorized to delete this course section",
                    HttpStatus.FORBIDDEN);
        }
        courseSectionRepository.deleteByTermTitleAndCourseTitle(courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());
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
    public void setStudentsScore(long courseSectionId,
            @NotNull SetStudentsScoreRequest setStudentsScoreRequest) {

        var courseSection = courseSectionRepository.findById(courseSectionId)
                .orElseThrow(() -> new ApiRequestException("course section not found!",
                        HttpStatus.NOT_FOUND));

        setStudentsScoreRequest.getScores().forEach((studentId, score) ->
                setStudentScore(courseSection.getCourseSectionRegistration(), studentId, score));
    }
}
