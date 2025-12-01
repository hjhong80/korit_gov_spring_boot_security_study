package com.korit.security_study.repository;

import com.korit.security_study.entity.User;
import com.korit.security_study.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    public Optional<User> findById(Integer userId) {
        System.out.println("UserRepository : findById");
        return userMapper.findById(userId);
    }

    public Optional<User> findByUsername(String username) {
        System.out.println("UserRepository : findByUsername");
        return userMapper.findByUsername(username);
    }

    public List<User> findAll() {
        System.out.println("UserRepository : findAll");
        return userMapper.findAll();
    }

    public Optional<User> addUser(User user) {
        System.out.println("UserRepository : addUser");
        try {
            int result = userMapper.addUser(user);
            System.out.println(result);
        } catch(DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public int updatePassword(User user) {
        System.out.println("UserRepository : editPassword");
        return userMapper.updatePassword(user);
    }

    public Optional<User> findByEmail(String email) {
        System.out.println("UserRepository : findByEmail");
        return userMapper.findByEmail(email);
    }
}
