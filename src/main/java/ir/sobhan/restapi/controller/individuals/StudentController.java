package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.response.GetStudentScoreResponse;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/all-students")
    public ResponseEntity<ListResponse<Student>> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/student")
    public ResponseEntity<String> authorizeStudent(@RequestParam String username, @RequestBody Student student) {

        return studentService.authorizeStudent(username, student);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/join-course-section")
    public ResponseEntity<String> joinCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        return studentService.joinCourseSection(courseSectionRequest, authentication);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/check-scores/{termId}")
    public ResponseEntity<GetStudentScoreResponse> getTermScores(
            @PathVariable long termId, Authentication authentication) {

        return studentService.fetchTermScores(termId, authentication);
    }
}
