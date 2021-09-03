package com.security.demo.security;

import java.util.Set;

import com.google.common.collect.Sets;
import static com.security.demo.security.UserPermission.*;

public enum Role {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

    private final Set<UserPermission> permissions;

    /**
     * @param permissions
     */
    private Role(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the permissions
     */
    public Set<UserPermission> getPermissions() {
        return permissions;
    }
    
}
