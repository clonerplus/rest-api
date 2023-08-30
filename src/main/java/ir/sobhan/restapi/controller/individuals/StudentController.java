package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.model.entity.individual.Student;
import ir.sobhan.restapi.model.input.coursesection.CourseSectionRequest;
import ir.sobhan.restapi.model.output.ListResponse;
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
    public ResponseEntity<String> authorizeStudent(@RequestParam String username, @RequestBody Student student) {
            studentService.authorizeStudent(username, student);
            return ResponseEntity.ok("Authorized user to student limits successfully");
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/join-course-section")
    public ResponseEntity<String> joinCourseSection(@RequestBody CourseSectionRequest courseSectionRequest,
                                                    Authentication authentication) {
        studentService.joinCourseSection(courseSectionRequest, authentication);
        return ResponseEntity.ok("joined to course section successfully!");
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/check-scores/{termId}")
    public ResponseEntity<String> getTermScores(@PathVariable long termId, Authentication authentication) {
        var studentScoreResponse = studentService.fetchTermScores(termId, authentication);
        return ResponseEntity.ok(String.valueOf(studentScoreResponse));
    }
}
