package ir.sobhan.restapi.controller.auth;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.request.individuals.auth.AuthenticationRequest;
import ir.sobhan.restapi.request.individuals.auth.RegisterRequest;
import ir.sobhan.restapi.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authenticationService.register(request);

            return ResponseEntity.ok("user successfully registered!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            var authenticationResponse = authenticationService.authenticate(request);

            return ResponseEntity.ok(String.valueOf(authenticationResponse));

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
