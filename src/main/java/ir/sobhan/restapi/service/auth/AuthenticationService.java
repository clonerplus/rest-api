package ir.sobhan.restapi.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.controller.exception.ApiRequestException;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.RedisTokenRepository;
import ir.sobhan.restapi.model.entity.individual.CustomUser;
import ir.sobhan.restapi.model.input.auth.AuthenticationRequest;
import ir.sobhan.restapi.model.input.individual.RegisterRequest;
import ir.sobhan.restapi.model.output.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationService {
    private final CustomUserRepository customUserRepository;
    private final RedisTokenRepository redisTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public AuthenticationService(CustomUserRepository customUserRepository,
                                 RedisTokenRepository redisTokenRepository,
                                 PasswordEncoder passwordEncoder, JwtService jwtService,
                                 AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.customUserRepository = customUserRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    public void register(RegisterRequest request) {
        if (customUserRepository.existsByUsername(request.getUsername()))
                    throw new ApiRequestException("User with this username already exists!",
                            HttpStatus.BAD_REQUEST);
        var customUser = CustomUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .nationalId(request.getNationalId())
                .admin(request.isAdmin())
                .active(request.isActive())
                .role(Role.CUSTOM_USER)
                .build();

        customUserRepository.save(customUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        var user = customUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiRequestException("incorrect username or password!",
                        HttpStatus.FORBIDDEN));

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
        redisTokenRepository.saveToken(jwtToken, customUser.getUsername(), 3600000);
        redisTokenRepository.saveToken(customUser.getUsername(), jwtToken, 3600000);
    }

    private void revokeAllUserTokens(CustomUser customUser) {
        redisTokenRepository.getToken(customUser.getUsername())
                .ifPresent(token -> {
                    redisTokenRepository.deleteToken(token);
                    redisTokenRepository.deleteToken(customUser.getUsername());
                });
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ApiRequestException("invalid token", HttpStatus.FORBIDDEN);
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var customUser = this.customUserRepository.findByUsername(username)
                    .orElseThrow(() -> new ApiRequestException("invalid token", HttpStatus.FORBIDDEN));
            if (jwtService.isTokenValid(refreshToken, customUser)) {
                var accessToken = jwtService.generateToken(customUser);
                revokeAllUserTokens(customUser);
                saveUserToken(customUser, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                objectMapper.writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

