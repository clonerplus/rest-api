package ir.sobhan.restapi.auth;

import ir.sobhan.restapi.model.individual.Instructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    CUSTOM_USER_READ("custom_user:read"),
    CUSTOM_USER_UPDATE("custom_user:update"),
    CUSTOM_USER_DELETE("custom_user:delete"),
    CUSTOM_USER_CREATE("custom_user:create"),
    INSTRUCTOR_READ("instructor:read"),
    INSTRUCTOR_UPDATE("instructor:update"),
    INSTRUCTOR_DELETE("instructor:delete"),
    INSTRUCTOR_CREATE("instructor:create")

    ;

    @Getter
    private final String permission;
}

