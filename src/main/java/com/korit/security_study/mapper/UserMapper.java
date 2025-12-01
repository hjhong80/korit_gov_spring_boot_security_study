package com.korit.security_study.mapper;

import com.korit.security_study.dto.SignupReqDto;
import com.korit.security_study.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Integer userId);
    List<User> findAll();
    int addUser(User user);
    int updatePassword(User user);
    int removeUser(Integer userId);
    Optional<User> findByEmail(String email);
}
