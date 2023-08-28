package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.model.individual.Student;
import ir.sobhan.restapi.request.coursesection.CourseSectionRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/student")
    public ResponseEntity<String> authorizeStudent(@RequestParam String username,
                                                   @RequestBody Student student) {
        try {
            studentService.authorizeStudent(username, student);

            return ResponseEntity.ok("Authorized user to student limits successfully");

        } catch (ApiRequestException e) {
            return e.getResponseEntity();
        }
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/join-course-section")
    public ResponseEntity<String> joinCourseSection(@RequestBody CourseSectionRequest courseSectionRequest,
                                                    Authentication authentication) {
        try {
            studentService.joinCourseSection(courseSectionRequest, authentication);

            return ResponseEntity.ok("joined to course section successfully!");

        } catch (ApiRequestException e) {
            return e.getResponseEntity();
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/check-scores/{termId}")
    public ResponseEntity<String> getTermScores(@PathVariable long termId, Authentication authentication) {
        try {
            var studentScoreResponse = studentService.fetchTermScores(termId, authentication);

            return ResponseEntity.ok(String.valueOf(studentScoreResponse));

        } catch (ApiRequestException e) {
            return e.getResponseEntity();
        }
    }
}
