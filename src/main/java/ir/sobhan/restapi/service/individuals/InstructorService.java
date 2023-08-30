package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.controller.exception.ApiRequestException;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.InstructorRepository;
import ir.sobhan.restapi.model.entity.individual.CustomUser;
import ir.sobhan.restapi.model.entity.individual.Instructor;
import ir.sobhan.restapi.model.input.individual.InstructorRequest;
import ir.sobhan.restapi.model.output.ListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class InstructorService {
    private final CustomUserRepository customUserRepository;
    private final InstructorRepository instructorRepository;

    public InstructorService(CustomUserRepository customUserRepository, InstructorRepository instructorRepository) {
        this.customUserRepository = customUserRepository;
        this.instructorRepository = instructorRepository;
    }

    public ListResponse<Instructor> getAllInstructors() {
        return ListResponse.<Instructor>builder()
                .responseList(instructorRepository.findAll())
                .build();
    }

    public void authorizeInstructor(String username, InstructorRequest instructorRequest) {
        if (username == null)
            throw new ApiRequestException("Invalid username!", HttpStatus.BAD_REQUEST);

        CustomUser customUser = customUserRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("username not found!", HttpStatus.NOT_FOUND));

        var instructor = Instructor.builder()
                .rank(instructorRequest.getRank())
                .customUser(customUser)
                .build();

        customUser.setInstructor(instructor);
        customUser.setRole(Role.INSTRUCTOR);
        instructorRepository.save(instructor);
    }
}
