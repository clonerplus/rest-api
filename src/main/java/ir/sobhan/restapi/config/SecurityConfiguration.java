package ir.sobhan.restapi.config;

import ir.sobhan.restapi.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers(
                                        new AntPathRequestMatcher("/register"),
                                        new AntPathRequestMatcher("/all-users"),
                                        new AntPathRequestMatcher("/all-instructors"),
                                        new AntPathRequestMatcher("/authenticate"),
                                        new AntPathRequestMatcher("/refresh-token"),
                                        new AntPathRequestMatcher("/all*"),
                                        new AntPathRequestMatcher("/api/v1/auth/**"),
                                        new AntPathRequestMatcher("/v2/api-docs"),
                                        new AntPathRequestMatcher("/v3/api-docs"),
                                        new AntPathRequestMatcher("/v3/api-docs/**"),
                                        new AntPathRequestMatcher("/swagger-resources"),
                                        new AntPathRequestMatcher("/configuration/ui"),
                                        new AntPathRequestMatcher("/configuration/security"),
                                        new AntPathRequestMatcher("/swagger-ui/**"),
                                        new AntPathRequestMatcher("/webjars/**"),
                                        new AntPathRequestMatcher("/swagger-ui.html")
                                ).permitAll()
                .anyRequest()
                .authenticated())
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()));

        return http.build();
    }
}

