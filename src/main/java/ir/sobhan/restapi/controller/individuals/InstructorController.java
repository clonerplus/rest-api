package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.model.individual.Instructor;
import ir.sobhan.restapi.request.individuals.auth.InstructorRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
                                                      @RequestBody InstructorRequest instructorRequest) {
        try {
            instructorService.authorizeInstructor(username, instructorRequest);

            return ResponseEntity.ok("Authorized user to instructor limits successfully");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
