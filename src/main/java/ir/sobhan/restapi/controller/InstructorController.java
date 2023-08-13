package ir.sobhan.restapi.controller;

import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.InstructorRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.model.individual.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class InstructorController {

    private final CustomUserRepository customUserRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorController(CustomUserRepository customUserRepository, InstructorRepository instructorRepository, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping("/all-instructors")
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @PostMapping("/authorize/instructor")
    public String authorizeInstructor(@RequestParam String username, @RequestBody Instructor instructor) {

        if (username == null)
            return "Invalid username!";

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return "username not found!";

        instructor.setCustomUser(customUser.get());
        customUser.get().setInstructor(instructor);
        instructorRepository.save(instructor);

        return "Authorized user to instructor limits successfully";
    }
}
