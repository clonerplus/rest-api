package ir.sobhan.restapi.conroller.individuals;

import ir.sobhan.restapi.controller.coursesection.*;
import ir.sobhan.restapi.controller.individuals.*;
import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.request.*;
import ir.sobhan.restapi.service.coursesection.CourseService;
import ir.sobhan.restapi.service.coursesection.TermService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CourseSectionControllerTest {
    @Autowired
    TermService termService;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseSectionController courseSectionController;
    @Autowired
    TermController termController;
    @Autowired
    StudentController studentController;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseSectionRepository courseSectionRepository;
    @Autowired
    TestUtils testUtils;

    @Test
    @WithMockUser(roles = {"INSTRUCTOR", "ADMIN", "STUDENT"})
    void setStudentScore() {

        long studentId = testUtils.generateStudent();

        long instructorId = testUtils.generateInstructor();

        long courseSectionId = testUtils.generateCourseSection();

        long termId = courseSectionRepository.findById(courseSectionId).get().getTerm().getId();

        long courseId = courseSectionRepository.findById(courseSectionId).get().getTerm().getId();

        CourseSectionRequest courseSectionRequest = CourseSectionRequest.builder()
                        .TermTitle(termService.getTermById(termId).get().getTitle())
                .CourseTitle(courseService.getCourseById(courseId).get().getTitle())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "ahmad10",  // Replace with the actual username
                "12345",  // Replace with the actual password
                List.of(new SimpleGrantedAuthority("STUDENT"))  // Replace with the appropriate role(s)
        );

        studentController.joinCourseSection(courseSectionRequest, authentication);

        SetStudentsScoreRequest setStudentsScoreRequest = SetStudentsScoreRequest.builder()
                        .scores(new HashMap<>(Map.of("124241244", 17.5)))
                        .build();

        courseSectionController.setStudentsScores(courseSectionId, setStudentsScoreRequest);

        Double score = testUtils.getStudentById(studentId).get().getCourseSectionRegistration().get(0).getScore();

        assertThat(score).isEqualTo(17.5);
    }
}
