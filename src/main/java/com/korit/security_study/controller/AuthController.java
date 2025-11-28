package com.korit.security_study.controller;

import com.korit.security_study.dto.SigninReqDto;
import com.korit.security_study.dto.SignupReqDto;
import com.korit.security_study.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto) {
        System.out.println("AuthController : signup");
        return ResponseEntity.ok(authService.addUser(signupReqDto));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        System.out.println("AuthController : getAll");
        return ResponseEntity.ok(authService.getUserAll());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        System.out.println("AuthController : getUserByUsername");
        return ResponseEntity.ok(authService.getUserByUsername(username));
    }

    @GetMapping("/userid/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable Integer userId) {
        System.out.println("AuthController : getUserByUserId");
        return ResponseEntity.ok(authService.getUserByUserId(userId));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        System.out.println("AuthController : test");
        return ResponseEntity.ok("test");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto) {
        System.out.println("AuthController : signin");
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }
}
