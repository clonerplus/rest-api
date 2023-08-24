package ir.sobhan.restapi.service.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sobhan.restapi.request.AuthenticationRequest;
import ir.sobhan.restapi.response.AuthenticationResponse;
import ir.sobhan.restapi.request.RegisterRequest;
import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.auth.Token;
import ir.sobhan.restapi.service.auth.JwtService;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.TokenRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AuthenticationService {
    private final CustomUserRepository customUserRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(CustomUserRepository customUserRepository,
                                 TokenRepository tokenRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.customUserRepository = customUserRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String register(RegisterRequest request) {
        var customUser = CustomUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .nationalId(request.getNationalId())
                .admin(request.isAdmin())
                .active(request.isActive())
                .role(Role.CUSTOM_USER)
                .build();
        var savedCustomUser = customUserRepository.save(customUser);

        return "user successfully registered!";
//        var jwtToken = jwtService.generateToken(customUser);
//        var refreshToken = jwtService.generateRefreshToken(customUser);
//        saveUserToken(savedCustomUser, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = customUserRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(CustomUser customUser, String jwtToken) {
        var token = Token.builder()
                .customUser(customUser)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(CustomUser customUser) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(customUser.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var customUser = this.customUserRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, customUser)) {
                var accessToken = jwtService.generateToken(customUser);
                revokeAllUserTokens(customUser);
                saveUserToken(customUser, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

