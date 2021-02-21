package com.spring.security.tutorial.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Roles {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            UserPermissions.COURSE_READ,
            UserPermissions.COURSE_WRITE,
            UserPermissions.STUDENT_WRITE,
            UserPermissions.STUDENT_READ
    )),
    ADMIN_TRAINEE(Sets.newHashSet(
            UserPermissions.COURSE_READ,
            UserPermissions.STUDENT_READ
    ));

    private final Set<UserPermissions> permissions;

    Roles(Set<UserPermissions> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermissions> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
