package com.korit.security_study.repository;

import com.korit.security_study.entity.UserRole;
import com.korit.security_study.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRoleRepository {
    private final UserRoleMapper userRoleMapper;

    public int addUserRole(UserRole userRole) {
        System.out.println("UserRoleRepository : addUserRole");
        return userRoleMapper.addUserRole(userRole);
    }
}
