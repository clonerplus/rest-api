package ir.sobhan.restapi.conroller.individuals;

import ir.sobhan.restapi.controller.individuals.*;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StudentRepository;
import ir.sobhan.restapi.model.coursesection.*;
import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.request.*;
import ir.sobhan.restapi.service.coursesection.*;
import ir.sobhan.restapi.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;

import java.util.*;

@SpringBootTest
@Service
public class TestUtils {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    TermService termService;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseSectionService courseSectionService;
    @Autowired
    StudentController studentController;
    @Autowired
    InstructorController instructorController;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CustomUserRepository customUserRepository;
    public RegisterRequest generateStudentRegisterRequest() {

        return RegisterRequest.builder()
                .username("ahmad10")
                .password("12345")
                .phone(null)
                .nationalId(null)
                .admin(false)
                .active(true)
                .build();
    }

    public RegisterRequest generateInstructorRegisterRequest() {
        return RegisterRequest.builder()
                .username("instructor")
                .password("12345")
                .phone(null)
                .nationalId(null)
                .admin(false)
                .active(true)
                .build();
    }

    @WithMockUser(roles = "ADMIN")
    public long generateTerm() {

        var term = Term.builder()
                .title("fall")
                .open(true)
                .build();

        termService.buildTerm(term);

        return termService.getTermByTitle(term.getTitle()).get().getId();
    }

    @WithMockUser(roles = "ADMIN")
    public long generateCourse() {

        var course = Course.builder()
                .title("MATH1")
                .units(3)
                .build();

        courseService.buildCourse(course);

        return courseService.getCourseByTitle(course.getTitle()).get().getId();
    }

    public long generateCourseSection() {
        long termId = generateTerm();
        String termTitle = termService.getTermById(termId).get().getTitle();

        long courseId = generateCourse();
        String courseTitle = courseService.getCourseById(courseId).get().getTitle();

        CourseSectionRequest courseSectionRequest = CourseSectionRequest.builder()
                                                            .TermTitle(termTitle)
                                                            .CourseTitle(courseTitle)
                                                            .build();

        courseSectionService.buildCourseSection(courseSectionRequest, "instructor");

        return courseSectionService.getCourseSectionByTermTitleAndCourseTitle(termTitle, courseTitle).get().getId();

    }

    @WithMockUser(roles = "ADMIN")
    public long generateInstructor() {

        RegisterRequest instructorRegisterRequest = generateInstructorRegisterRequest();

        authenticationService.register(instructorRegisterRequest);

        Optional<Instructor> instructor = Optional.ofNullable(Instructor.builder()
                .rank(Instructor.Rank.ASSISTANT)
                .build());

        instructorController.authorizeInstructor(instructorRegisterRequest.getUsername(),
                instructor.get());

        return instructor.get().getId();
    }

    @WithMockUser(roles = "ADMIN")
    public long generateStudent() {

        RegisterRequest studentRegisterRequest = generateStudentRegisterRequest();

        authenticationService.register(studentRegisterRequest);

        Optional<Student> student = Optional.ofNullable(Student.builder()
                .studentId("124241244")
                .degree(Student.Degree.MS)
                .startDate(new Date(12))
                .build());

        studentController.authorizeStudent(studentRegisterRequest.getUsername(), student.get());

        return student.get().getId();
    }

    public Optional<Student> getStudentById(long id) {

        return studentRepository.findById(id);
    }

    public void clearCustomUserRepository() {

        customUserRepository.deleteAll();
    }


}
