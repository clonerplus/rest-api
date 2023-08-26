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

    public ListResponse<Instructor> getAllInstructors() {

        return ListResponse.<Instructor>builder()
                .responseList(instructorRepository.findAll())
                .build();

    }

    public String authorizeInstructor(String username, Instructor instructor) {

        if (username == null)
            return "Invalid username!";

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return "username not found!";

        instructor.setCustomUser(customUser.get());
        customUser.get().setInstructor(instructor);
        customUser.get().setRole(Role.INSTRUCTOR);
        instructorRepository.save(instructor);


        return "Authorized user to instructor limits successfully";
    }
}
