package ir.sobhan.restapi.conroller.individuals;

import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.request.RegisterRequest;
import ir.sobhan.restapi.service.individuals.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CustomUserControllerTest {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    CustomUserRepository customUserRepository;
    TestUtils testUtils;

    @Test
    void registerCustomUser() {

        RegisterRequest registerRequest = testUtils.generateStudentRegisterRequest();

        authenticationService.register(registerRequest);

        Optional<CustomUser> customUser = customUserRepository.findByUsername("ahmad10");

        assertThat(customUser.isEmpty()).isEqualTo(false);
    }
}