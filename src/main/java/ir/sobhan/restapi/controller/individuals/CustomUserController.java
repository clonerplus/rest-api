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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserController(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/all-users")
    public List<CustomUser> getAllRegisteredUsers() {
        return customUserRepository.findAll();
    }

//    @PostMapping("/register")
//    public String registerUser(@RequestBody CustomUser customUser) {
//        String rawPassword = customUser.getPassword();
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//        customUser.setPassword(encodedPassword);
//
//        customUserRepository.save(customUser);
//
//        return "Registered successfully";
//    }
}
