package ir.sobhan.restapi.conroller.individuals;

import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.model.entity.individual.CustomUser;
import ir.sobhan.restapi.model.input.individual.RegisterRequest;
import ir.sobhan.restapi.service.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomUserControllerTest {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    CustomUserRepository customUserRepository;
    @Autowired
    TestUtils testUtils;

    @Test
    void registerCustomUser() {

        RegisterRequest registerRequest = testUtils.generateStudentRegisterRequest();

        authenticationService.register(registerRequest);

        Optional<CustomUser> customUser = customUserRepository.findByUsername("ahmad10");

        assertThat(customUser.isEmpty()).isEqualTo(false);
    }
}
