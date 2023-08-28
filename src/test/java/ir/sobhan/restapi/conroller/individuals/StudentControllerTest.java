package ir.sobhan.restapi.conroller.individuals;

import ir.sobhan.restapi.controller.coursesection.CourseSectionController;
import ir.sobhan.restapi.controller.individuals.StudentController;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.StudentRepository;
import ir.sobhan.restapi.model.individual.Student;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.request.SetStudentsScoreRequest;
import ir.sobhan.restapi.response.GetStudentScoreResponse;
import ir.sobhan.restapi.service.coursesection.CourseService;
import ir.sobhan.restapi.service.coursesection.TermService;
import ir.sobhan.restapi.service.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentControllerTest {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    StudentController studentController;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseSectionRepository courseSectionRepository;
    @Autowired
    TermService termService;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseSectionController courseSectionController;
    @Autowired
    TestUtils testUtils;

//    @BeforeEach
//    void setUp() {
//        testUtils.generateStudent();
//    }
//
//    @AfterEach
//    void tearDown() {
//        testUtils.clearCustomUserRepository();
//    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void authorizeStudent() {

        long studentId = testUtils.generateStudent();

        Optional<Student> student = studentRepository.findById(studentId);

        assertThat(student.isEmpty()).isEqualTo(false);

    }

    @Test
    @WithMockUser(roles = {"INSTRUCTOR", "ADMIN", "STUDENT"})
    void getStudentScores() {

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
        
        ResponseEntity<String> result = studentController.getTermScores(termId, authentication);

        System.out.println(result);

//        Map<String, Double> courseScores = Objects.requireNonNull(result.getBody()).getScores();
//        Double termAverageScore = Objects.requireNonNull(result.getBody()).getAverage();

//        assert courseScores != null;
//        Map<String, Double> expectedScores = Map.of(
//                "MATH1", 17.5
//        );
//
//
//        assertThat(courseScores).isEqualTo(expectedScores);
//        assertThat(termAverageScore).isEqualTo(17.5);
    }

}
