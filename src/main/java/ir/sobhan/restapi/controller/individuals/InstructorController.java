package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.CustomUserService;
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
        return instructorService.getAllInstructors();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/instructor")
    public ResponseEntity<String> authorizeInstructor(@RequestParam String username, @RequestBody Instructor instructor) {

        return instructorService.authorizeInstructor(username, instructor);
    }
}
