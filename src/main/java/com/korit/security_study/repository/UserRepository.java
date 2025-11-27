package com.korit.security_study.repository;

import com.korit.security_study.entity.User;
import com.korit.security_study.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

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
            userMapper.addUser(user);
        } catch(DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public int editUser(User user) {
        System.out.println("UserRepository : editUser");
        return userMapper.addUser(user);
    }
}
