package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.InstructorRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.model.individual.Instructor;
import ir.sobhan.restapi.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstructorService {

    private final CustomUserRepository customUserRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(CustomUserRepository customUserRepository, InstructorRepository instructorRepository) {
        this.customUserRepository = customUserRepository;
        this.instructorRepository = instructorRepository;
    }

    public ResponseEntity<ListResponse<Instructor>> getAllInstructors() {

        ListResponse<Instructor> listResponse = ListResponse.<Instructor>builder()
                .responseList(instructorRepository.findAll())
                .build();

        return ResponseEntity.ok(listResponse);
    }

    public ResponseEntity<String> authorizeInstructor(String username, Instructor instructor) {

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
