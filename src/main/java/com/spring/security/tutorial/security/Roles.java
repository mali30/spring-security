package com.spring.security.tutorial.security;

import com.google.common.collect.Sets;

import java.util.Set;

public enum Roles {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            UserPermissions.COURSE_READ,
            UserPermissions.COURSE_WRITE,
            UserPermissions.STUDENT_WRITE,
            UserPermissions.STUDENT_READ
    ));

    private final Set<UserPermissions> permissions;

    Roles(Set<UserPermissions> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermissions> permissions() {
        return permissions;
    }
}
