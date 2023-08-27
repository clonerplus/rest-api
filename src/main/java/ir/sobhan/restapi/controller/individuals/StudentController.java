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

import java.util.HashMap;
import java.util.Map;


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
    public ResponseEntity<String> authorizeStudent(
            @RequestParam String username, @RequestBody Student student) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("Invalid username!", HttpStatus.BAD_REQUEST,
                        "username not found!", HttpStatus.NOT_FOUND,
                        "Authorized user to student limits successfully", HttpStatus.OK));

        String resultMsg = studentService.authorizeStudent(username, student);

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/join-course-section")
    public ResponseEntity<String> joinCourseSection(@RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("Invalid term title or course title!", HttpStatus.BAD_REQUEST,
                        "joined to course section successfully!", HttpStatus.OK));

        String resultMsg = studentService.joinCourseSection(courseSectionRequest, authentication);

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/check-scores/{termId}")
    public ResponseEntity<GetStudentScoreResponse> getTermScores(@PathVariable long termId,
                                                                 Authentication authentication) {
        return ResponseEntity.ok(studentService.fetchTermScores(termId, authentication));
    }
}
