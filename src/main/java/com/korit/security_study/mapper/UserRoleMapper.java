package com.korit.security_study.mapper;

import com.korit.security_study.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRoleMapper {
    Optional<UserRole> findByUserId(Integer userId);
    Optional<UserRole> findByRoleId(Integer roleId);
    int addUserRole(UserRole userRole);
    int updateUserRole(UserRole userRole);
}
