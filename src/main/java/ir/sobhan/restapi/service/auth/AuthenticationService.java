package ir.sobhan.restapi.service.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sobhan.restapi.auth.*;
import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.request.*;
import ir.sobhan.restapi.response.AuthenticationResponse;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
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

    @Autowired
    public AuthenticationService(CustomUserRepository customUserRepository,
                                  RedisTokenRepository redisTokenRepository,
                                 PasswordEncoder passwordEncoder, JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.customUserRepository = customUserRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequest request) {
        if (customUserRepository.findByUsername(request.getUsername()).isPresent())
            throw new ApiRequestException("user with this username already exists!",
                    HttpStatus.NOT_FOUND);

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
//        var token = Token.builder()
//                .customUser(customUser)
//                .token(jwtToken)
//                .tokenType(Token.TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
        redisTokenRepository.saveToken(jwtToken, customUser.getUsername(), 3600000);
        redisTokenRepository.saveToken(customUser.getUsername(), jwtToken, 3600000);

    }

    private void revokeAllUserTokens(CustomUser customUser) {

        var lastToken = redisTokenRepository.getToken(customUser.getUsername());

        redisTokenRepository.deleteToken(lastToken.get());
        redisTokenRepository.deleteToken(customUser.getUsername());

//        var validUserTokens = tokenRepository.findAllValidTokenByUser(customUser.getId());
//
//        if (validUserTokens.isEmpty())
//            return;
//
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//
//        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response
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

