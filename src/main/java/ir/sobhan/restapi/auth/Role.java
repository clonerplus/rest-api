package ir.sobhan.restapi.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ir.sobhan.restapi.auth.Permission.*;

@RequiredArgsConstructor
public enum Role {

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    ),
    CUSTOM_USER(
            Set.of(
                    CUSTOM_USER_READ,
                    CUSTOM_USER_UPDATE,
                    CUSTOM_USER_DELETE,
                    CUSTOM_USER_CREATE
            )
    ),
    INSTRUCTOR(
            Set.of(
                    INSTRUCTOR_READ,
                    INSTRUCTOR_UPDATE,
                    INSTRUCTOR_DELETE,
                    INSTRUCTOR_CREATE
            )
    )
    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

