package ir.sobhan.restapi.controller.auth;

import ir.sobhan.restapi.request.AuthenticationRequest;
import ir.sobhan.restapi.request.RegisterRequest;
import ir.sobhan.restapi.response.AuthenticationResponse;
import ir.sobhan.restapi.service.auth.AuthenticationService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
//@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

//    @PreAuthorize("hasAuthority('admin:read')")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/test")
    public String test() {
        return "yep";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }


}

