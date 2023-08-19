package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.request.CourseSectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class StudentController {

    private final CustomUserRepository customUserRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    @Autowired
    public StudentController(CustomUserRepository customUserRepository,
                             StudentRepository studentRepository,
                             CourseSectionRepository courseSectionRepository,
                             CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.customUserRepository = customUserRepository;
        this.studentRepository = studentRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    @GetMapping("/all-students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/student")
    public ResponseEntity<String> authorizeStudent(@RequestParam String username, @RequestBody Student student) {

        if (username == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username!");

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username not found!");

        student.setCustomUser(customUser.get());
        customUser.get().setStudent(student);
        customUser.get().setRole(Role.STUDENT);
        studentRepository.save(student);

        return ResponseEntity.ok("Authorized user to student limits successfully");
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/join-course-section")
    public ResponseEntity<String> joinCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        var courseSection = courseSectionRepository.findByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());

        if (courseSection.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid term title or course title!");


        var student = studentRepository.findByCustomUserUsername(authentication.getName());

        CourseSectionRegistration courseSectionRegistration =
                CourseSectionRegistration.builder()
                        .courseSection(courseSection.get())
                        .student(student.get())
                        .build();

        var courseSectionRegistrationList = student.get().getCourseSectionRegistration();
        courseSectionRegistrationList.add(courseSectionRegistration);
        student.get().setCourseSectionRegistration(courseSectionRegistrationList);

        courseSectionRegistrationList = courseSection.get().getCourseSectionRegistration();
        courseSectionRegistrationList.add(courseSectionRegistration);
        courseSection.get().setCourseSectionRegistration(courseSectionRegistrationList);

        courseSectionRegistrationRepository.save(courseSectionRegistration);
        studentRepository.updateStudentByCustomUserUsername(authentication.getName(), student.get());
        courseSectionRepository.updateByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());

        return ResponseEntity.ok("joined to course section successfully!");
    }
}
