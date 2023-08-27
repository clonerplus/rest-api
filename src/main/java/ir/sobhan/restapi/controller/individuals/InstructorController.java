package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/all-instructors")
    public ResponseEntity<ListResponse<Instructor>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/instructor")
    public ResponseEntity<String> authorizeInstructor(@RequestParam String username,
                                                      @RequestBody Instructor instructor) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("Invalid username!", HttpStatus.BAD_REQUEST,
                        "username not found!", HttpStatus.NOT_FOUND,
                        "Authorized user to instructor limits successfully", HttpStatus.OK));

        String resultMsg = instructorService.authorizeInstructor(username, instructor);

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }
}
