package ir.sobhan.restapi.service.auth;

import ir.sobhan.restapi.dao.RedisTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final RedisTokenRepository redisTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = redisTokenRepository.getToken(jwt)
                .orElse(null);
        redisTokenRepository.deleteToken(storedToken);
        redisTokenRepository.deleteToken(jwt);
    }
}

