package ir.sobhan.restapi.auth;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
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
    STAFF(
            Set.of(
                    STAFF_READ,
                    STAFF_UPDATE,
                    STAFF_DELETE,
                    STAFF_CREATE
            )
    ),
    INSTRUCTOR(
            Set.of(
                    INSTRUCTOR_READ,
                    INSTRUCTOR_UPDATE,
                    INSTRUCTOR_DELETE,
                    INSTRUCTOR_CREATE
            )
    ),
    STUDENT(
            Set.of(
                    STUDENT_READ,
                    STUDENT_UPDATE,
                    STUDENT_DELETE,
                    STUDENT_CREATE
            )
    );

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

