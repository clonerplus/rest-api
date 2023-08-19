package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.individual.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class InstructorController {

    private final CustomUserRepository customUserRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorController(CustomUserRepository customUserRepository, InstructorRepository instructorRepository) {
        this.customUserRepository = customUserRepository;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping("/all-instructors")
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/instructor")
    public ResponseEntity<String> authorizeInstructor(@RequestParam String username, @RequestBody Instructor instructor) {

        if (username == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username!");

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username not found!");

        instructor.setCustomUser(customUser.get());
        customUser.get().setInstructor(instructor);
        customUser.get().setRole(Role.INSTRUCTOR);
        instructorRepository.save(instructor);

        return ResponseEntity.ok("Authorized user to instructor limits successfully");
    }
}
