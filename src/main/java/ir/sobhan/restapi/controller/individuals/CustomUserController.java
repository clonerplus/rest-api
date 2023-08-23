package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomUserController {
    private final CustomUserRepository customUserRepository;

    @Autowired
    public CustomUserController(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }


    @GetMapping("/all-users")
    public List<CustomUser> getAllRegisteredUsers() {
        return customUserRepository.findAll();
    }

}
