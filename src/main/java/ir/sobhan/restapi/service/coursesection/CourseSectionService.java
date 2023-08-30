package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.controller.exception.ApiRequestException;
import ir.sobhan.restapi.dao.CourseSectionRegistrationRepository;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.InstructorRepository;
import ir.sobhan.restapi.model.entity.coursesection.CourseSection;
import ir.sobhan.restapi.model.entity.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.model.input.coursesection.CourseSectionRequest;
import ir.sobhan.restapi.model.input.coursesection.SetStudentsScoreRequest;
import org.jetbrains.annotations.NotNull;
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
        var term = termService.getTermByTitle(courseSectionRequest.getTermTitle());
        var course = courseService.getCourseByTitle(courseSectionRequest.getCourseTitle());

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

    public List<CourseSection> getAllTermsAndStudentsCount(String termTitle) {
        if (termTitle == null) {
            throw new ApiRequestException("invalid term title!", HttpStatus.BAD_REQUEST);
        }
        return courseSectionRepository.findAllByTermTitle(termTitle)
                .orElseThrow(() -> new ApiRequestException("term not found!", HttpStatus.NOT_FOUND));
    }

    public List<CourseSection> getAllTerms(String termTitle) {
        if (termTitle == null) {
            throw new ApiRequestException("invalid term title!", HttpStatus.BAD_REQUEST);
        }
        return courseSectionRepository.findAllByTermTitle(termTitle)
                .orElseThrow(() -> new ApiRequestException("term not found!", HttpStatus.NOT_FOUND));
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

    private void setStudentScore(SetStudentsScoreRequest setStudentsScoreRequest,
                                 List<CourseSectionRegistration> courseSectionRegistrationList) {
        setStudentsScoreRequest.getScores().forEach((studentId, score) ->
                courseSectionRegistrationList.stream()
                        .filter(registration -> registration.getStudent().getStudentId().equals(studentId))
                        .forEach(registration -> registration.setScore(score)));
        courseSectionRegistrationRepository.saveAll(courseSectionRegistrationList);
    }

    public void setStudentsScore(long courseSectionId,
            @NotNull SetStudentsScoreRequest setStudentsScoreRequest) {

        var courseSection = courseSectionRepository.findById(courseSectionId)
                .orElseThrow(() -> new ApiRequestException("course section not found!",
                        HttpStatus.NOT_FOUND));

        var courseSectionRegistrationList = courseSection.getCourseSectionRegistration();
        setStudentScore(setStudentsScoreRequest, courseSectionRegistrationList);
    }
}
